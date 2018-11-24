package safewayapp.Component;

import android.app.Application;

import javax.inject.Singleton;

import Fragment.ContactFragment;
import Fragment.HomeFragment;
import dagger.Component;
import safewayapp.Dao.ContatoDao;
import safewayapp.Module.AppModule;
import safewayapp.Module.RoomModule;
import safewayapp.Repository.AppDatabase;
import safewayapp.Repository.IContatoDataSource;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class})
public interface ContatoComponent {
    void inject(ContactFragment fragment);

    void inject(HomeFragment homeFragment);

    ContatoDao contatoDao();

    AppDatabase appDatabase();

    IContatoDataSource contatoDataSource();


    Application application();
}
