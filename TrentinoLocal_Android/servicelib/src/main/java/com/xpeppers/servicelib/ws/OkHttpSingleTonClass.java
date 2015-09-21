package com.xpeppers.servicelib.ws;

import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/20/2015
 */
public class OkHttpSingleTonClass {
    private static OkHttpSingleTonClass instance;
    private static OkHttpClient okHttpClient;
    private static Context context;

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", String.format("max-age=%d, only-if-cached, max-stale=%d", 120, 0))
                    .build();
        }
    };

    public static OkHttpSingleTonClass getInstance(Context context) {
        if(instance == null)
            instance = new OkHttpSingleTonClass();

        instance.context = context;

        return instance;
    }

    private OkHttpSingleTonClass() {
    }

    public OkHttpClient getOkHttpClient() throws IOException {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
            okHttpClient.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
            //okHttpClient.setRetryOnConnectionFailure(true);
            createCacheForOkHTTP();
        }
        return okHttpClient;
    }

    private void createCacheForOkHTTP() throws IOException {
        Cache cache = null;
        cache = new Cache(getDirectory(), 1024 * 1024 * 10);
        okHttpClient.setCache(cache);
    }

    private File getDirectory() {
        //return new File("location");
        return new File(context.getApplicationContext().getCacheDir().getAbsolutePath(), "HttpCache");
    }
}
