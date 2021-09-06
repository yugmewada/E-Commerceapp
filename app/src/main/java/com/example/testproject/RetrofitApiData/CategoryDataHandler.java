package com.example.testproject.RetrofitApiData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryDataHandler {

    @SerializedName("cat_name")
    @Expose
    public String catName;

    @SerializedName("cat_img_path")
    @Expose
    public String catImgPath;

    public String getCatName() { return catName; }
    public String getCatImgPath() { return catImgPath; }

}
