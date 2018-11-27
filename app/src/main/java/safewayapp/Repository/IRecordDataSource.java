package safewayapp.Repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import safewayapp.Persistence.Record;


public interface IRecordDataSource {
    LiveData<List<Record>> getAll();

    long insert(Record record);

    int delete(Record record);

    void update(Record record);

    void deleteAll();
}
