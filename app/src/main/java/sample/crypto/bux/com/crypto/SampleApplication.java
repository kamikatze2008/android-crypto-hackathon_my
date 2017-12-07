package sample.crypto.bux.com.crypto;

import android.app.Application;

import com.bux.crypto.Crypto;

public class SampleApplication extends Application {

    private static final String AUTHORIZATION_TOKEN = "YOUR_TOKEN_HERE";

    @Override
    public void onCreate() {
        super.onCreate();
        Crypto.init(AUTHORIZATION_TOKEN);
    }
}
