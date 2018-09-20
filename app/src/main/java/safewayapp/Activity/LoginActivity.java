package safewayapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.R;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CADASTRO = 0;

    @BindView(R.id.btn_login)
    Button _loginButton;

    @BindView(R.id.link_cadastro)
    TextView _cadastroLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        pedirPermissoes();

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _cadastroLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_CADASTRO);
                finish();
            }
        });
    }

    private void login() {

        //Validações

        Intent main = new Intent(this, MainActivity.class);

        startActivity(main);
    }

    private void pedirPermissoes() {

    }
}
