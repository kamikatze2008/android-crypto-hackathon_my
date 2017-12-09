package sample.crypto.bux.com.crypto.presentation.availableCryptoCurrencyList

import dagger.Subcomponent
import dagger.android.AndroidInjector
import sample.crypto.bux.com.crypto.di.scope.PerActivity

@PerActivity
@Subcomponent
interface AvailableCryptoCurrencyComponent : AndroidInjector<AvailableCryptoCurrencyListFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<AvailableCryptoCurrencyListFragment>()

}