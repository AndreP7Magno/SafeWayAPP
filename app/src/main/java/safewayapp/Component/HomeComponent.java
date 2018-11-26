package safewayapp.Component;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import safewayapp.Dao.ContatoDao;
import safewayapp.Dao.RecordDao;
import safewayapp.Fragment.HomeFragment;
import safewayapp.Module.AppModule;
import safewayapp.Module.NetModule;
import safewayapp.Module.RoomModule;
import safewayapp.Repository.AppDatabase;
import safewayapp.Repository.IContatoDataSource;
import safewayapp.Repository.IRecordDataSource;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class, NetModule.class})
public interface HomeComponent {
    void inject(HomeFragment homeFragment);

    RecordDao recordDao();

    IRecordDataSource recordDataSource();

    ContatoDao contatoDao();

    IContatoDataSource contatoDataSource();

    AppDatabase appDataBase();

    Application application();

    SharedPreferences sharedPreferences();
}
