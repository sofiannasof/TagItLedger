package eu.ubitech.smenesidou.tagitledger.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.net.ssl.SSLSession;

import dagger.Module;
import dagger.Provides;
import eu.ubitech.smenesidou.tagitledger.api.ApiService;
import eu.ubitech.smenesidou.tagitledger.data.AppRemoteDataStore;
import eu.ubitech.smenesidou.tagitledger.utils.HostnameVerifier;
import eu.ubitech.smenesidou.tagitledger.utils.SslSocketFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {

    String mBaseUrl;
    String mEvtUrl;

    public DataModule(String mBaseUrl, String mEvtUrl) {
        this.mBaseUrl = mBaseUrl;
        this.mEvtUrl = mEvtUrl;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    @Named("default")
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .sslSocketFactory(SslSocketFactory.createSSLSocketFactory())
                .hostnameVerifier(new HostnameVerifier(){
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                }).build();
    }

    @Provides
    @Singleton
    @Named("nxt")
    Retrofit provideRetrofit(@Named("default")Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("nxt")
    ApiService provideServiceNxt(@Named("nxt") Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    @Named("evt")
    ApiService provideServiceEvt(@Named("evt") Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    @Named("evt")
    Retrofit provideEvtRetrofit(@Named("default") Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(mEvtUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    AppRemoteDataStore providesRepository() {
        return new AppRemoteDataStore();
    }}

