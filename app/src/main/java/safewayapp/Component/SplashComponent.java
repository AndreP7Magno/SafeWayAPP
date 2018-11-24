package safewayapp.Component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import safewayapp.Activity.SplashActivity;
import safewayapp.Module.AppModule;
import safewayapp.Module.RoomModule;
import safewayapp.Repository.AppDatabase;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class})
public interface SplashComponent {

    void inject(SplashActivity activity);

    AppDatabase appDatabase();

    Application application();
}
