package safewayapp.Activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.Helper.MaterialCheckBox;
import safewayapp.Helper.SnackBarHelper;
import safewayapp.R;

public class NovoAssedioActivity extends AppCompatActivity {
    private static final int REQUEST_NOVO_ASSEDIO = 998;

    @BindView(R.id.coordinator_novo_assedio)
    CoordinatorLayout coordinatorNovoAssedio;

    @BindView(R.id.edtDataOcorrencia)
    EditText edtDataOcorrencia;

    @BindView(R.id.edtHoraOcorrencia)
    EditText edtHoraOcorrencia;

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String endereco = extras.getString("endereco");
            String descricao = extras.getString("descricao");
            String dataAssedio = extras.getString("data");
            Boolean grave = extras.getBoolean("cbGrave");
            Boolean medio = extras.getBoolean("cbMedio");
            Boolean baixa = extras.getBoolean("cbBaixa");

            inicializaDatas();

            txtEndereco.setText(endereco);
            txtDescricao.setText(descricao);
            try {
                Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dataAssedio);
                String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(data);
                String formattedHour = new SimpleDateFormat("HH:mm").format(data);

                edtDataOcorrencia.setOnFocusChangeListener(null);
                edtDataOcorrencia.setText(formattedDate);
                edtDataOcorrencia.addTextChangedListener(DataOcorrenciaListener);

                edtHoraOcorrencia.setText(formattedHour);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cbGrave.setChecked(grave);
            cbMedio.setChecked(medio);
            cbBaixa.setChecked(baixa);
            Toast.makeText(getApplicationContext(), "Ops, não foi possível cadastrar o registro. Por favor, tente novamente!", Toast.LENGTH_SHORT).show();
        }
        else
            inicializaDatas();

        txtGrave.setOnClickListener(onGraveListener);
        txtMedio.setOnClickListener(onMedioListener);
        txtBaixa.setOnClickListener(onBaixaListener);

        btnSalvarRegistroAssedio.setOnClickListener(onSalvarRegistroAssedioClickListener);
    }

    private void inicializaDatas() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String firstDay = sdf.format(today);

        DateFormat sdfHora = new SimpleDateFormat("HH:mm");
        String firstHour = sdfHora.format(today);

        edtDataOcorrencia.setOnFocusChangeListener(null);
        edtDataOcorrencia.setText(firstDay);
        edtDataOcorrencia.addTextChangedListener(DataOcorrenciaListener);

        edtHoraOcorrencia.setText(firstHour);
    }

    private void CheckCombos(String tipoCheck) {
        if (tipoCheck == "grave") {
            if (cbGrave.isChecked())
                cbGrave.setChecked(false);
            else
                cbGrave.setChecked(true);
        } else if (tipoCheck == "medio") {
            if (cbMedio.isChecked())
                cbMedio.setChecked(false);
            else
                cbMedio.setChecked(true);
        } else if (tipoCheck == "baixa") {
            if (cbBaixa.isChecked())
                cbBaixa.setChecked(false);
            else
                cbBaixa.setChecked(true);
        }
    }

    private View.OnClickListener onSalvarRegistroAssedioClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validaCampos()) {
                String enderecoSemMascara = txtEndereco.getText().toString()
                        .replace("-", "")
                        .replace(",", "");

                getIntent().putExtra("Endereco", enderecoSemMascara);
                getIntent().putExtra("Descricao", txtDescricao.getText().toString());
                getIntent().putExtra("cbGrave", cbGrave.isChecked());
                getIntent().putExtra("cbMedio", cbMedio.isChecked());
                getIntent().putExtra("cbBaixa", cbBaixa.isChecked());

                Date date = null;
                try {
                    String finalDate = edtDataOcorrencia.getText().toString() + " " + edtHoraOcorrencia.getText().toString();
                    date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(finalDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);

                getIntent().putExtra("data", formattedDate);

                setResult(REQUEST_NOVO_ASSEDIO, getIntent());
                finish();
            }
        }
    };

    private boolean validaCampos() {
        if (txtEndereco.getText().toString().trim().isEmpty()) {
            SnackBarHelper.getInstance(coordinatorNovoAssedio).show("Endereço é obrigatório", Snackbar.LENGTH_LONG);
            return false;
        } else if (txtDescricao.getText().toString().trim().isEmpty()) {
            SnackBarHelper.getInstance(coordinatorNovoAssedio).show("Descrição é obrigatório", Snackbar.LENGTH_LONG);
            return false;
        } else if (!cbGrave.isChecked() && !cbMedio.isChecked() && !cbBaixa.isChecked()) {
            SnackBarHelper.getInstance(coordinatorNovoAssedio).show("Severidade é obrigatório", Snackbar.LENGTH_LONG);
            return false;
        } else if (
                (cbGrave.isChecked() && cbMedio.isChecked()) || (cbGrave.isChecked() && cbBaixa.isChecked()) ||
                        ((cbBaixa.isChecked() && cbMedio.isChecked()))) {
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

    TextWatcher DataOcorrenciaListener = new TextWatcher() {
        private String current = "";
        private String ddmmyyyy = "ddmmyyyy";
        private Calendar cal = Calendar.getInstance();

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8) {
                    clean = clean + ddmmyyyy.substring(clean.length());
                } else {
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day = Integer.parseInt(clean.substring(0, 2));
                    int mon = Integer.parseInt(clean.substring(2, 4));
                    int year = Integer.parseInt(clean.substring(4, 8));

                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                    cal.set(Calendar.MONTH, mon - 1);
                    year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                    clean = String.format("%02d%02d%02d", day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                edtDataOcorrencia.setText(current);
                edtDataOcorrencia.setSelection(sel < current.length() ? sel : current.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
