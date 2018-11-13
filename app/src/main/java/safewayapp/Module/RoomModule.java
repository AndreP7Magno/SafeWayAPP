package safewayapp.Module;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import safewayapp.Dao.ContatoDao;
import safewayapp.Repository.AppDatabase;
import safewayapp.Repository.ContatoDataSource;
import safewayapp.Repository.IContatoDataSource;

@Module
public class RoomModule {
    private AppDatabase appDatabase;

    public RoomModule(Application mApplication) {
        appDatabase =
                Room
                        .databaseBuilder(mApplication, AppDatabase.class, "db-safewayapp")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
    }

    @Singleton
    @Provides
    AppDatabase providesRoomDatabase() {
        return appDatabase;
    }

    @Singleton
    @Provides
    ContatoDao getContatoDao(AppDatabase appDatabase) {
        return appDatabase.getContatoDao();
    }

    @Singleton
    @Provides
    IContatoDataSource usuarioRepository(ContatoDao contatoDao) {
        return new ContatoDataSource(contatoDao);
    }

}
