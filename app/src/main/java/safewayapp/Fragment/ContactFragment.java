package safewayapp.Fragment;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionButton;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.Adapter.ContatosAdapter;
import safewayapp.Component.DaggerContatoComponent;
import safewayapp.Helper.SnackBarHelper;
import safewayapp.Module.AppModule;
import safewayapp.Module.NetModule;
import safewayapp.Module.RoomModule;
import safewayapp.Persistence.Contato;
import safewayapp.R;
import safewayapp.Repository.IContatoDataSource;


public class ContactFragment extends Fragment {
    @BindView(R.id.listViewContatos)
    RecyclerView mRecycleContatos;

    @BindView(R.id.btnNovoContato)
    FloatingActionButton btnNovoContato;

    @BindView(R.id.coordinator_contato)
    CoordinatorLayout coordinatorContato;

    private CoordinatorLayout coordinatorNovoContato;

    @Inject
    public IContatoDataSource contatoDataSource;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Dialog MyDialog;
    private AppCompatButton btnVoltar;
    private AppCompatButton btnSalvar;
    private EditText txtNomeContato;
    private EditText txtTelefoneContato;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        initFragment(view);

        initNovoContatoDialog();

        initMaskTelefone();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnNovoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNomeContato.setText("");
                txtTelefoneContato.setText("");
                MyDialog.show();
            }
        });

        listaContatos();
    }

    private void initFragment(View view){
        ButterKnife.bind(this, view);

        DaggerContatoComponent.builder()
                .appModule(new AppModule(getActivity().getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .netModule(new NetModule(getString(R.string.baseURL)))
                .build()
                .inject(this);
    }

    private void initNovoContatoDialog(){
        MyDialog = new Dialog(getActivity());
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.dialog_novo_contato);

        btnVoltar = (AppCompatButton) MyDialog.findViewById(R.id.btnVoltarContato);
        btnSalvar = (AppCompatButton)MyDialog.findViewById(R.id.btnSalvarContato);
        txtNomeContato = (EditText) MyDialog.findViewById(R.id.txtNomeContato);
        txtTelefoneContato = (EditText) MyDialog.findViewById(R.id.txtTelefoneContato);
        coordinatorNovoContato = (CoordinatorLayout) MyDialog.findViewById(R.id.coordinator_novo_contato);

        btnVoltar.setEnabled(true);
        btnSalvar.setEnabled(true);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaContato();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNomeContato.setText("");
                txtTelefoneContato.setText("");
                MyDialog.cancel();
            }
        });
    }

    private void initMaskTelefone(){
        final MaskedTextChangedListener listener = MaskedTextChangedListener.Companion.installOn(
                txtTelefoneContato,
                "([00]) [00000]-[0000]",
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                        Log.d("TAG", extractedValue);
                        Log.d("TAG", String.valueOf(maskFilled));
                    }
                }
        );

        txtTelefoneContato.setHint(listener.placeholder());
    }

    private void salvaContato(){
        String telefoneSemMascara = txtTelefoneContato.getText().toString()
                .replace("(", "")
                .replace(")", "")
                .replace("-", "")
                .replace(" ", "")
                .trim();

        if(validaCampos(telefoneSemMascara)){
            long codigo = contatoDataSource.insert(new Contato(txtNomeContato.getText().toString(), telefoneSemMascara));

            if (codigo != 0){
                listaContatos();
                MyDialog.cancel();
                SnackBarHelper.getInstance(coordinatorContato).show("Contato salvo com sucesso", Snackbar.LENGTH_LONG);
            }
            else{
                SnackBarHelper.getInstance(coordinatorContato).show("Erro ao salvar o contato", Snackbar.LENGTH_LONG);
            }
        }
    }

    private boolean validaCampos(String telefone){
        if (txtNomeContato.getText().toString().equals("")){
            SnackBarHelper.getInstance(coordinatorNovoContato).show("Nome não informado", Snackbar.LENGTH_LONG);
            return false;
        }
        else if (telefone.equals("")) {
            SnackBarHelper.getInstance(coordinatorNovoContato).show("Telefone não informado", Snackbar.LENGTH_LONG);
            return false;
        }
        else if (telefone.length() < 10){
            SnackBarHelper.getInstance(coordinatorNovoContato).show("Telefone inválido", Snackbar.LENGTH_LONG);
            return false;
        }

        return true;
    }

    private void listaContatos(){
        mRecycleContatos.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecycleContatos.setLayoutManager(mLayoutManager);

        contatoDataSource.getAll().observe(this, new Observer<List<Contato>>() {
            @Override
            public void onChanged(@Nullable List<Contato> contatos) {
                mAdapter = new ContatosAdapter(contatos, contatoDataSource, ContactFragment.this);
                mRecycleContatos.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
