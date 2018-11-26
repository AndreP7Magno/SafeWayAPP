package safewayapp.Repository;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

import safewayapp.Dao.ContatoDao;
import safewayapp.Dao.RecordDao;
import safewayapp.Dao.UsuarioDao;
import safewayapp.Persistence.Contato;
import safewayapp.Persistence.Record;
import safewayapp.Persistence.Usuario;

@Database(entities =
        {
                Contato.class,
                Usuario.class,
                Record.class
        }, version = AppDatabase.VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    static final int VERSION = 1;

    public abstract ContatoDao getContatoDao();
    public abstract UsuarioDao getUsuarioDao();
    public abstract RecordDao getRecordDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };
}
