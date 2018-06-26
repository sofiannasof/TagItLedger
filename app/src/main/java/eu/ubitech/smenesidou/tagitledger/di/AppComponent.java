package eu.ubitech.smenesidou.tagitledger.di;

import javax.inject.Singleton;

import dagger.Component;
import eu.ubitech.smenesidou.tagitledger.MainActivity;
import eu.ubitech.smenesidou.tagitledger.data.AppRemoteDataStore;

@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(AppRemoteDataStore appRemoteDataStore);
}
