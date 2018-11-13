package safewayapp.Module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private static final String PREF_KEY = "PK_SafewayApp";
    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public SharedPreferences providePreferences()
    {
        return ( this.mApplication.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE));
    }
}
