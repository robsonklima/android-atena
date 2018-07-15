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

    @DELETE("projects/{id}")
    Call <Project> deleteProject(@Path("id") String id);

    @GET("projects/{id}")
    Call<Project> getProject(@Path("id") String id);

    @POST("projects/")
    Call<Project> createProject(@Body Project project);

    @PUT("projects/{id}")
    Call <Project> updateProject(@Path("id") String id, @Body Project project);

}