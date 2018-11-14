package safewayapp.Component;

import android.app.Application;

import javax.inject.Singleton;

import Fragment.ContactFragment;
import dagger.Component;
import safewayapp.Activity.NovoContatoActivity;
import safewayapp.Dao.ContatoDao;
import safewayapp.Module.AppModule;
import safewayapp.Module.RoomModule;
import safewayapp.Repository.AppDatabase;
import safewayapp.Repository.IContatoDataSource;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class})
public interface ContatoComponent {
    void inject(ContactFragment fragment);

    void inject(NovoContatoActivity activity);

    ContatoDao contatoDao();

    AppDatabase appDatabase();

    IContatoDataSource contatoDataSource();


    Application application();
}
