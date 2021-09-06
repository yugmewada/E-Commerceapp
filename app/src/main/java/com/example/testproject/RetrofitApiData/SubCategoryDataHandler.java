package com.example.testproject.RetrofitApiData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategoryDataHandler {

    @SerializedName("sub_cat_name")
    @Expose
    public String subCatName;

    @SerializedName("sub_cat_image_path")
    @Expose
    public String subCatImagePath;

    public String getSubCatName() { return subCatName; }
    public String getSubCatImagePath() { return subCatImagePath; }
}
