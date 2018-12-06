package safewayapp.Component;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import safewayapp.Dao.ContatoDao;
import safewayapp.Dao.UsuarioDao;
import safewayapp.Fragment.ContactFragment;
import safewayapp.Module.AppModule;
import safewayapp.Module.NetModule;
import safewayapp.Module.RoomModule;
import safewayapp.Repository.AppDatabase;
import safewayapp.Repository.IContatoDataSource;
import safewayapp.Repository.IUsuarioDataSource;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class, NetModule.class})
public interface ContatoComponent {
    void inject(ContactFragment fragment);

    ContatoDao contatoDao();

    IContatoDataSource contatoDataSource();

    UsuarioDao usuarioDao();

    IUsuarioDataSource usuarioDataSource();

    AppDatabase appDataBase();

    Application application();

    SharedPreferences sharedPreferences();
}
