package sample.crypto.bux.com.crypto.presentation.availableCryptoCurrencyList

import com.bux.crypto.domain.Market
import sample.crypto.bux.com.crypto.presentation.base.BasePresenter
import sample.crypto.bux.com.crypto.presentation.base.BaseView

interface AvailableCryptoCurrencyListContract {
    interface View : BaseView {
        fun showProgressBar()
        fun hideProgressBar()
        fun showError(errorMessage: String)
        fun showOrUpdateMarkets(marketsArray: Array<Market>)

    }

    abstract class Presenter : BasePresenter<View>() {

        abstract fun loadData()
    }
}