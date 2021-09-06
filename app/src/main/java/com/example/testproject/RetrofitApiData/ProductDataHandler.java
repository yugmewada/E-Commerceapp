package com.example.testproject.RetrofitApiData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDataHandler {

    @SerializedName("product name")
    @Expose
    public String productName;

    @SerializedName("product_description")
    @Expose
    public String productDescription;

    @SerializedName("company name")
    @Expose
    public String companyName;

    @SerializedName("prod_img_path")
    @Expose
    public String prodImgPath;

    @SerializedName("product price")
    @Expose
    public String productPrice;


    public String getProductName() {
        return productName;
    }

    public String getProductDescription() { return productDescription; }

    public String getCompanyName() {
        return companyName;
    }

    public String getProdImgPath() {
        return prodImgPath;
    }

    public String getProductPrice() {
        return productPrice;
    }
}
