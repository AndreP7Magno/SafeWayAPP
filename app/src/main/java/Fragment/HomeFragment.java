package Fragment;

import android.arch.lifecycle.Observer;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.Activity.LoginActivity;
import safewayapp.Component.DaggerContatoComponent;
import safewayapp.Helper.ProgressDialogHelper;
import safewayapp.Helper.SnackBarHelper;
import safewayapp.Module.AppModule;
import safewayapp.Module.RoomModule;
import safewayapp.Persistence.Contato;
import safewayapp.R;
import safewayapp.Repository.IContatoDataSource;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

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
    IContatoDataSource contatoDataSource;

    private boolean fabExpanded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, rootView);

        DaggerContatoComponent.builder()
                .appModule(new AppModule(getActivity().getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .build()
                .inject(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        closeSubMenusFab();

        fabReportarAssedio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Assedio", Toast.LENGTH_SHORT).show();
            }
        });

        fabReportarSituacaoPerigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialogHelper dialog = new ProgressDialogHelper(getActivity(), "Aguarde", "Enviando mensagem aos seus contatos...");
                dialog.show();
                contatoDataSource.getAll().observe(getActivity(), new Observer<List<Contato>>() {
                    @Override
                    public void onChanged(@Nullable List<Contato> contatos) {
                        if (contatos.isEmpty()) {
                            SnackBarHelper.getInstance(home_coordinator).showBottomNaviagtion("Lista de contatos vazia", Snackbar.LENGTH_LONG);
                            dialog.dismiss();
                            return;
                        }
                        for (Contato item:
                             contatos) {
                            sendSMS(item.getTelefone(), "Teste msg");
                        }
                        SnackBarHelper.getInstance(home_coordinator).showBottomNaviagtion("Mensagens sendo enviadas ao seus contatos", Snackbar.LENGTH_LONG);
                        dialog.dismiss();
                    }
                });
            }
        });

        fabAddReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true) {
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
    public void onMapReady(GoogleMap map) {
        double latitude = -30.083389;
        double longitude = -51.2239894;
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Casa"));

        //map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        //Zoom da câmera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));

        //Polyline polyline1 = map.addPolyline(new PolylineOptions()
        //.clickable(true)
        //.add(
        //new LatLng(-35.016, 143.321),
        //new LatLng(-34.747, 145.592),
        //new LatLng(-34.364, 147.891),
        //new LatLng(-33.501, 150.217),
        //new LatLng(-32.306, 149.248),
        //new LatLng(-32.491, 147.309)));
        // Store a data object with the polyline, used here to indicate an arbitrary type.
        //polyline1. setTag("A");


        //Mostrar a localização atual do dispositivo
        //map.setMyLocationEnabled(true);

        //Informações de tráfico
        //map.setTrafficEnabled(true);
    }
}
