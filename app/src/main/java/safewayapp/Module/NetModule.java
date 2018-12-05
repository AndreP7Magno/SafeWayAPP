package safewayapp.Module;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import safewayapp.Api.AuthenticateApi;
import safewayapp.Api.ContactApi;
import safewayapp.Api.EmergencyCallApi;
import safewayapp.Api.RecordApi;

@Module
public class NetModule {
    String mBaseUrl;

    public NetModule(String mBaseUrl) {
        this.mBaseUrl = mBaseUrl;
    }

    @Provides
    @Singleton
    Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(okhttp3.Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.readTimeout(60, TimeUnit.SECONDS);
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    AuthenticateApi getAuthenticateApi(Retrofit retroFit) {
        return retroFit.create(AuthenticateApi.class);
    }

    @Provides
    @Singleton
    RecordApi getRecordApi(Retrofit retroFit) {
        return retroFit.create(RecordApi.class);
    }

    @Provides
    @Singleton
    EmergencyCallApi getEmergencyCallApi(Retrofit retroFit) {
        return retroFit.create(EmergencyCallApi.class);
    }

    @Provides
    @Singleton
    ContactApi getContactApi(Retrofit retroFit) {
        return retroFit.create(ContactApi.class);
    }

}
