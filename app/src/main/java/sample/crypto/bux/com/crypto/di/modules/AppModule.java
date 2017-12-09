package sample.crypto.bux.com.crypto.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sample.crypto.bux.com.crypto.presentation.MainApplication;

@Module
abstract public class AppModule {

    @Provides
    @Singleton
    public static Context context(final MainApplication app) {
        return app.getApplicationContext();
    }
}
