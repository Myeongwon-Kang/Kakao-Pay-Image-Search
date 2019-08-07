package com.genie97.myapplication.model;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageSearchApplication extends Application {

    private Retrofit retrofit;
    private NetworkService networkService;

    private static final String kakaoSearchBaseUrl = "https://dapi.kakao.com";

    @Override
    public void onCreate(){
        super.onCreate();
        // API inits in this class, so all activities can use the API
        setupAPIClient();

        // Global context
        ImageSearchApplication.context = getApplicationContext();
    }

    private void setupAPIClient(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor
                .Logger() {
            @Override
            public void log(String message) {
                Log.d("APIClient", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(kakaoSearchBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        networkService = retrofit.create(NetworkService.class);
    }

    public NetworkService getNetworkService(){
        return networkService;
    }

    private static Context context;

    public static Context getAppContext(){
        return ImageSearchApplication.context;
    }
}