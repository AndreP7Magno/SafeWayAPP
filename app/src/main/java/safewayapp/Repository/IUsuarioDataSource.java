package safewayapp.Repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import safewayapp.Persistence.Usuario;

public interface IUsuarioDataSource {
    LiveData<List<Usuario>> getAll();

    Usuario getByCPF(String cpf);

    long insert(Usuario usuario);

    int delete(Usuario usuario);

    void update(Usuario usuario);
}
