package sample.crypto.bux.com.crypto.presentation.availableCryptoCurrencyList

import com.bux.crypto.RxCryptoDataManager
import com.bux.crypto.RxCryptoWebSocketConnectionManager
import com.bux.crypto.domain.Market
import com.bux.crypto.internal.websocket.events.CryptoQuoteUpdateEvent
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class AvailableCryptoCurrencyListPresenter @Inject constructor(
        val rxCryptoDataManager: RxCryptoDataManager,
        val rxCryptoWebSocketConnectionManager: RxCryptoWebSocketConnectionManager)
    : AvailableCryptoCurrencyListContract.Presenter() {

    private var marketsArray: Array<Market>? = null

    override fun loadData() {
        safeSubscribe {
            rxCryptoDataManager.marketsObservable
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { view?.showProgressBar() }
                    .doAfterTerminate { view?.hideProgressBar() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                fillDataAndSubscribeForAll(it)
                            },
                            {
                                it.message?.let {
                                    view?.showError(it)
                                }
                            }
                    )
        }
    }

    private fun fillDataAndSubscribeForAll(it: Array<Market>) {
        marketsArray = it
        view?.showOrUpdateMarkets(it)
        it.forEach {
            rxCryptoWebSocketConnectionManager.subscribeForMarkets(it)
        }
    }

    override fun resume() {
        super.resume()
        subscribeForSocketObservable()
    }

    private fun subscribeForSocketObservable() {
        safeSubscribe {
            rxCryptoWebSocketConnectionManager.connectionObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                when (it) {
                                    is CryptoQuoteUpdateEvent -> updateMarket(it)
                                }
                            },
                            {
                                it.message?.let {
                                    view?.showError(it)
                                }
                            }
                    )
        }
    }

    private fun updateMarket(cryptoQuoteUpdateEvent: CryptoQuoteUpdateEvent) {
        with(cryptoQuoteUpdateEvent.cryptoQuote) {
            marketsArray?.let {
                it.filter {
                    it.name == marketName
                }.getOrNull(0)?.let {
                    it.name = marketName
                    it.baseCurrency = baseCurrency
                    it.quoteCurrency = quoteCurrency
                    it.bestAsk = ask
                    it.bestBid = bid
                    it.lastUpdated = timestamp
                }
                view?.showOrUpdateMarkets(it)
            }
        }
    }

    fun unsubscribeForAllMarkets() {
        marketsArray?.forEach { rxCryptoWebSocketConnectionManager.unsubscribeFromMarkets(it) }
        rxCryptoWebSocketConnectionManager.disconnect()
    }

    override fun destroy() {
        unsubscribeForAllMarkets()
        super.destroy()
    }
}