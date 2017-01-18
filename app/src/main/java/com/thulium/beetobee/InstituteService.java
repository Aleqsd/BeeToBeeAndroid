package com.thulium.beetobee;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Alex on 17/01/2017.
 */

public interface InstituteService {


    //i.e. http://localhost/api/institute/Students
    @GET("/institute/Students")
    public void getStudent(Callback<List<Student>> callback);

    //i.e. http://localhost/api/institute/Students/1
    @GET("/institute/Students/{id}")
    public void getStudentById(@Path("id") Integer id, Callback<Student> callback);

    //i.e. http://localhost/api/institute/Students/1
    @DELETE("/institute/Students/{id}")
    public void deleteStudentById(@Path("id") Integer id, Callback<Student> callback);

    //i.e. http://localhost/api/institute/Students/1
    @PUT("/institute/Students/{id}")
    public void updateStudentById(@Path("id") Integer id, @Body Student student, Callback<Student> callback);

    //i.e. http://localhost/api/institute/Students/1
    @POST("/institute/Students")
    public void addStudentById(@Body Student student, Callback<Student> callback);
}
