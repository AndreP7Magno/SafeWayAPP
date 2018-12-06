package safewayapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import safewayapp.Activity.HistoricoAtividadesActivity;
import safewayapp.Activity.LoginActivity;
import safewayapp.Api.ContactApi;
import safewayapp.Api.RecordApi;
import safewayapp.Api.response.ContactResponse;
import safewayapp.Api.response.RecordResponse;
import safewayapp.Component.DaggerLoginComponent;
import safewayapp.Helper.DialogHelper;
import safewayapp.Helper.IOUtil;
import safewayapp.Helper.ProgressDialogHelper;
import safewayapp.Module.AppModule;
import safewayapp.Module.NetModule;
import safewayapp.Module.RoomModule;
import safewayapp.Persistence.Contato;
import safewayapp.Persistence.Record;
import safewayapp.Persistence.Usuario;
import safewayapp.R;
import safewayapp.Repository.IContatoDataSource;
import safewayapp.Repository.IRecordDataSource;
import safewayapp.Repository.IUsuarioDataSource;

public class ProfileFragment extends Fragment {

    @BindView(R.id.relativeHistoricoAtividades)
    RelativeLayout relativeHistoricoAtividades;

    @BindView(R.id.relativeHistoricoSituacaoPerigo)
    RelativeLayout relativeHistoricoSituacaoPerigo;

    @BindView(R.id.relativeAtualizarRecord)
    RelativeLayout relativeAtualizarRecord;

    @BindView(R.id.relativeSair)
    RelativeLayout relativeSair;

    @BindView(R.id.txtNomeUsuario)
    TextView txtNomeUsuario;

    @BindView(R.id.txtCPFUsuario)
    TextView txtCPFUsuario;

    @BindView(R.id.txtEmailUsuario)
    TextView txtEmailUsuario;

    @BindView(R.id.imgAvatar)
    ImageView imgAvatar;

    private byte[] diaphragm;

    @Inject
    IRecordDataSource recordDataSource;

    @Inject
    IContatoDataSource contatoDataSource;

    @Inject
    IUsuarioDataSource usuarioDataSource;

    @Inject
    RecordApi recordApi;

    @Inject
    ContactApi contactApi;

    @Inject
    public SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initFragment(view);

        String cpf = sharedPreferences.getString("CPF", "");
        txtCPFUsuario.setText(cpf);
        txtNomeUsuario.setText(sharedPreferences.getString("NomeUsuario", ""));
        txtEmailUsuario.setText(sharedPreferences.getString("EmailUsuario", ""));

        formatImage(cpf);

        relativeHistoricoAtividades.setOnClickListener(OnHistoricoAtividades);
        relativeHistoricoSituacaoPerigo.setOnClickListener(OnHistoricoSituacaoPerigo);
        relativeAtualizarRecord.setOnClickListener(onAtualizarRecordClickListener);
        relativeSair.setOnClickListener(OnSairListener);

        return view;
    }

    private void formatImage(String cpf) {
        RequestOptions requestOptions = new RequestOptions();
        byte[] fotoUsuario;

        fotoUsuario = getAddPhoto(getActivity().getApplicationContext());

        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.override(100, 100);
        requestOptions.transforms(new CircleCrop());
        requestOptions.signature(new Key() {
            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                String.valueOf(System.currentTimeMillis());
            }
        });

        imgAvatar.setImageBitmap(null);
        Glide.with(getActivity().getApplicationContext()).load(fotoUsuario).apply(requestOptions).into(imgAvatar);
    }

    public byte[] getAddPhoto(Context context) {
        if (diaphragm == null) {
            try {
                InputStream inputStream = context.getAssets().open("add_photo.png");
                diaphragm = IOUtil.toByteArray(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return diaphragm;
    }

    private void initFragment(View view) {
        ButterKnife.bind(this, view);

        DaggerLoginComponent.builder()
                .appModule(new AppModule(getActivity().getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .netModule(new NetModule(getString(R.string.baseURL)))
                .build()
                .inject(this);
    }

    View.OnClickListener OnHistoricoAtividades = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(getActivity(), HistoricoAtividadesActivity.class);
            startActivity(it);
        }
    };

    View.OnClickListener OnHistoricoSituacaoPerigo = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Histórico de Situação de Perigo", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener onAtualizarRecordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogHelper.getInstance().
                    ShowAlertPositiveNegative(getActivity(), R.string.deseja_atualizar, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            recordDataSource.deleteAll();
                            contatoDataSource.deleteAll();

                            final ProgressDialogHelper d = new ProgressDialogHelper(getActivity(), "Atualizando dados", "Aguarde enquanto estamos atualizando os registros...");
                            d.show();

                            recordApi.getAll().enqueue(new Callback<List<RecordResponse>>() {
                                @Override
                                public void onResponse(Call<List<RecordResponse>> call, Response<List<RecordResponse>> response) {
                                    if (response.code() == HttpURLConnection.HTTP_OK) {
                                        List<RecordResponse> data = response.body();
                                        for (RecordResponse item : data
                                                ) {
                                            salvarRecord(item);
                                        }

                                        String cpf = sharedPreferences.getString("CPF", "");
                                        Usuario usuario = usuarioDataSource.getByCPF(cpf);
                                        final String user = usuario.getId();

                                        contactApi.getContact(user).enqueue(new Callback<List<ContactResponse>>() {
                                            @Override
                                            public void onResponse(Call<List<ContactResponse>> call, Response<List<ContactResponse>> response) {
                                                if (response.code() == HttpURLConnection.HTTP_OK) {
                                                    List<ContactResponse> dataContato = response.body();
                                                    for (ContactResponse item : dataContato
                                                            ) {
                                                        salvarContact(item);
                                                    }

                                                    d.dismiss();

                                                    Intent i = getActivity().getBaseContext().getPackageManager()
                                                            .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    getActivity().startActivity(i);
                                                    getActivity().finish();
                                                } else {
                                                    try {
                                                        d.dismiss();
                                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                                        Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<ContactResponse>> call, Throwable t) {
                                                d.dismiss();
                                                Toast.makeText(getContext(), "ERRO AO SALVAR", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        try {
                                            d.dismiss();
                                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                                            Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<RecordResponse>> call, Throwable t) {
                                    d.dismiss();
                                    Toast.makeText(getContext(), "ERRO AO SALVAR", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }, new MaterialDialog.SingleButtonCallback() {

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    });
        }
    };

    private void salvarRecord(RecordResponse data) {
        recordDataSource.insert(
                new Record(
                        data.get_id(),
                        data.getDate(),
                        data.getDescription(),
                        data.getLatitude(),
                        data.getLongitude(),
                        data.getSeverity(),
                        data.getUser()));
    }

    private void salvarContact(ContactResponse data) {
        contatoDataSource.insert(
                new Contato(
                        data.getId(),
                        data.getName(),
                        data.getPhonenumber()));
    }

    View.OnClickListener OnSairListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogHelper.getInstance().
                    ShowAlertPositiveNegative(getActivity(), R.string.deseja_sair, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("CPF", "");

                            editor.commit();

                            Intent it = new Intent(getActivity(), LoginActivity.class);
                            getActivity().startActivity(it);
                            getActivity().finish();
                        }
                    }, new MaterialDialog.SingleButtonCallback() {

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    });
        }
    };
}
