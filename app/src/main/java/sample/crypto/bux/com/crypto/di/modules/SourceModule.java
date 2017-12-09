package sample.crypto.bux.com.crypto.di.modules;

import com.bux.crypto.RxCrypto;
import com.bux.crypto.RxCryptoDataManager;
import com.bux.crypto.RxCryptoWebSocketConnectionManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SourceModule {

    @Provides
    @Singleton
    static RxCrypto rxCrypto() {
        return RxCrypto.getInstance();
    }

    @Provides
    @Singleton
    static RxCryptoDataManager rxCryptoDataManager(final RxCrypto rxCrypto) {
        return rxCrypto.getDataManager();
    }

    @Provides
    @Singleton
    static RxCryptoWebSocketConnectionManager rxCryptoWebSocketConnectionManager(
            final RxCrypto rxCrypto) {
        return rxCrypto.getWebSocketConnectionManager();
    }

}
