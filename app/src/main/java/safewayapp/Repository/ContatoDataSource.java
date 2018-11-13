package safewayapp.Repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import safewayapp.Dao.ContatoDao;
import safewayapp.Persistence.Contato;

public class ContatoDataSource implements IContatoDataSource {

    private ContatoDao contatoDao;

    @Inject
    public ContatoDataSource(ContatoDao contatoDao) {
        this.contatoDao = contatoDao;
    }

    @Override
    public LiveData<List<Contato>> getAll() {
        return contatoDao.getAll();
    }

    @Override
    public Contato getByNome(String nome) {
        return contatoDao.getByNome(nome);
    }

    @Override
    public long insert(Contato contato) {
        return contatoDao.insert(contato);
    }

    @Override
    public int delete(Contato contato) {
        return contatoDao.delete(contato);
    }

    @Override
    public void update(Contato contato) {
        contatoDao.update(contato);
    }
}
