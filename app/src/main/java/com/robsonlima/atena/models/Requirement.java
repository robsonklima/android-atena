package com.robsonlima.atena.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Requirement {

    @SerializedName("_id")
    @Expose
    public String _id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("description")
    @Expose
    public String description;

    public Requirement(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
