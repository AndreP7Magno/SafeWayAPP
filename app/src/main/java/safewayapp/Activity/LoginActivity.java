package safewayapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.redmadrobot.inputmask.MaskedTextChangedListener;

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


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.txtCPFLogin)
    EditText cpfText;

    @BindView(R.id.txtSenhaLogin)
    EditText senhaText;

    @BindView(R.id.btn_login)
    Button loginButton;

    @BindView(R.id.btn_cadastro)
    Button cadastroButton;

    @BindView(R.id.txtForgotPass)
    TextView txtForgotPass;

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

        initMaskCPF();

        loginButton.setOnClickListener(onEntrarClickListener);
        txtForgotPass.setOnClickListener(onEsqueceuSenhaListener);
        cadastroButton.setOnClickListener(onCadastroClickListener);
    }

    private void initActivity() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        DaggerLoginComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .netModule(new NetModule(getString(R.string.baseURL)))
                .build()
                .inject(this);
    }

    private void initMaskCPF() {
        final MaskedTextChangedListener listener = MaskedTextChangedListener.Companion.installOn(
                cpfText,
                "[000].[000].[000]-[00]",
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                        Log.d("TAG", extractedValue);
                        Log.d("TAG", String.valueOf(maskFilled));
                    }
                }
        );
    }

    private void autenticarUsuario() {
        String cpfSemMascara = cpfText.getText().toString()
                .replace("-", "")
                .replace(".", "")
                .trim();

        if (validarLogin(cpfSemMascara)) {
            final ProgressDialogHelper dialog = new ProgressDialogHelper(LoginActivity.this, "Aguarde", "Validando Usuário...");
            dialog.show();

            authenticateApi.postLogin(new LoginRequest(cpfSemMascara, senhaText.getText().toString())).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        LoginResponse data = response.body();

                        salvarToken(data);

                        salvarUsuario(data);

                        dialog.dismiss();
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
                    Toast.makeText(getApplicationContext(), "ERRO AO LOGAR", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean validarLogin(String cpfSemMascara) {
        if (cpfSemMascara.equals("")) {
            Toast.makeText(getApplicationContext(), "CPF é obrigatório", Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (!ValidateCPFHelper.validaCPF(cpfSemMascara)) {
                Toast.makeText(getApplicationContext(), "CPF Inválido", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if (senhaText.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Senha é obrigatório", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void salvarToken(LoginResponse data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        LoginResponse featureResponse = new LoginResponse();
        Calendar.getInstance().add(Calendar.HOUR, 12);

        Calendar cal = Calendar.getInstance();
        cal.setTime(Calendar.getInstance().getTime());
        cal.add(Calendar.HOUR_OF_DAY, 12);
        cal.getTime();

        //editor.putString("Token",data.getToken());
        editor.putLong("DataExpericao", cal.getTime().getTime());
        editor.putString("CPF", data.getCPF());
        editor.putString("NomeUsuario", data.getName());
        editor.putString("EmailUsuario", data.getEmail());
        editor.commit();
    }

    private void salvarUsuario(LoginResponse data) {
        if (usuarioDataSource.getByCPF(data.getCPF()) == null) {
            usuarioDataSource.insert(
                    new Usuario(
                            data.getCPF(),
                            data.getName(),
                            data.getEmail(),
                            data.get_id()));
        }
    }

    private void inicializarAplicacao() {
        Intent it = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(it);
        finish();
    }

    View.OnClickListener onEntrarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            autenticarUsuario();
        }
    };

    View.OnClickListener onEsqueceuSenhaListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(), "Esquece a senha", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener onCadastroClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        }
    };


}
