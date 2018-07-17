package com.robsonlima.atena.interfaces;

import com.robsonlima.atena.models.Requirement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RequirementInterface {

    @GET("projects/requirements/{id}")
    Call<Requirement> getRequirement(@Path("id") String id);

    @POST("projects/requirements/{id}")
    Call<Requirement> createRequirement(@Path("id") String id, @Body Requirement requirement);

    @PUT("projects/requirements/{id}")
    Call <Requirement> updateRequirement(@Path("id") String id, @Body Requirement requirement);

    @DELETE("projects/requirements/{id}")
    Call<Void> deleteRequirement(@Path("id") String id);

}
