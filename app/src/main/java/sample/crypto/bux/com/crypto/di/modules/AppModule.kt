package sample.crypto.bux.com.crypto.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import sample.crypto.bux.com.crypto.presentation.MainApplication
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun context(app: MainApplication): Context {
        return app.applicationContext
    }
}
