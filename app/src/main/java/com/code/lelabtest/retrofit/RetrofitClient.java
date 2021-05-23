package com.code.lelabtest.retrofit;

import android.content.Context;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.code.lelabtest.config.Constants.baseUrl;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static Context ctx;


    public RetrofitClient(Context ctx) {

        RetrofitClient.ctx = ctx;
    }

    public static Retrofit getInstance() {

        if (retrofit == null) {

            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(1);

            OkHttpClient okClient = new OkHttpClient.Builder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .dispatcher(dispatcher)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .client(okClient)
                    .build();
        }


        return retrofit;
    }

}
