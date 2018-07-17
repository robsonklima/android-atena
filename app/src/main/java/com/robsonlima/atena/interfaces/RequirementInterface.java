package com.robsonlima.atena.interfaces;

import com.robsonlima.atena.models.Project;
import com.robsonlima.atena.models.Requirement;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RequirementInterface {

    @GET("projects/requirements/{id}")
    Call<Requirement> getRequirement(@Path("id") String id);

    @DELETE("projects/requirements/{id}")
    Call<Void> deleteRequirement(@Path("id") String id);

}
