package safewayapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.Helper.ProgressDialogHelper;
import safewayapp.Helper.ValidateCPFHelper;
import safewayapp.R;


public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CADASTRO = 0;

    @BindView(R.id.txtCPFLogin)
    EditText _cpfText;

    @BindView(R.id.txtSenhaLogin)
    EditText _senhaText;

    @BindView(R.id.btn_login)
    Button _loginButton;

    @BindView(R.id.btn_cadastro)
    Button _cadastroButton;

    @BindView(R.id.btn_gmail)
    Button _gmailButton;

    @BindView(R.id.btn_facebook)
    Button _facebookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _cadastroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        _gmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Gmail", Toast.LENGTH_SHORT).show();
            }
        });

        _facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login() {
        final ProgressDialogHelper dialog = new ProgressDialogHelper(LoginActivity.this, "Aguarde", "Validando Usuário...");
        dialog.show();

        _loginButton.setEnabled(false);
        _cadastroButton.setEnabled(false);

        if(!validaCampos()){
            dialog.dismiss();
            _loginButton.setEnabled(true);
            _cadastroButton.setEnabled(true);
            Toast.makeText(getApplicationContext(), "Dados de preenchimento inválidos.", Toast.LENGTH_LONG).show();
            return;
        }

        // Lógica do login de usuário

        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        dialog.dismiss();

        _loginButton.setEnabled(true);
        _cadastroButton.setEnabled(true);
    }

    private boolean validaCampos() {
        if(_cpfText.getText().toString().trim().equals(""))
            return false;
        else{
            if (!ValidateCPFHelper.validaCPF(_cpfText.getText().toString()))
                return false;
        }
        if(_senhaText.getText().toString().trim().equals(""))
            return false;

        return true;
    }

}
