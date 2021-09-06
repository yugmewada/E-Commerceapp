package com.example.testproject;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.testproject.RetrofitApiData.RetrofitController;
import com.example.testproject.RetrofitApiData.SubCategoryDataHandler;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    SubCategoryAdapter subCategoryAdapter;
    Bundle bundle;
    int  cat_id;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        bundle=getIntent().getExtras();
        cat_id =bundle.getInt("id");
        cat_id = cat_id+1;

        toolbar= findViewById(R.id.toolbar_subCategory);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Categories for "+bundle.getString("categoryName"));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubCategoryActivity.super.onBackPressed();
            }
        });

        recyclerView= findViewById(R.id.subCategoryRecyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getSubCategoryData();
    }
    public void getSubCategoryData(){
        progressDialog = new ProgressDialog(SubCategoryActivity.this);
        progressDialog.setMessage("Getting Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<List<SubCategoryDataHandler>> call= RetrofitController.getInstance().getApi().subcategories(cat_id);
        call.enqueue(new Callback<List<SubCategoryDataHandler>>() {
            @Override
            public void onResponse(Call<List<SubCategoryDataHandler>> call, Response<List<SubCategoryDataHandler>> response) {
                if (response.isSuccessful()){
                    List<SubCategoryDataHandler> subCategoryDataHandler = response.body();
                    subCategoryAdapter= new SubCategoryAdapter(SubCategoryActivity.this, subCategoryDataHandler);
                    recyclerView.setAdapter(subCategoryAdapter);
                    progressDialog.dismiss();
                }else {
                    builder= new AlertDialog.Builder(SubCategoryActivity.this);
                    builder.setTitle(R.string.msgTitle);
                    builder.setMessage("Data not found");
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(SubCategoryActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<List<SubCategoryDataHandler>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.subCategoryHolder> {
        Context context;
        List<SubCategoryDataHandler>subCategoryDataHandler;

        public SubCategoryAdapter(SubCategoryActivity subCategoryActivity, List<SubCategoryDataHandler>subCategoryDataHandler) {
            this.context= subCategoryActivity;
            this.subCategoryDataHandler=subCategoryDataHandler;
        }

        public class subCategoryHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;

            public subCategoryHolder(@NonNull  View itemView) {
                super(itemView);
                textView= itemView.findViewById(R.id.subCategory_TV);
                imageView= itemView.findViewById(R.id.subCategory_IV);
            }
        }

        @NonNull
        @Override
        public subCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.content_sub_category,parent,false);
            return new subCategoryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull  SubCategoryActivity.SubCategoryAdapter.subCategoryHolder holder, int position) {
            Picasso.get().load(subCategoryDataHandler.get(position).getSubCatImagePath()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);
            holder.textView.setText(subCategoryDataHandler.get(position).getSubCatName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle=  new Bundle();
                    bundle.putInt("id", position);
                    bundle.putString("productName", holder.textView.getText().toString());
                    Intent intent = new Intent(SubCategoryActivity.this, ProductActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return subCategoryDataHandler.size();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
