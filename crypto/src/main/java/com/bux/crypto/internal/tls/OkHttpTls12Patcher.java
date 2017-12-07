package com.bux.crypto.internal.tls;

import android.os.Build;
import android.util.Log;

import java.security.KeyStore;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

public class OkHttpTls12Patcher {

    public static void patch(OkHttpClient.Builder builder) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            enableTsl12(builder);
        }

        ConnectionSpec tls12ConnectionSpec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .allEnabledCipherSuites()
                .build();

        builder.connectionSpecs(Arrays.asList(tls12ConnectionSpec, ConnectionSpec.COMPATIBLE_TLS));
    }


    private static void enableTsl12(OkHttpClient.Builder builder) {
        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init((KeyStore) null);
            TrustManager[] trustManagers = factory.getTrustManagers();
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            SSLContext tls12Context = SSLContext.getInstance("TLSv1.2");
            tls12Context.init(null, trustManagers, null);
            SSLSocketFactory sslSocketFactory = new Tls12SocketFactory(tls12Context.getSocketFactory());

            builder.sslSocketFactory(sslSocketFactory, trustManager);
        } catch (Exception exc) {
            // Our logging isn't enabled at this point, just using the system one to reach logcat
            Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
        }
    }
}
