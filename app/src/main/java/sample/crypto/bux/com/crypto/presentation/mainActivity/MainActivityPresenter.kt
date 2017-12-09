package sample.crypto.bux.com.crypto.presentation.mainActivity

import sample.crypto.bux.com.crypto.di.scope.PerActivity
import javax.inject.Inject

@PerActivity
class MainActivityPresenter @Inject constructor()
    : MainActivityContract.Presenter