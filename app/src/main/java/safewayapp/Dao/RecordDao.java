package safewayapp.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import safewayapp.Persistence.Record;

@Dao
public interface RecordDao {
    @Query("SELECT * FROM record")
    LiveData<List<Record>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Record record);

    @Update
    void update(Record record);

    @Delete
    int delete(Record record);

    @Query("DELETE FROM record")
    void deleteAll();
}
