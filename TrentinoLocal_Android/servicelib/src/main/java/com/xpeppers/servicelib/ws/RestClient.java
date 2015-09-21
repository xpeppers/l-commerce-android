package com.xpeppers.servicelib.ws;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.xpeppers.servicelib.BuildConfig;
import com.xpeppers.servicelib.utils.AssetsPropertyReader;
import com.xpeppers.servicelib.utils.Constants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Properties;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class RestClient {
    private ApiService apiService;
    private Context context;

    public RestClient(Context context)
    {
        this.context = context;

        String BASE_URL = Constants.BASE_PATH;
        Properties servicesProperties = AssetsPropertyReader.getInstance(context).getProperties(Constants.SERVICES_PROPERTIES);

        if(servicesProperties != null && !servicesProperties.isEmpty()) {
            switch (BuildConfig.API_SERVER) {
                case 0:
                    BASE_URL = servicesProperties.getProperty(Constants.PROPERTIES_DEBUG_BASE_URL);
                    break;
                case 1:
                    BASE_URL = servicesProperties.getProperty(Constants.PROPERTIES_ALPHA_BASE_URL);
                    break;
                case 2:
                    BASE_URL = servicesProperties.getProperty(Constants.PROPERTIES_RELEASE_BASE_URL);
                    break;
                default:
                    BASE_URL = Constants.BASE_PATH;
            }
        }


        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        // DESERIALIZER
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        // to serialize and deserialize byte arrays in base64
        // (SEE: http://stackoverflow.com/questions/25522309/converting-json-between-string-and-byte-with-gson)
        builder.registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter());


        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Gson gson = builder.create();

        RestAdapter restAdapter = null;
        try {
            restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setConverter(new GsonConverter(gson))
                    .setEndpoint(BASE_URL)
                    .setClient(new OkClient(OkHttpSingleTonClass.getInstance(this.context).getOkHttpClient()))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        apiService = restAdapter.create(ApiService.class);
    }

    public ApiService getApiService() { return apiService; }


    private static class ByteArrayToBase64TypeAdapter implements JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.decode(json.getAsString(), Base64.NO_WRAP);
        }
    }
}
