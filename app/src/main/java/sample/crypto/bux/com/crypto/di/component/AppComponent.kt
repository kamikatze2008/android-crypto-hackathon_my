package sample.crypto.bux.com.crypto.di.component

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import sample.crypto.bux.com.crypto.di.modules.AppModule
import sample.crypto.bux.com.crypto.di.modules.SourceModule
import sample.crypto.bux.com.crypto.presentation.MainApplication
import sample.crypto.bux.com.crypto.presentation.availableCryptoCurrencyList.AvailableCryptoCurrencyModule
import sample.crypto.bux.com.crypto.presentation.mainActivity.MainActivityModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    SourceModule::class,
    MainActivityModule::class,
    AvailableCryptoCurrencyModule::class,
    AndroidSupportInjectionModule::class
])
interface AppComponent : AndroidInjector<MainApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MainApplication>()
}
