package com.xpeppers.servicelib.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/08/24
 */
public class AssetsPropertyReader {
    private static AssetsPropertyReader instance;
    private Context context;
    private Properties properties;

    public static AssetsPropertyReader getInstance(Context context) {
        if(instance == null)
            instance = new AssetsPropertyReader();

        instance.context = context;
        instance.properties = new Properties();

        return instance;
    }

    public Properties getProperties(String FileName) {

        try {
            /**
             * getAssets() Return an AssetManager instance for your
             * application's package. AssetManager Provides access to an
             * application's raw asset files;
             */
            AssetManager assetManager = context.getAssets();
            /**
             * Open an asset using ACCESS_STREAMING mode. This
             */
            InputStream inputStream = assetManager.open(FileName);
            /**
             * Loads properties from the specified InputStream,
             */
            properties.load(inputStream);

        } catch (IOException e) {
            Log.e("AssetsPropertyReader", e.toString());
        }
        return properties;

    }

}