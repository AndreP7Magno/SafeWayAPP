package safewayapp.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.redmadrobot.inputmask.MaskedTextChangedListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.Component.DaggerContatoComponent;
import safewayapp.Module.AppModule;
import safewayapp.Module.RoomModule;
import safewayapp.Persistence.Contato;
import safewayapp.R;
import safewayapp.Repository.IContatoDataSource;

public class NovoContatoActivity extends AppCompatActivity {

    private static final int REQUEST_CONTATO = 998;

    @BindView(R.id.txtNomeContato)
    EditText txtNomeContato;

    @BindView(R.id.txtTelefoneContato)
    EditText txtTelefoneContato;

    @BindView(R.id.btnSalvarContato)
    Button btnSalvarContato;

    @Inject
    public IContatoDataSource contatoDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_contato);

        ButterKnife.bind(this);

        DaggerContatoComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .inject(this);

        final MaskedTextChangedListener listener = MaskedTextChangedListener.Companion.installOn(
                txtTelefoneContato,
                "([000]) [00000]-[0000]",
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                        Log.d("TAG", extractedValue);
                        Log.d("TAG", String.valueOf(maskFilled));
                    }
                }
        );

        txtTelefoneContato.setHint(listener.placeholder());

        btnSalvarContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaContato();
            }
        });
    }

    private void salvaContato(){
        if(validaCampos()){

            String telefoneSemMascara = txtTelefoneContato.getText().toString()
                    .replace("(", "")
                    .replace(")", "")
                    .replace("-", "")
                    .replace(" ", "")
                    .trim();
            long codigo = contatoDataSource.insert(new Contato(txtNomeContato.getText().toString(), telefoneSemMascara));

            if (codigo != 0){
                getIntent().putExtra("Resultado", "OK");

                setResult(REQUEST_CONTATO, getIntent());
                finish();
            }
            else{
                getIntent().putExtra("Resultado", "ERRO");
                Toast.makeText(getApplicationContext(), "Erro ao salvar o contato", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validaCampos(){
        if (txtNomeContato.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Nome não informado", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (txtTelefoneContato.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Telefone não informado", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
