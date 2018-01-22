package com.example.owner.petbetter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {


    //private static final String BASE_URL = "http://192.168.0.13/petbetter/";
    private static final String BASE_URL = "http://10.0.2.2/petbetter/";
    //private static final String BASE_URL = "http://localhost/petbetter/";
    //private static final String BASE_URL = "https://petbetter.herokuapp.com/";
    private static Retrofit retrofit;

    public static Retrofit getServiceGenerator(){
        if(retrofit==null){
            Gson gson = new GsonBuilder().setLenient().create();

            retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }
}
