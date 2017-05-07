package com.thulium.beetobee.WebService;

import com.thulium.beetobee.Formation.Formation;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Alex on 17/01/2017.
 * This interface is used by Retrofit
 */

public interface RequeteService {

    @GET("signin")
    Call<MyResponse> login(@Query("email") String email, @Query("password") String password);

    @GET("users/{id}")
    Call<MyResponse> loginWithToken(@Path("id") Integer id, @Query("access_token") String access_token);

    @POST("users/signup")
    Call<MyResponse> addStudent(@Body UserRegister user);

    @POST("users/update/{id}")
    Call<MyResponse> updateUser(@Body UserUpdate user, @Path("id") Integer id, @Query("access_token") String access_token);

    @Multipart
    @POST("users/{id}/picture")
    Call<ResponseBody> uploadProfilePicture(@Path("id") Integer id,@Part MultipartBody.Part file, @Query("access_token") String access_token);

    @GET
    Call<ResponseBody> downloadProfilePicture(@Url String fileUrl);

    @GET("formations/{id}")
    Call<MyFormationResponse> getFormation(@Path("id") Integer id);

    @POST("formations/participate/{formationId}/{userId}")
    Call<MyFormationResponse> participateFormation(@Path("formationId") Integer formationId,@Path("userId") Integer userId, @Query("access_token") String access_token);

    @POST("formations/participate/{formationId}/{userId}/delete")
    Call<SimpleResponse> deleteParticipateFormation(@Path("formationId") Integer formationId,@Path("userId") Integer userId, @Query("access_token") String access_token);

    @GET("formations/all")
    Call<AllFormationResponse> getAllFormation();

    @POST("formations/add")
    Call<MyFormationResponse> addFormation(@Body Formation formation);

    @POST("formations/delete/{formationId}")
    Call<SimpleResponse> deleteFormation(@Path("formationId") Integer id);
}
