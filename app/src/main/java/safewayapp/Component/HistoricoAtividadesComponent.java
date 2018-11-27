package safewayapp.Component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import safewayapp.Activity.HistoricoAtividades;
import safewayapp.Dao.RecordDao;
import safewayapp.Module.AppModule;
import safewayapp.Module.NetModule;
import safewayapp.Module.RoomModule;
import safewayapp.Repository.AppDatabase;
import safewayapp.Repository.IRecordDataSource;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class, NetModule.class})
public interface HistoricoAtividadesComponent {
    void inject(HistoricoAtividades historicoAtividades);

    RecordDao recordDao();

    IRecordDataSource recordDataSource();

    AppDatabase appDataBase();

    Application application();
}
