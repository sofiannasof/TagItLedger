package eu.ubitech.smenesidou.tagitledger.data;

import android.app.Application;

import eu.ubitech.smenesidou.tagitledger.di.AppComponent;
import eu.ubitech.smenesidou.tagitledger.di.AppModule;
import eu.ubitech.smenesidou.tagitledger.di.DaggerAppComponent;
import eu.ubitech.smenesidou.tagitledger.di.DataModule;

public class TagItLedgerApplication extends Application {

    private static AppComponent mAppComponent;
    //TODO: Update ip based on the blockchain ip
    public static final String BASE_URL_NXT = "http://ip:7876";
    public static final String BASE_URL_EVT = "https://api.evrythng.com";

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule(BASE_URL_NXT, BASE_URL_EVT))
                .build();
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }
}
