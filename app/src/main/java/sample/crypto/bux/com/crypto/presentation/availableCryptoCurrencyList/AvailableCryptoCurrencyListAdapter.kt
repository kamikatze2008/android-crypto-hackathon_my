package sample.crypto.bux.com.crypto.presentation.availableCryptoCurrencyList

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bux.crypto.domain.Market
import kotlinx.android.synthetic.main.item_market_item.view.*
import sample.crypto.bux.com.crypto.R
import sample.crypto.bux.com.crypto.di.scope.PerActivity
import javax.inject.Inject

@PerActivity
class AvailableCryptoCurrencyListAdapter @Inject constructor() : RecyclerView.Adapter<AvailableCryptoCurrencyListAdapter.ViewHolder>() {

    var items: MutableList<Market> = mutableListOf()
    var listener: (Market) -> Unit = {}

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        with(holder) {
            rootView.setOnClickListener({ listener.invoke(item) })
            marketName.text = item.name
            baseCurrency.text = item.baseCurrency
            quoteCurrency.text = item.quoteCurrency
            bestBid.text = item.bestBid.toPlainString()
            bestAsk.text = item.bestAsk.toPlainString()
            lastUpdated.text = item.lastUpdated.toString()
        }
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_market_item, parent, false))
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rootView = view.rootView
        val marketName = view.market_name
        val baseCurrency = view.base_currency
        val quoteCurrency = view.quote_currency
        val bestBid = view.best_bid
        val bestAsk = view.best_ask
        val lastUpdated = view.last_updated
    }
}