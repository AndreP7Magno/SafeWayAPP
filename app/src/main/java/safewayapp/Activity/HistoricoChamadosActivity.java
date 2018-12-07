package safewayapp.Activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.Api.HistoricApi;
import safewayapp.Component.DaggerHistoricoChamadosComponent;
import safewayapp.Module.AppModule;
import safewayapp.Module.NetModule;
import safewayapp.Module.RoomModule;
import safewayapp.R;

public class HistoricoChamadosActivity extends AppCompatActivity {

    @BindView(R.id.recycleViewHistoricoAtividades)
    RecyclerView mRecycleHistoricoAtividades;

    @BindView(R.id.coordinator_historico_atividades)
    CoordinatorLayout coordinatorHistoricoAtividades;

    @Inject
    HistoricApi historicApi;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_chamados);

        initActivity();
    }

    private void initActivity(){
        ButterKnife.bind(this);

        DaggerHistoricoChamadosComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .netModule(new NetModule(getString(R.string.baseURL)))
                .build()
                .inject(this);
    }
}
