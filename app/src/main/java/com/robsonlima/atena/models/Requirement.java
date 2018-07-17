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

    public Requirement(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
