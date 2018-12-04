package safewayapp.Fragment;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import safewayapp.Activity.NovoAssedioActivity;
import safewayapp.Api.RecordApi;
import safewayapp.Api.request.RecordRequest;
import safewayapp.Api.response.RecordResponse;
import safewayapp.Component.DaggerHomeComponent;
import safewayapp.Helper.DialogHelper;
import safewayapp.Helper.GPSHelper;
import safewayapp.Helper.ProgressDialogHelper;
import safewayapp.Helper.SnackBarHelper;
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

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private static final int REQUEST_NOVO_ASSEDIO = 998;

    @BindView(R.id.fabAddReport)
    FloatingActionButton fabAddReport;

    @BindView(R.id.layoutFabReportarAssedio)
    LinearLayout layoutFabReportarAssedio;

    @BindView(R.id.layoutFabReportarSituacaPerigo)
    LinearLayout layoutFabReportarSituacaoPerigo;

    @BindView(R.id.fabReportarAssedio)
    FloatingActionButton fabReportarAssedio;

    @BindView(R.id.fabReportarSituacaoPerigo)
    FloatingActionButton fabReportarSituacaoPerigo;

    @BindView(R.id.home_coordinator)
    CoordinatorLayout home_coordinator;

    @Inject
    public RecordApi recordApi;

    @Inject
    IContatoDataSource contatoDataSource;

    @Inject
    IUsuarioDataSource usuarioDataSource;

    @Inject
    IRecordDataSource recordDataSource;

    @Inject
    public SharedPreferences sharedPreferences;

    private boolean fabExpanded = false;

    SupportMapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initFragment(view);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    private void initFragment(View view) {
        ButterKnife.bind(this, view);

        DaggerHomeComponent.builder()
                .appModule(new AppModule(getActivity().getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .netModule(new NetModule(getString(R.string.baseURL)))
                .build()
                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        closeSubMenusFab();

        fabReportarAssedio.setOnClickListener(onReportAssedioClickListener);
        fabAddReport.setOnClickListener(onAddReportClickListener);
        fabReportarSituacaoPerigo.setOnClickListener(onReportSituacaoPerigoClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NOVO_ASSEDIO && data != null) {
            final ProgressDialogHelper dialog = new ProgressDialogHelper(getActivity(), "Aguarde", "Registrando Assédio...");
            dialog.show();

            Bundle params = data.getExtras();
            String Endereco = params.getString("Endereco");
            final String Descricao = params.getString("Descricao");
            Boolean Grave = params.getBoolean("cbGrave");
            Boolean Medio = params.getBoolean("cbMedio");
            Boolean Baixa = params.getBoolean("cbBaixa");
            final String DataAssedio = params.getString("data");

            String cpf = sharedPreferences.getString("CPF", "");
            Usuario usuario = usuarioDataSource.getByCPF(cpf);
            final String user = usuario.getId();

            LatLng position = getLocationFromAddress(Endereco);
            if (position == null) {
                Intent it = new Intent(getActivity(), NovoAssedioActivity.class);
                it.putExtra("endereco", Endereco);
                it.putExtra("descricao", Descricao);
                it.putExtra("data", DataAssedio);
                it.putExtra("cbGrave", Grave);
                it.putExtra("cbMedio", Medio);
                it.putExtra("cbBaixa", Baixa);
                startActivityForResult(it, REQUEST_NOVO_ASSEDIO);
                return;
            }

            final String latitude = String.valueOf(position.latitude);
            final String longitude = String.valueOf(position.longitude);

            final String severety;
            if (Grave)
                severety = "alta";
            else if (Medio)
                severety = "media";
            else
                severety = "baixa";


            recordApi.postCreate(new RecordRequest(user, latitude, longitude, severety, Descricao, DataAssedio)).enqueue(new retrofit2.Callback<RecordResponse>() {
                @Override
                public void onResponse(Call<RecordResponse> call, Response<RecordResponse> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        RecordResponse data = response.body();
                        salvarRecord(data);

                        SnackBarHelper.getInstance(home_coordinator).showBottomNaviagtion("Registro salvo com sucesso", Snackbar.LENGTH_LONG);
                        dialog.dismiss();
                    } else {
                        try {
                            dialog.dismiss();
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
                public void onFailure(Call<RecordResponse> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "ERRO AO SALVAR", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

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

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return p1;
    }

    private void closeSubMenusFab() {
        layoutFabReportarAssedio.setVisibility(View.INVISIBLE);
        layoutFabReportarSituacaoPerigo.setVisibility(View.INVISIBLE);
        fabAddReport.setImageResource(R.drawable.ic_add_white_24dp);
        fabExpanded = false;
    }

    private void openSubMenusFab() {
        layoutFabReportarAssedio.setVisibility(View.VISIBLE);
        layoutFabReportarSituacaoPerigo.setVisibility(View.VISIBLE);
        fabAddReport.setImageResource(R.drawable.ic_close_white_24dp);
        fabExpanded = true;
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        final ProgressDialogHelper dialog = new ProgressDialogHelper(getActivity(), "Aguarde", "Buscando dados cadastrados...");
        dialog.show();

        initRecordMap(map, dialog);
    }

    private void initRecordMap(final GoogleMap map, final ProgressDialogHelper dialog) {
        recordDataSource.getAll().observe(this, new Observer<List<Record>>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onChanged(@Nullable List<Record> records) {
                Location location = verificaLocalizacao();
                if (location == null) {
                    DialogHelper.getInstance().
                            ShowMessageGPSLocation(getActivity(), R.string.msg_ativar_gps, new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent i = getActivity().getBaseContext().getPackageManager()
                                            .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getActivity().startActivity(i);
                                    getActivity().finish();
                                }
                            });
                    return;
                }

                dialog.dismiss();
                for (Record item : records
                        ) {
                    Date data = null;
                    String formattedDate = null;
                    try {
                        data = new SimpleDateFormat("yyyy-MM-dd").parse(item.getData());
                        formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(data);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    double latitude = Double.parseDouble(item.getLatitute());
                    double longitude = Double.parseDouble(item.getLongitude());
                    if (item.getSeverity().equals("baixa"))
                        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(item.getDescricao()).snippet(formattedDate).icon(getMarkerIcon("#4CAF50")));
                    else if (item.getSeverity().equals("media"))
                        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(item.getDescricao()).snippet(formattedDate).icon(getMarkerIcon("#FFEB3B")));
                    else if (item.getSeverity().equals("alta"))
                        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(item.getDescricao()).snippet(formattedDate).icon(getMarkerIcon("#D32F2F")));
                }
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                map.setMyLocationEnabled(true);
                boolean sucess = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        getContext(), R.raw.style_json));
            }
        });
    }

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    private Location verificaLocalizacao() {
        return GPSHelper.getInstance().getLocation();
    }

    private View.OnClickListener onReportAssedioClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(getActivity(), NovoAssedioActivity.class);
            startActivityForResult(it, REQUEST_NOVO_ASSEDIO);
        }
    };

    private View.OnClickListener onAddReportClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (fabExpanded == true) {
                closeSubMenusFab();
            } else {
                openSubMenusFab();
            }
        }
    };

    private View.OnClickListener onReportSituacaoPerigoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ProgressDialogHelper dialog = new ProgressDialogHelper(getActivity(), "Aguarde", "Enviando mensagem aos seus contatos...");
            dialog.show();

            final Location location = verificaLocalizacao();

            contatoDataSource.getAll().observe(getActivity(), new Observer<List<Contato>>() {
                @Override
                public void onChanged(@Nullable List<Contato> contatos) {
                    try {
                        if (contatos.isEmpty()) {
                            SnackBarHelper.getInstance(home_coordinator).showBottomNaviagtion("Lista de contatos vazia", Snackbar.LENGTH_LONG);
                            dialog.dismiss();
                            return;
                        }

                        Geocoder coder = new Geocoder(getActivity());
                        List<Address> addresses = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        String endereco = addresses.get(0).getAddressLine(0);

                        String[] separadorEndereco = endereco.split("-");
                        String[] separadorFinal = separadorEndereco[1].trim().split(",");
                        String enderecoFinal = separadorEndereco[0].trim() + " - " + separadorFinal[0].trim();

                        String nomeUsuario = sharedPreferences.getString("NomeUsuario","");

                        for (Contato item : contatos) {
                            String textoMensagem = item.getNome() + ", o(a) " + nomeUsuario + " corre perigo. Esta localizado na " + enderecoFinal + ". MENSAGEM ENVIADA PELO SAFEWAY!";
                            textoMensagem = stripAccents(textoMensagem);
                            sendSMS(item.getTelefone(), textoMensagem);
                        }

                        //insert registro no banco

                        SnackBarHelper.getInstance(home_coordinator).showBottomNaviagtion("Mensagens sendo enviadas ao seus contatos", Snackbar.LENGTH_LONG);
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    public void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager sms = SmsManager.getDefault();
            PendingIntent sentPI;
            String SENT = "SMS_SENT";

            sentPI = PendingIntent.getBroadcast(getActivity(), 0,new Intent(SENT), 0);

            sms.sendTextMessage(phoneNumber, null, message, sentPI, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
