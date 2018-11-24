package safewayapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.InputMismatchException;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.Helper.ProgressDialogHelper;
import safewayapp.Helper.ValidateCPFHelper;
import safewayapp.R;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.txtNome)
    EditText _nomeText;

    @BindView(R.id.txtCpf)
    EditText _cpfText;

    @BindView(R.id.txtEmail)
    EditText _emailText;

    @BindView(R.id.txtSenha)
    EditText _senhaText;

    @BindView(R.id.btn_cadastro)
    Button _cadastrarButton;

    @BindView(R.id.btn_voltar)
    Button _voltarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        _cadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });

        _voltarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void cadastrar() {
        final ProgressDialogHelper dialog = new ProgressDialogHelper(SignUpActivity.this, "Aguarde", "Validando Usuário...");
        dialog.show();

        _cadastrarButton.setEnabled(false);
        _voltarButton.setEnabled(false);

        if(!validaCampos()){
            dialog.dismiss();
            _cadastrarButton.setEnabled(true);
            _voltarButton.setEnabled(true);
            Toast.makeText(getApplicationContext(), "Dados de preenchimento inválidos.", Toast.LENGTH_LONG).show();
            return;
        }

        // Lógica do cadastro de usuário

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        dialog.dismiss();

        _cadastrarButton.setEnabled(true);
        _voltarButton.setEnabled(true);
    }

    private boolean validaCampos() {
        if(_nomeText.getText().toString().trim().equals(""))
            return false;
        if(_cpfText.getText().toString().trim().equals(""))
            return false;
        else{
            if (!ValidateCPFHelper.validaCPF(_cpfText.getText().toString()))
                return false;
        }
        if(_emailText.getText().toString().trim().equals(""))
            return false;
        if(_senhaText.getText().toString().trim().equals(""))
            return false;

        return true;
    }

}
