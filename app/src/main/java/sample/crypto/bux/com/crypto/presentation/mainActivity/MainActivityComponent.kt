package sample.crypto.bux.com.crypto.presentation.mainActivity

import dagger.Subcomponent
import dagger.android.AndroidInjector
import sample.crypto.bux.com.crypto.di.scope.PerActivity

@PerActivity
@Subcomponent
interface MainActivityComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()

}