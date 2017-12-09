package sample.crypto.bux.com.crypto.presentation

import com.bux.crypto.RxCrypto
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import sample.crypto.bux.com.crypto.di.component.DaggerAppComponent

class MainApplication : DaggerApplication() {

    companion object {
        val AUTHORIZATION_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJyZWZyZXNoYWJsZSI6ZmFsc2UsInN1YiI6Ijc5YTEyMTAyLTNjYzItNDU1NC1hOTA3LTU2ZGM3MzU4ZjkzNiIsImF1ZCI6ImRldi5nZXRidXguY29tIiwic2NwIjpbImNyeXB0bzpsb2dpbiIsImNyeXB0bzphZG1pbiJdLCJleHAiOjE1NDQxOTYxMzQsImlhdCI6MTUxMjYzOTIwOCwianRpIjoiODQ2NGVhZjEtOWFjMy00MTliLTg2NjUtYTQ1NDA5NWUwMmU4IiwiY2lkIjoiODQ3MzYyMzgwNCJ9.q8YB4HOOwooQoctm2EejuX_SXlsYGhPaSKxEc7RqaPQ"
    }

    override fun onCreate() {
        Thread.setDefaultUncaughtExceptionHandler({ _, e ->
            run {
                e.printStackTrace()
            }
        })
        super.onCreate()
        RxCrypto.init(AUTHORIZATION_TOKEN)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}
