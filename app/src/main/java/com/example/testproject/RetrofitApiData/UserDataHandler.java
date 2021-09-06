package com.example.testproject.RetrofitApiData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDataHandler {
    @SerializedName("message1")
    @Expose
    public String message;
    @SerializedName("response")
    @Expose
    public List<UserDataResponse> response = null;

    @SerializedName("status")
    @Expose
    public String Status;

    public class UserDataResponse {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("contact")
        @Expose
        public String contact;
        @SerializedName("password")
        @Expose
        public String password;
    }
}
