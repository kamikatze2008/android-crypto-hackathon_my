package sample.crypto.bux.com.crypto.presentation.mainActivity

import android.os.Bundle
import android.support.v4.app.Fragment
import dagger.android.support.DaggerAppCompatActivity
import sample.crypto.bux.com.crypto.R
import sample.crypto.bux.com.crypto.presentation.availableCryptoCurrencyList.AvailableCryptoCurrencyListFragment
import javax.inject.Inject

/**
 * Use [.cryptoDataManager] and [.cryptoWebSocketConnectionManager] to work with data.
 */
class MainActivity : DaggerAppCompatActivity(), MainActivityContract.View {

    @Inject lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(AvailableCryptoCurrencyListFragment.newInstance(), AvailableCryptoCurrencyListFragment.TAG)
    }

    fun replaceFragment(fragment: Fragment, tag: String) = supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, fragment, tag)
            .addToBackStack(tag)
            .commit()


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }
}
