package com.example.testproject.RetrofitApiData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiConnector {
    @FormUrlEncoded
    @POST("register.php")
    Call<UserDataHandler> addData(@Field("name") String name, @Field("email") String email,
                                  @Field("contact") String contact, @Field("password") String password);

    @GET("login.php")
    Call<UserDataHandler> login(@Query("email") String login_email, @Query("password") String login_password);

    @GET("categories.php")
    Call<List<CategoryDataHandler>> categories();

    @GET("sub_categories.php")
    Call<List<SubCategoryDataHandler>> subcategories(@Query("id") int cat_id);

    @GET("products.php")
    Call<List<ProductDataHandler>> products(@Query("id") int sub_cat_id);

    @FormUrlEncoded
    @POST("update.php")
    Call<UserDataHandler> updateData(@Field("id")String id,
                                     @Field("name") String name,
                                     @Field("contact") String contact,
                                     @Field("password") String password);

    @FormUrlEncoded
    @POST("delete.php")
    Call<UserDataHandler> delete(@Field("id") String id);

}
