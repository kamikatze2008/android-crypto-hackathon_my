package sample.crypto.bux.com.crypto.presentation.transaction

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bux.crypto.domain.Market
import sample.crypto.bux.com.crypto.R

class TransactionFragment : Fragment() {
    companion object {
        val TAG: String = TransactionFragment::class.java.simpleName
        val BASE_CURRENCY_KEY = "base_currency_key"
        val QUOTE_CURRENCY_KEY = "quote_currency_key"
        val BEST_ASK_KEY = "best_ask_key"
        val BEST_BID_KEY = "best_bid_key"

        fun newInstance(market: Market): TransactionFragment {
            val bundle = Bundle().apply {
                putString(BASE_CURRENCY_KEY, market.baseCurrency)
                putString(QUOTE_CURRENCY_KEY, market.quoteCurrency)
                putString(BEST_ASK_KEY, market.bestAsk.toPlainString())
                putString(BEST_BID_KEY, market.bestBid.toPlainString())
            }
            return TransactionFragment().apply { arguments = bundle }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_transaction, container, false)
    }
}