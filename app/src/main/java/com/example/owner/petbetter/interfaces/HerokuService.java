package com.example.owner.petbetter;

/**
 * Created by owner on 12/11/2017.
 */
import com.example.owner.petbetter.classes.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HerokuService {

    @FormUrlEncoded
    @POST("checkLogin.php")
    Call<User> checkLogin(@Field("email") String email, @Field("password") String password);
}
