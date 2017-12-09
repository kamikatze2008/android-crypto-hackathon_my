package sample.crypto.bux.com.crypto.presentation.base

import android.support.annotation.CallSuper
import rx.Subscription

abstract class BasePresenter<T : BaseView> {

    private val subscriptionList: MutableList<Subscription> = mutableListOf()

    var view: T? = null

    @CallSuper
    open fun pause() = dispose()

    @CallSuper
    open fun resume() = Unit

    @CallSuper
    open fun destroy() {
        dispose()
        view = null
    }

    fun dispose() {
        subscriptionList.forEach({ it.unsubscribe() })
        subscriptionList.clear()
    }

    fun safeSubscribe(action: () -> Subscription) {
        subscriptionList.add(action())
    }


}