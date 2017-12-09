package sample.crypto.bux.com.crypto;

import android.app.Application;

import com.bux.crypto.Crypto;

/**
 * In order to use Crypto framework you need to initialize {@link Crypto} instance with
 * the provided authorization token first.
 * If you are familiar with RxJava and would like to use it in you app you can replace {@link Crypto} with
 * {@link com.bux.crypto.RxCrypto}.
 */
public class SampleApplication extends Application {

    private static final String AUTHORIZATION_TOKEN = "YOUR_TOKEN_HERE";

    @Override
    public void onCreate() {
        super.onCreate();
        Crypto.init(AUTHORIZATION_TOKEN);
    }
}
