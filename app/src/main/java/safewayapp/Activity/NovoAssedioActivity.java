package safewayapp.Activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.Helper.MaterialCheckBox;
import safewayapp.Helper.SnackBarHelper;
import safewayapp.R;

public class NovoAssedioActivity extends AppCompatActivity {
    private static final int REQUEST_NOVO_ASSEDIO = 998;

    @BindView(R.id.coordinator_novo_assedio)
    CoordinatorLayout coordinatorNovoAssedio;

    @BindView(R.id.txtEndereco)
    EditText txtEndereco;

    @BindView(R.id.txtDescricao)
    EditText txtDescricao;

    @BindView(R.id.cbGrave)
    MaterialCheckBox cbGrave;

    @BindView(R.id.txtGrave)
    TextView txtGrave;

    @BindView(R.id.cbMedio)
    MaterialCheckBox cbMedio;

    @BindView(R.id.txtMedio)
    TextView txtMedio;

    @BindView(R.id.cbBaixa)
    MaterialCheckBox cbBaixa;

    @BindView(R.id.txtBaixa)
    TextView txtBaixa;

    @BindView(R.id.btnSalvarRegistroAssedio)
    Button btnSalvarRegistroAssedio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_assedio);

        ButterKnife.bind(this);

        txtGrave.setOnClickListener(onGraveListener);
        txtMedio.setOnClickListener(onMedioListener);
        txtBaixa.setOnClickListener(onBaixaListener);

        btnSalvarRegistroAssedio.setOnClickListener(onSalvarRegistroAssedioClickListener);
    }

    private void CheckCombos(String tipoCheck) {
        if (tipoCheck == "grave"){
            if (cbGrave.isChecked())
                cbGrave.setChecked(false);
            else
                cbGrave.setChecked(true);
        }
        else if (tipoCheck == "medio"){
            if (cbMedio.isChecked())
                cbMedio.setChecked(false);
            else
                cbMedio.setChecked(true);
        }
        else if (tipoCheck == "baixa"){
            if (cbBaixa.isChecked())
                cbBaixa.setChecked(false);
            else
                cbBaixa.setChecked(true);
        }
    }

    private View.OnClickListener onSalvarRegistroAssedioClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validaCampos()){
                getIntent().putExtra("Endereco", txtEndereco.getText().toString());
                getIntent().putExtra("Descricao", txtDescricao.getText().toString());
                getIntent().putExtra("cbGrave", cbGrave.isChecked());
                getIntent().putExtra("cbMedio", cbMedio.isChecked());
                getIntent().putExtra("cbBaixa", cbBaixa.isChecked());

                setResult(REQUEST_NOVO_ASSEDIO, getIntent());
                finish();
            }
        }
    };

    private boolean validaCampos(){
        if(txtEndereco.getText().toString().trim().isEmpty()){
            SnackBarHelper.getInstance(coordinatorNovoAssedio).show("Endereço é obrigatório", Snackbar.LENGTH_LONG);
            return false;
        }
        else if (txtDescricao.getText().toString().trim().isEmpty()){
            SnackBarHelper.getInstance(coordinatorNovoAssedio).show("Descrição é obrigatório", Snackbar.LENGTH_LONG);
            return false;
        }
        else if (!cbGrave.isChecked() && !cbMedio.isChecked() && !cbBaixa.isChecked()){
            SnackBarHelper.getInstance(coordinatorNovoAssedio).show("Severidade é obrigatório", Snackbar.LENGTH_LONG);
            return false;
        }
        else if (
                (cbGrave.isChecked() && cbMedio.isChecked()) || (cbGrave.isChecked() && cbBaixa.isChecked()) ||
                        ((cbBaixa.isChecked() && cbMedio.isChecked()))){
            SnackBarHelper.getInstance(coordinatorNovoAssedio).show("Informe somente uma severidade", Snackbar.LENGTH_LONG);
            return false;
        }

        return true;

    }

    View.OnClickListener onGraveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckCombos("grave");
        }
    };

    View.OnClickListener onMedioListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckCombos("medio");
        }
    };

    View.OnClickListener onBaixaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckCombos("baixa");
        }
    };
}
