package safewayapp.Activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.Api.RecordApi;
import safewayapp.Component.DaggerHistoricoAtividadesComponent;
import safewayapp.Module.AppModule;
import safewayapp.Module.NetModule;
import safewayapp.Module.RoomModule;
import safewayapp.R;

public class HistoricoAtividades extends AppCompatActivity {

    @BindView(R.id.recycleViewHistoricoAtividade)
    RecyclerView mRecycleHistoricoAtividades;

    @BindView(R.id.coordinator_historico_atividades)
    CoordinatorLayout coordinatorHistoricoAtividades;

    @Inject
    public RecordApi recordApi;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_atividades);

        initActivity();


    }

    private void initActivity(){
        ButterKnife.bind(this);

        DaggerHistoricoAtividadesComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .netModule(new NetModule(getString(R.string.baseURL)))
                .build()
                .inject(this);
    }


}
