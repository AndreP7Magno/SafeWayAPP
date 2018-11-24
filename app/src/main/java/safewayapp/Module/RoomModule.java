package safewayapp.Module;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import safewayapp.Dao.ContatoDao;
import safewayapp.Dao.UsuarioDao;
import safewayapp.Repository.AppDatabase;
import safewayapp.Repository.ContatoDataSource;
import safewayapp.Repository.IContatoDataSource;
import safewayapp.Repository.IUsuarioDataSource;
import safewayapp.Repository.UsuarioDataSource;

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
    IContatoDataSource contatoDataSource(ContatoDao contatoDao) {
        return new ContatoDataSource(contatoDao);
    }

    @Singleton
    @Provides
    UsuarioDao providesUsuarioDao(AppDatabase appDatabase) {
        return appDatabase.getUsuarioDao();
    }

    @Singleton
    @Provides
    IUsuarioDataSource usuarioDataSource(UsuarioDao usuarioDao) {
        return new UsuarioDataSource(usuarioDao);
    }

}
