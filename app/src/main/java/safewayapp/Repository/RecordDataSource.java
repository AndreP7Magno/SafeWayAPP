package safewayapp.Repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import safewayapp.Dao.RecordDao;
import safewayapp.Persistence.Record;

public class RecordDataSource implements IRecordDataSource {

    private RecordDao recordDao;

    @Inject
    public RecordDataSource(RecordDao recordDao) {
        this.recordDao = recordDao;
    }

    @Override
    public LiveData<List<Record>> getAll() {
        return recordDao.getAll();
    }

    @Override
    public long insert(Record record) {
        return recordDao.insert(record);
    }

    @Override
    public int delete(Record record) {
        return recordDao.delete(record);
    }

    @Override
    public void update(Record record) {
        recordDao.update(record);
    }
}
