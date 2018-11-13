package safewayapp.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import safewayapp.Persistence.Contato;

@Dao
public interface ContatoDao {
    @Query("SELECT * FROM contato")
    LiveData<List<Contato>> getAll();

    @Query("SELECT * FROM contato WHERE Nome = :nome")
    Contato getByNome(String nome);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Contato contato);

    @Update
    void update(Contato contato);

    @Delete
    int delete(Contato contato);
}
