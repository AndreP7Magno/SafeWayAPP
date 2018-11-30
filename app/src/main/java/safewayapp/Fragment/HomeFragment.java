package safewayapp.Fragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
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

    private Double latitude;
    private Double longitude;

    //private FusedLocationProviderClient mFusedLocationClient;
    Geocoder geocoder;
    List<Address> addresses;

    SupportMapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initFragment(view);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //geocoder = new Geocoder(getActivity(), Locale.getDefault());
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //getLocation();

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
            if (position == null){
                //chamar intent
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
            // May throw an IOException
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

    private void initRecordMap(final GoogleMap map, final ProgressDialogHelper dialog){
        recordDataSource.getAll().observe(this, new Observer<List<Record>>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onChanged(@Nullable List<Record> records) {
                Location location = GPSHelper.getInstance().getLocation();
                if (location == null){
                    new DialogHelper().showGPSSettingDialog(getActivity());
                    return;
                }

                for (Record item : records
                     ) {
                    double latitude = Double.parseDouble(item.getLatitute());
                    double longitude = Double.parseDouble(item.getLongitude());
                    map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(item.getDescricao()));
                }
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                map.setMyLocationEnabled(true);
                boolean sucess = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        getContext(), R.raw.style_json));
                dialog.dismiss();
            }
        });
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

            boolean continuar = true;
            while (continuar) {
                if (!latitude.isNaN() && !longitude.isNaN())
                    continuar = false;
                    /*else
                        getLocation();*/
            }

            contatoDataSource.getAll().observe(getActivity(), new Observer<List<Contato>>() {
                @Override
                public void onChanged(@Nullable List<Contato> contatos) {
                    try {
                        if (contatos.isEmpty()) {
                            SnackBarHelper.getInstance(home_coordinator).showBottomNaviagtion("Lista de contatos vazia", Snackbar.LENGTH_LONG);
                            dialog.dismiss();
                            return;
                        }

                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        String endereco = addresses.get(0).getAddressLine(0);

                        for (Contato item :
                                contatos) {
                            sendSMS(item.getTelefone(), "Olá " + item.getNome() + "!/n Você foi marcado como contato seguro de NOME USUARIO e esta pessoa no momento corre certo perigo./nEla se encontra no seguinte endereço " + endereco + "./nPor favor, entre com contato com ela./n/n MENSAGEM ENVIADA AUTOMATICAMENTE PELO APLICATIVO SAFEWAY");
                        }
                        SnackBarHelper.getInstance(home_coordinator).showBottomNaviagtion("Mensagens sendo enviadas ao seus contatos", Snackbar.LENGTH_LONG);
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*@SuppressLint("MissingPermission")
    private void getLocation() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        });
    }*/
}
