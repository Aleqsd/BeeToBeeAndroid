package com.thulium.beetobee.WebService;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Alex on 17/01/2017.
 */

public interface RequeteService {


    //i.e. http://localhost/api/institute/Students
    //@GET("/institute/Students")
    //public void getStudent(Callback<List<User>> callback);

    //@GET("/users/{id}")
    //void getStudentById(@Path("id") Integer id, Callback<MyResponse> callback);

    @GET("/signin")
    void login(@Query("email") String email, @Query("password") String password, Callback<MyResponse> callback);

    @GET("/users/{id}")
    void loginWithToken(@Path("id") Integer id, @Query("access_token") String access_token,Callback<MyResponse> callback);

    //i.e. http://localhost/api/institute/Students/1
    //@DELETE("/institute/Students/{id}")
    //public void deleteStudentById(@Path("id") Integer id, Callback<User> callback);

    //i.e. http://localhost/api/institute/Students/1
    //@PUT("/institute/Students/{id}")
    //public void updateStudentById(@Path("id") Integer id, @Body User user, Callback<User> callback);

    //i.e. http://localhost/api/institute/Students/1
    @POST("/users/register")
    void addStudent(@Body UserRegister user, Callback<UserRegister> callback);
}
