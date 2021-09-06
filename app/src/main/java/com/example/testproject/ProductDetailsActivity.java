package com.example.testproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView prodName,prodPrice,prodCompany,prodDescription;
    ImageView imageView;
    Bundle bundle;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        bundle= getIntent().getExtras();

        imageView= findViewById(R.id.productDetailImage);
        prodName= findViewById(R.id.productDetailName_TV);
        prodPrice= findViewById(R.id.productDetailPrice_TV);
        prodCompany= findViewById(R.id.productDetailCompany_TV);
        prodDescription= findViewById(R.id.productDetailDescription_TV);

        toolbar= findViewById(R.id.toolbar_productDetails);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailsActivity.super.onBackPressed();
            }
        });

        /*for(int i=1; i<=10; i++){
            list= new ArrayList<>();
            list.add(String.valueOf(i));
            arrayAdapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
            spinner.setAdapter(arrayAdapter);
        }*/

        getproductDetails();
    }

    private void getproductDetails() {
        Picasso.get().load(bundle.get("productImagePath").toString()).placeholder(R.mipmap.ic_launcher).into(imageView);
        prodName.setText(bundle.getString("productName"));
        prodPrice.setText(bundle.getString("productPrice"));
        prodCompany.setText(bundle.getString("productCompany"));
        prodDescription.setText("Description: "+bundle.getString("productDescription"));
    }

    @Override
    public void onBackPressed() {

    }
}
