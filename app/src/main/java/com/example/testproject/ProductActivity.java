package com.example.testproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.testproject.RetrofitApiData.ProductDataHandler;
import com.example.testproject.RetrofitApiData.RetrofitController;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    Bundle bundle;
    int sub_cat_id;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ProductAdapter productAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        bundle = getIntent().getExtras();
        sub_cat_id = bundle.getInt("id");
        sub_cat_id = sub_cat_id + 1;

        toolbar = findViewById(R.id.toolbar_products);
        toolbar.setTitle(bundle.getString("productName"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.super.onBackPressed();
            }
        });
        recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getProductData();
    }

    public void getProductData() {
        progressDialog = new ProgressDialog(ProductActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Call<List<ProductDataHandler>> call = RetrofitController.getInstance().getApi().products(sub_cat_id);
        call.enqueue(new Callback<List<ProductDataHandler>>() {
            @Override
            public void onResponse(Call<List<ProductDataHandler>> call, Response<List<ProductDataHandler>> response) {
                if (response.isSuccessful()) {
                    List<ProductDataHandler> productDataHandler = response.body();
                    productAdapter = new ProductAdapter(ProductActivity.this, productDataHandler);
                    recyclerView.setAdapter(productAdapter);
                    progressDialog.dismiss();
                } else {
                    builder = new AlertDialog.Builder(ProductActivity.this);
                    builder.setTitle(R.string.msgTitle);
                    builder.setMessage("Products not found");
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(ProductActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductDataHandler>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.productHolder> {
        Context context;
        List<ProductDataHandler> productDataHandler;

        public ProductAdapter(ProductActivity productActivity, List<ProductDataHandler> productDataHandler) {
            this.context = productActivity;
            this.productDataHandler = productDataHandler;
        }

        public class productHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView productName, productPrice, productCompany;

            public productHolder(@NotNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.productImage_IV);
                productName = itemView.findViewById(R.id.productName_TV);
                productPrice = itemView.findViewById(R.id.productPrice_TV);
                productCompany = itemView.findViewById(R.id.productCompanyName_TV);
            }
        }

        @NotNull
        @Override
        public ProductAdapter.productHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_products, parent, false);
            return new productHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ProductAdapter.productHolder holder, int position) {
            Picasso.get().load(productDataHandler.get(position).getProdImgPath()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);
            holder.productName.setText("Name: " + productDataHandler.get(position).getProductName());
            holder.productPrice.setText("Price: " +"₹"+ productDataHandler.get(position).getProductPrice());
            holder.productCompany.setText("Company Name: " + productDataHandler.get(position).getCompanyName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle=  new Bundle();
                    bundle.putString("productImagePath", productDataHandler.get(position).getProdImgPath());
                    bundle.putString("productName", holder.productName.getText().toString());
                    bundle.putString("productPrice", holder.productPrice.getText().toString());
                    bundle.putString("productCompany", holder.productCompany.getText().toString());
                    bundle.putString("productDescription", productDataHandler.get(position).getProductDescription());

                    Intent intent = new Intent(ProductActivity.this, ProductDetailsActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return productDataHandler.size();
        }

    }

    @Override
    public void onBackPressed() {
    }
}