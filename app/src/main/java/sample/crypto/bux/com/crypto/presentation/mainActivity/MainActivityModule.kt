package sample.crypto.bux.com.crypto.presentation.mainActivity

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import sample.crypto.bux.com.crypto.di.scope.PerActivity

@Module(subcomponents = [MainActivityComponent::class])
abstract class MainActivityModule {
    @Binds
    @PerActivity
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun provideSplashPresenter(presenter: MainActivityPresenter): MainActivityPresenter

    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bind(builder: MainActivityComponent.Builder): AndroidInjector.Factory<out Activity>
}