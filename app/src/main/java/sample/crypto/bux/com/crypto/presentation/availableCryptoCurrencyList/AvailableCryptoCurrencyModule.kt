package sample.crypto.bux.com.crypto.presentation.availableCryptoCurrencyList

import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import sample.crypto.bux.com.crypto.di.scope.PerActivity


@Module(subcomponents = [AvailableCryptoCurrencyComponent::class])
abstract class AvailableCryptoCurrencyModule {

    @Binds
    @PerActivity
    @IntoMap
    @FragmentKey(AvailableCryptoCurrencyListFragment::class)
    internal abstract fun provideAvailableCryptoCurrencyAdapter(adapter: AvailableCryptoCurrencyListAdapter): AvailableCryptoCurrencyListAdapter

    @Binds
    @PerActivity
    @IntoMap
    @FragmentKey(AvailableCryptoCurrencyListFragment::class)
    internal abstract fun provideAvailableCryptoCurrencyPresenter(presenter: AvailableCryptoCurrencyListPresenter): AvailableCryptoCurrencyListPresenter

    @Binds
    @IntoMap
    @FragmentKey(AvailableCryptoCurrencyListFragment::class)
    internal abstract fun bind(builder: AvailableCryptoCurrencyComponent.Builder): AndroidInjector.Factory<out Fragment>
}