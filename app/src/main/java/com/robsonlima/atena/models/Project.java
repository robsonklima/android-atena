package com.robsonlima.atena.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Project {

    @SerializedName("_id")
    @Expose
    public String _id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("requirements")
    @Expose
    public List<Requirement> requirements = new ArrayList<>();

    public Project(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}