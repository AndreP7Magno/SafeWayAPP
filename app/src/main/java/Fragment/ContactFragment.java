package Fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.Activity.NovoContatoActivity;
import safewayapp.Adapter.ContatosAdapter;
import safewayapp.Module.AppModule;
import safewayapp.Module.RoomModule;
import safewayapp.Persistence.Contato;
import safewayapp.R;
import safewayapp.Repository.ContatoDataSource;
import safewayapp.Repository.IContatoDataSource;


public class ContactFragment extends Fragment {
    private static final int REQUEST_FILTRO = 998;

    @BindView(R.id.listViewContatos)
    RecyclerView recycleContatos;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Inject
    public IContatoDataSource contatoDataSource;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), NovoContatoActivity.class);
                startActivityForResult(it, REQUEST_FILTRO);
            }
        });

        recycleContatos.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        recycleContatos.setLayoutManager(mLayoutManager);

        /*DaggerMainComponent.builder()
                .appModule(new AppModule(getActivity().getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .build()
                .inject(this);*/

        /*contatoDataSource.getAll().observe(this, new Observer<List<Contato>>() {
            @Override
            public void onChanged(@Nullable List<Contato> contatos) {
                mAdapter = new ContatosAdapter(contatos, contatoDataSource, ContactFragment.this);
                recycleContatos.setAdapter(mAdapter);
            }
        });*/
    }
}
