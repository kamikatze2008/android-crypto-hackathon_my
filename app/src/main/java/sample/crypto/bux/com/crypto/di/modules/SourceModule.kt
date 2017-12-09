package sample.crypto.bux.com.crypto.di.modules

import com.bux.crypto.RxCrypto
import com.bux.crypto.RxCryptoDataManager
import com.bux.crypto.RxCryptoWebSocketConnectionManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SourceModule {

    @Provides
    @Singleton
    fun rxCrypto(): RxCrypto {
        return RxCrypto.getInstance()
    }

    @Provides
    @Singleton
    fun rxCryptoDataManager(rxCrypto: RxCrypto): RxCryptoDataManager {
        return rxCrypto.dataManager
    }

    @Provides
    @Singleton
    fun rxCryptoWebSocketConnectionManager(
            rxCrypto: RxCrypto): RxCryptoWebSocketConnectionManager {
        return rxCrypto.webSocketConnectionManager
    }

}