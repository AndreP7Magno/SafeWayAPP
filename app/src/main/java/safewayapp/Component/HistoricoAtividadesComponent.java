package safewayapp.Component;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import safewayapp.Activity.HistoricoAtividadesActivity;
import safewayapp.Dao.UsuarioDao;
import safewayapp.Module.AppModule;
import safewayapp.Module.NetModule;
import safewayapp.Module.RoomModule;
import safewayapp.Repository.AppDatabase;
import safewayapp.Repository.IUsuarioDataSource;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class, NetModule.class})
public interface HistoricoAtividadesComponent {
    void inject(HistoricoAtividadesActivity historicoAtividadesActivity);

    UsuarioDao usuarioDao();

    IUsuarioDataSource usuarioDataSource();


    AppDatabase appDataBase();

    Application application();

    SharedPreferences sharedPreferences();
}
