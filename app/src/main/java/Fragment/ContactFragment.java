package Fragment;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.Activity.NovoContatoActivity;
import safewayapp.Adapter.ContatosAdapter;
import safewayapp.Component.DaggerContatoComponent;
import safewayapp.Module.AppModule;
import safewayapp.Module.RoomModule;
import safewayapp.Persistence.Contato;
import safewayapp.R;
import safewayapp.Repository.IContatoDataSource;


public class ContactFragment extends Fragment {
    private static final int REQUEST_CONTATO= 998;

    @BindView(R.id.listViewContatos)
    RecyclerView mRecycleContatos;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Inject
    public IContatoDataSource contatoDataSource;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Dialog MyDialog;
    private AppCompatButton btnVoltar;
    private AppCompatButton btnSalvar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        ButterKnife.bind(this, rootView);

        DaggerContatoComponent.builder()
                .appModule(new AppModule(getActivity().getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .build()
                .inject(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCustomAlertDialog();
            }
        });

        listaContatos();
    }

    public void MyCustomAlertDialog(){
        MyDialog = new Dialog(getActivity());
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.dialog_novo_contato);
        //MyDialog.setTitle("My Custom Dialog");

        btnVoltar = (AppCompatButton) MyDialog.findViewById(R.id.btnVoltarContato);
        btnSalvar = (AppCompatButton)MyDialog.findViewById(R.id.btnSalvarContato);

        btnVoltar.setEnabled(true);
        btnSalvar.setEnabled(true);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Hello, I'm Custom Alert Dialog", Toast.LENGTH_LONG).show();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTATO && data != null) {
            Bundle params = data.getExtras();
            String resultado = params.getString("Resultado");

            if (resultado.equals("OK")){
                listaContatos();
                Toast.makeText(getContext(), "Contato salvo com sucesso", Toast.LENGTH_SHORT).show();
            }
        }
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
