package safewayapp.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import safewayapp.Adapter.HistoricoChamadosAdapter;
import safewayapp.Api.HistoricApi;
import safewayapp.Api.response.EmergencyCall;
import safewayapp.Api.response.HistoricResponse;
import safewayapp.Component.DaggerHistoricoChamadosComponent;
import safewayapp.Module.AppModule;
import safewayapp.Module.NetModule;
import safewayapp.Module.RoomModule;
import safewayapp.Persistence.Usuario;
import safewayapp.R;
import safewayapp.Repository.IUsuarioDataSource;

public class HistoricoChamadosActivity extends AppCompatActivity {

    @BindView(R.id.recycleViewHistoricoChamados)
    RecyclerView mRecycleHistoricoChamados;

    @BindView(R.id.coordinator_historico_chamados)
    CoordinatorLayout coordinatorHistoricoChamados;

    @Inject
    IUsuarioDataSource usuarioDataSource;

    @Inject
    HistoricApi historicApi;

    @Inject
    SharedPreferences sharedPreferences;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_chamados);

        initActivity();

        mRecycleHistoricoChamados.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleHistoricoChamados.setLayoutManager(mLayoutManager);

        String cpf = sharedPreferences.getString("CPF", "");
        Usuario usuario = usuarioDataSource.getByCPF(cpf);
        final String user = usuario.getId();

        historicApi.getHistoric(user).enqueue(new Callback<HistoricResponse>() {
            @Override
            public void onResponse(Call<HistoricResponse> call, Response<HistoricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    HistoricResponse data = response.body();
                    List<EmergencyCall> lstChamados = data.getEmergencyCalls();

                    Collections.sort(lstChamados, new Comparator<EmergencyCall>() {
                        @Override
                        public int compare(EmergencyCall o1, EmergencyCall o2) {
                            return o2.data.compareTo(o1.data);
                        }
                    });

                    mAdapter = new HistoricoChamadosAdapter(lstChamados, mRecycleHistoricoChamados, HistoricoChamadosActivity.this);
                    mRecycleHistoricoChamados.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<HistoricResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERRO AO CONSULTAR DADOS", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initActivity() {
        ButterKnife.bind(this);

        DaggerHistoricoChamadosComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .netModule(new NetModule(getString(R.string.baseURL)))
                .build()
                .inject(this);
    }
}
