package safewayapp.Repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import safewayapp.Persistence.Contato;

public interface IContatoDataSource {
    LiveData<List<Contato>> getAll();

    Contato getByNome(String nome);

    Contato getById(String id);

    long insert(Contato usuario);

    int delete(Contato usuario);

    void update(Contato usuario);

    void deleteAll();

}
