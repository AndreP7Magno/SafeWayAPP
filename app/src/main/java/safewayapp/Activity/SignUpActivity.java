package safewayapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import safewayapp.Api.AuthenticateApi;
import safewayapp.Api.request.LoginRequest;
import safewayapp.Api.response.LoginResponse;
import safewayapp.Component.DaggerLoginComponent;
import safewayapp.Helper.ProgressDialogHelper;
import safewayapp.Helper.ValidateCPFHelper;
import safewayapp.Module.AppModule;
import safewayapp.Module.NetModule;
import safewayapp.Module.RoomModule;
import safewayapp.Persistence.Usuario;
import safewayapp.R;
import safewayapp.Repository.IUsuarioDataSource;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.txtNomeCadastroContato)
    EditText nomeContato;

    @BindView(R.id.txtCpfCadastroContato)
    EditText cpfContato;

    @BindView(R.id.txtEmailCadastroContato)
    EditText emailContato;

    @BindView(R.id.txtSenhaCadastroContato)
    EditText senhaContato;

    @BindView(R.id.btn_cadastro)
    Button cadastrarButton;

    @BindView(R.id.btn_voltar)
    Button voltarButton;

    @Inject
    public AuthenticateApi authenticateApi;

    @Inject
    public IUsuarioDataSource usuarioDataSource;

    @Inject
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActivity();

        cadastrarButton.setOnClickListener(onCadastrarClickListener);
        voltarButton.setOnClickListener(onVoltarClickListener);
    }

    private void initActivity() {
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        DaggerLoginComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .netModule(new NetModule(getString(R.string.baseURL)))
                .build()
                .inject(this);
    }

    public void autenticarUsuario() {
        if (validarCadastro()) {
            final ProgressDialogHelper dialog = new ProgressDialogHelper(SignUpActivity.this, "Aguarde", "Cadastrando Usuário...");
            dialog.show();

            LoginRequest teste = new LoginRequest(nomeContato.getText().toString(), emailContato.getText().toString(), cpfContato.getText().toString(), senhaContato.getText().toString());
            Gson gson = new Gson();
            String json = gson.toJson(teste);

            authenticateApi.postCreate(new LoginRequest(nomeContato.getText().toString(), emailContato.getText().toString(), cpfContato.getText().toString(), senhaContato.getText().toString())).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        LoginResponse data = response.body();

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Usuário criado com sucesso", Toast.LENGTH_LONG).show();
                        inicializarAplicacao();
                    } else {
                        try {
                            dialog.dismiss();
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
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "ERRO AO CADASTRAR", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean validarCadastro() {
        if (nomeContato.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Nome é obrigatório", Toast.LENGTH_LONG).show();
            return false;
        }
        if (cpfContato.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "CPF é obrigatório", Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (!ValidateCPFHelper.validaCPF(cpfContato.getText().toString())) {
                Toast.makeText(getApplicationContext(), "CPF Inválido", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (emailContato.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Email é obrigatório", Toast.LENGTH_LONG).show();
            return false;
        }
        if (senhaContato.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Senha é obrigatório", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void inicializarAplicacao() {
        Intent it = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(it);
        finish();
    }

    View.OnClickListener onCadastrarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            autenticarUsuario();
        }
    };

    View.OnClickListener onVoltarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    };

}
