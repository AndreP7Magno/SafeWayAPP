package safewayapp.Repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import safewayapp.Dao.UsuarioDao;
import safewayapp.Persistence.Usuario;

public class UsuarioDataSource implements IUsuarioDataSource {

    private UsuarioDao usuarioDao;

    @Inject
    public UsuarioDataSource(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Override
    public LiveData<List<Usuario>> getAll() {
        return usuarioDao.getAll();
    }

    @Override
    public Usuario getByCPF(String cpf) {
        return usuarioDao.getByCPF(cpf);
    }

    @Override
    public long insert(Usuario usuario) {
        return usuarioDao.insert(usuario);
    }

    @Override
    public int delete(Usuario usuario) {
        return usuarioDao.delete(usuario);
    }

    @Override
    public void update(Usuario usuario) {
        usuarioDao.update(usuario);
    }
}
