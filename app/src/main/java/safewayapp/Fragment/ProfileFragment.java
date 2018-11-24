package safewayapp.Fragment;

import android.content.Context;
import android.content.Intent;
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

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.Activity.LoginActivity;
import safewayapp.Helper.DialogHelper;
import safewayapp.Helper.IOUtil;
import safewayapp.R;

public class ProfileFragment extends Fragment {

    @BindView(R.id.relativeHistoricoAtividades)
    RelativeLayout relativeHistoricoAtividades;

    @BindView(R.id.relativeHistoricoSituacaoPerigo)
    RelativeLayout relativeHistoricoSituacaoPerigo;

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

        //Seta labels com dados do usuário

        String cpf = "86278746034";
        txtCPFUsuario.setText(cpf);
        txtNomeUsuario.setText("André Magno");
        txtEmailUsuario.setText("andremagno14@gmail.com");

        formatImage(cpf);

        relativeHistoricoAtividades.setOnClickListener(OnHistoricoAtividades);
        relativeHistoricoSituacaoPerigo.setOnClickListener(OnHistoricoSituacaoPerigo);
        relativeSair.setOnClickListener(OnSairListener);

        return view;
    }

    private void formatImage(String cpf) {
        RequestOptions requestOptions = new RequestOptions();
        byte[] fotoUsuario;

        fotoUsuario = getAddPhoto(getActivity().getApplicationContext());

        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.override(100,100);
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

        //DaggerLoginComponent
    }

    View.OnClickListener OnHistoricoAtividades = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Histórico de Atividades", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener OnHistoricoSituacaoPerigo = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Histórico de Situação de Perigo", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener OnSairListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DialogHelper.getInstance().
                    ShowAlertPositiveNegative(getActivity(), R.string.deseja_sair, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
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
