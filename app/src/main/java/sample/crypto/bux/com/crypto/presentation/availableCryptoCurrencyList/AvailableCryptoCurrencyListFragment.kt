package sample.crypto.bux.com.crypto.presentation.availableCryptoCurrencyList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bux.crypto.domain.Market
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_available_crypto_currency_list.*
import sample.crypto.bux.com.crypto.R
import sample.crypto.bux.com.crypto.presentation.mainActivity.MainActivity
import sample.crypto.bux.com.crypto.presentation.transaction.TransactionFragment
import sample.crypto.bux.com.crypto.utils.showShortToast

import javax.inject.Inject

class AvailableCryptoCurrencyListFragment : DaggerFragment(), AvailableCryptoCurrencyListContract.View {

    companion object {
        val TAG: String = AvailableCryptoCurrencyListFragment::class.java.simpleName

        fun newInstance() = AvailableCryptoCurrencyListFragment()
    }

    @Inject lateinit var presenter: AvailableCryptoCurrencyListPresenter

    @Inject lateinit var availableCryptoCurrencyListAdapter: AvailableCryptoCurrencyListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater?.inflate(R.layout.fragment_available_crypto_currency_list, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.view = this
        presenter.loadData()
        availableCryptoCurrencyListAdapter.listener = { onItemClick(it) }
        rv_list.adapter = availableCryptoCurrencyListAdapter
    }

    private fun onItemClick(market: Market) {
        if (activity is MainActivity) {
            (activity as MainActivity).replaceFragment(TransactionFragment.newInstance(market), TransactionFragment.TAG)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onPause() {
        super.onPause()
        presenter.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun showProgressBar() {
        progressbar.show()
    }

    override fun hideProgressBar() {
        progressbar.hide()
    }

    override fun showError(errorMessage: String) {
        showShortToast(errorMessage)
    }

    override fun showOrUpdateMarkets(marketsArray: Array<Market>) {
        with(availableCryptoCurrencyListAdapter) {
            items.clear()
            items.addAll(marketsArray.toMutableList())
            notifyDataSetChanged()
        }
    }
}