package safewayapp.Component;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import safewayapp.Activity.LoginActivity;
import safewayapp.Activity.SignUpActivity;
import safewayapp.Fragment.ProfileFragment;
import safewayapp.Module.AppModule;
import safewayapp.Module.NetModule;
import safewayapp.Module.RoomModule;
import safewayapp.Repository.AppDatabase;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class, NetModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
    void inject(SignUpActivity activity);
    void inject(ProfileFragment fragment);

    AppDatabase appDataBase();

    Application application();

    SharedPreferences sharedPreferences();

}
