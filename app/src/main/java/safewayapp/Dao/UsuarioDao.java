package safewayapp.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import safewayapp.Persistence.Usuario;

@Dao
public interface UsuarioDao {

    @Query("SELECT * FROM usuario")
    LiveData<List<Usuario>> getAll();

    @Query("SELECT * FROM usuario WHERE CPF = :cpf")
    Usuario getByCPF(String cpf);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Usuario usuario);

    @Update
    void update(Usuario usuario);

    @Delete
    int delete(Usuario usuario);
}
