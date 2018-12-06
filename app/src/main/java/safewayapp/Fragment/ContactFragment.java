package safewayapp.Fragment;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import safewayapp.Adapter.ContatosAdapter;
import safewayapp.Api.ContactApi;
import safewayapp.Api.request.ContactRequest;
import safewayapp.Api.response.ContactResponse;
import safewayapp.Component.DaggerContatoComponent;
import safewayapp.Helper.DialogHelper;
import safewayapp.Helper.ProgressDialogHelper;
import safewayapp.Helper.RecyclerItemTouchHelper;
import safewayapp.Helper.SnackBarHelper;
import safewayapp.Module.AppModule;
import safewayapp.Module.NetModule;
import safewayapp.Module.RoomModule;
import safewayapp.Persistence.Contato;
import safewayapp.Persistence.Usuario;
import safewayapp.R;
import safewayapp.Repository.IContatoDataSource;
import safewayapp.Repository.IUsuarioDataSource;
import safewayapp.ViewHolder.ContatosItemHolder;


public class ContactFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    @BindView(R.id.listViewContatos)
    RecyclerView mRecycleContatos;

    @BindView(R.id.btnNovoContato)
    FloatingActionButton btnNovoContato;

    @BindView(R.id.coordinator_contato)
    CoordinatorLayout coordinatorContato;

    private CoordinatorLayout coordinatorNovoContato;

    @Inject
    IContatoDataSource contatoDataSource;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    ContactApi contactApi;

    @Inject
    public IUsuarioDataSource usuarioDataSource;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Dialog MyDialog;
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

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecycleContatos);

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

    private void initFragment(View view) {
        ButterKnife.bind(this, view);

        DaggerContatoComponent.builder()
                .appModule(new AppModule(getActivity().getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .netModule(new NetModule(getString(R.string.baseURL)))
                .build()
                .inject(this);
    }

    private void initNovoContatoDialog() {
        MyDialog = new Dialog(getActivity());
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.dialog_novo_contato);

        btnSalvar = (AppCompatButton) MyDialog.findViewById(R.id.btnSalvarContato);
        txtNomeContato = (EditText) MyDialog.findViewById(R.id.txtNomeContato);
        txtTelefoneContato = (EditText) MyDialog.findViewById(R.id.txtTelefoneContato);
        coordinatorNovoContato = (CoordinatorLayout) MyDialog.findViewById(R.id.coordinator_novo_contato);

        btnSalvar.setEnabled(true);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaContato();
            }
        });

    }

    private void initMaskTelefone() {
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
    }

    private void salvaContato() {
        final ProgressDialogHelper dialog = new ProgressDialogHelper(getActivity(), "Aguarde", "Registrando Contato...");
        dialog.show();

        final String telefoneSemMascara = txtTelefoneContato.getText().toString()
                .replace("(", "")
                .replace(")", "")
                .replace("-", "")
                .replace(" ", "")
                .trim();

        String cpf = sharedPreferences.getString("CPF", "");
        Usuario usuario = usuarioDataSource.getByCPF(cpf);
        final String user = usuario.getId();

        if (validaCampos(telefoneSemMascara)) {
            contactApi.postContact(new ContactRequest(user, txtNomeContato.getText().toString(), telefoneSemMascara)).enqueue(new Callback<ContactResponse>() {
                @Override
                public void onResponse(Call<ContactResponse> call, Response<ContactResponse> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        ContactResponse data = response.body();
                        salvarContato(data, txtNomeContato.getText().toString(), telefoneSemMascara);
                        //listaContatos();
                        dialog.dismiss();
                        MyDialog.cancel();
                    } else {
                        try {
                            dialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ContactResponse> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "ERRO AO SALVAR", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void salvarContato(ContactResponse data, String nome, String telefone) {
        if (contatoDataSource.getById(data.getId()) == null) {
            contatoDataSource.insert(
                    new Contato(
                            data.getId(),
                            nome,
                            telefone));
        }
    }

    private boolean validaCampos(String telefone) {
        if (txtNomeContato.getText().toString().equals("")) {
            SnackBarHelper.getInstance(coordinatorNovoContato).show("Nome não informado", Snackbar.LENGTH_LONG);
            return false;
        } else if (telefone.equals("")) {
            SnackBarHelper.getInstance(coordinatorNovoContato).show("Telefone não informado", Snackbar.LENGTH_LONG);
            return false;
        } else if (telefone.length() < 10) {
            SnackBarHelper.getInstance(coordinatorNovoContato).show("Telefone inválido", Snackbar.LENGTH_LONG);
            return false;
        }

        return true;
    }

    private void listaContatos() {
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

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ContatosItemHolder) {
            final ProgressDialogHelper d = new ProgressDialogHelper(getActivity(), "Aguarde", "Excluindo Contato...");
            d.show();

            DialogHelper.getInstance().ShowAlert(getActivity(), "Deseja realmente excluir o contato?", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    final Contato deletedItem = ((ContatosAdapter) mAdapter).mDataset.get(viewHolder.getAdapterPosition());

                    contactApi.deleteContact(new ContactRequest(deletedItem.getId())).enqueue(new Callback<ContactResponse>() {
                        @Override
                        public void onResponse(Call<ContactResponse> call, Response<ContactResponse> response) {
                            if (response.code() == HttpURLConnection.HTTP_OK) {
                                ContactResponse data = response.body();

                                ((ContatosAdapter) mAdapter).removeItem(viewHolder.getAdapterPosition());
                                int del = contatoDataSource.delete(deletedItem);
                                SnackBarHelper.getInstance(coordinatorContato).show("Excluído com sucesso!", Snackbar.LENGTH_LONG);
                                d.dismiss();
                                return;
                            } else {
                                try {
                                    d.dismiss();
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ContactResponse> call, Throwable t) {
                            d.dismiss();
                            Toast.makeText(getContext(), "ERRO AO SALVAR", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            mAdapter.notifyDataSetChanged();
        }
    }
}