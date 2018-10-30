package safewayapp.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            //Toast.makeText(LoginActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //Toast.makeText(LoginActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        pedirPermissoes();

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Se você recusar a permissão, você não poderá usar este serviço\n\nPor favor ative as permissões em [Configuração] > [Permissão]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET)
                .setDeniedCloseButtonText("Fechar")
                .setGotoSettingButtonText("Configuração")
                .check();

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
        _loginButton.setEnabled(false);
        _cadastroButton.setEnabled(false);

        if(!validaCampos()){
            _loginButton.setEnabled(true);
            _cadastroButton.setEnabled(true);
            Toast.makeText(getApplicationContext(), "Dados de preenchimento inválidos.", Toast.LENGTH_LONG).show();
            return;
        }

        // Lógica do login de usuário

        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);

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

    private void pedirPermissoes() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, 2);
    }

}
