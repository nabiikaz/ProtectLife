package com.pfe.protectlife.Services;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class retrofitClient {
    String API_BASE_URL = "https://protectioncivile2020.herokuapp.com/api/";
    //private static String API_BASE_URL = "http://192.168.1.2:30001/api/";

    String authToken;
    private static Retrofit retrofit;
    private static retrofitClient instance;






    public Retrofit getRetrofit() {


        return retrofit;
    }


    public retrofitClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .client(httpClient.build())
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        retrofit = builder.build();


    }


}

