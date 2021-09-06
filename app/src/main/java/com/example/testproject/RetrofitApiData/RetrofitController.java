package com.example.testproject.RetrofitApiData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitController {
    private static RetrofitController retrofitController;
    private static Retrofit retrofit;
    private OkHttpClient.Builder builder= new OkHttpClient.Builder();
    private HttpLoggingInterceptor interceptor= new HttpLoggingInterceptor();

    RetrofitController() {
        Gson gson= new GsonBuilder()
                .setLenient()
                .create();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);

        retrofit= new Retrofit.Builder()
                .baseUrl(ConstantUrl.URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized RetrofitController getInstance(){
        if (retrofitController==null){
            retrofitController= new RetrofitController();
        }
        return retrofitController;
    }

    public ApiConnector getApi(){
        return retrofit.create(ApiConnector.class);
    }
}
