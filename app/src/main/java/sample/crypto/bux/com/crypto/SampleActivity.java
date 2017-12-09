package sample.crypto.bux.com.crypto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bux.crypto.Crypto;
import com.bux.crypto.CryptoDataManager;
import com.bux.crypto.CryptoWebSocketConnectionManager;

/**
 * Use {@link #cryptoDataManager} and {@link #cryptoWebSocketConnectionManager} to work with data.
 */
public class SampleActivity extends AppCompatActivity {

    private CryptoDataManager cryptoDataManager = Crypto.getInstance().getDataManager();
    private CryptoWebSocketConnectionManager cryptoWebSocketConnectionManager = Crypto.getInstance().getWebSocketConnectionManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
