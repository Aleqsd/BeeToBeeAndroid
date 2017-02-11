package com.thulium.beetobee.WebService;

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

/**
 * Created by Alex on 17/01/2017.
 * This interface is used by Retrofit
 */

public interface RequeteService {

    @GET("/signin")
    Call<MyResponse> login(@Query("email") String email, @Query("password") String password);

    @GET("users/{id}")
    Call<MyResponse> loginWithToken(@Path("id") Integer id, @Query("access_token") String access_token);

    @POST("/users/register")
    Call<UserRegister> addStudent(@Body UserRegister user);

    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(@Part MultipartBody.Part file);

    //i.e. http://localhost/api/institute/Students
    //@GET("/institute/Students")
    //public void getStudent(Callback<List<User>> callback);

    //@GET("/users/{id}")
    //void getStudentById(@Path("id") Integer id, Callback<MyResponse> callback);

    //i.e. http://localhost/api/institute/Students/1
    //@DELETE("/institute/Students/{id}")
    //public void deleteStudentById(@Path("id") Integer id, Callback<User> callback);

    //i.e. http://localhost/api/institute/Students/1
    //@PUT("/institute/Students/{id}")
    //public void updateStudentById(@Path("id") Integer id, @Body User user, Callback<User> callback);
}
