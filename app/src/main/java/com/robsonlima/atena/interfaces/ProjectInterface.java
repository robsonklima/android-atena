package com.robsonlima.atena.interfaces;

import com.robsonlima.atena.models.Project;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProjectInterface {

    @GET("projects")
    Call<List<Project>> getListProjects();

    @GET("projects/?")
    Call<List<Project>> getProject(@Query("_id") int _id);

    @POST("projects/")
    Call<Project> createProject(@Body Project project);

    @PUT("projects/{_id}")
    Call <Project> updateProject(@Path("_id") int _id, @Body Project project);

    @DELETE("projects/{_id}")
    Call <Project> deleteProject(@Path("_id") int _id);

}