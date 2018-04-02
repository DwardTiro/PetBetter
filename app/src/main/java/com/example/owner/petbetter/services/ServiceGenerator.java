package com.example.owner.petbetter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    public static final String BASE_URL = "http://159.89.201.52/petbetter/";
    //public static final String BASE_URL = "http://172.20.10.4/petbetter/";
    //public static final String BASE_URL = "http://192.168.0.19/petbetter/";
    //public static final String BASE_URL = "http://172.16.4.44/petbetter/";
    //public static final String BASE_URL = "http://10.160.201.23/petbetter/";
    //private static final String BASE_URL = "http://10.0.2.2/petbetter/";
    //private static final String BASE_URL = "http://localhost/petbetter/";
    //private static final String BASE_URL = "https://petbetter.herokuapp.com/";
    private static Retrofit retrofit;

    public static Retrofit getServiceGenerator(){
        if(retrofit==null){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(logging);

            Gson gson = new GsonBuilder().setLenient().create();

            retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .client(httpClient.build())
                            .build();
        }
        return retrofit;
    }
}
