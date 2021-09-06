package com.example.testproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.testproject.RetrofitApiData.CategoryDataHandler;
import com.example.testproject.RetrofitApiData.ConnectionDetector;
import com.example.testproject.RetrofitApiData.ConstantUrl;
import com.example.testproject.RetrofitApiData.RetrofitController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomePageFragment extends Fragment {

    View view;
    ImageSlider imageSlider;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    ArrayList<CategoryDataHandler> searchCategory;
    CategoryAdapter categoryAdapter;
    SharedPreferences sharedPreferences;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = getActivity().getSharedPreferences(ConstantUrl.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        //setUPSearchView();
        //setUpSlider();
        RecyclerViewGrid();
        return view;
    }

   /* public void setUPSearchView() {
        MainActivity.homeSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().equalsIgnoreCase("")) {
                    categoryAdapter.filter("");
                } else {
                    categoryAdapter.filter(newText);
                }
                return false;
            }
        });
    }
*/
    private void setUpSlider() {
        imageSlider = view.findViewById(R.id.ImageSlider);
        List<SlideModel> slideImages = new ArrayList<>();
        slideImages.add(new SlideModel(R.drawable.amazon1, ScaleTypes.CENTER_CROP));
        slideImages.add(new SlideModel(R.drawable.amazon2, ScaleTypes.CENTER_CROP));
        slideImages.add(new SlideModel(R.drawable.amazon3, ScaleTypes.CENTER_CROP));
        slideImages.add(new SlideModel(R.drawable.amazon4, ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideImages);

    }

    private void RecyclerViewGrid() {
        recyclerView = view.findViewById(R.id.categories_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
           getData();
        } else {
            new ConnectionDetector(getActivity()).connectiondetect();
        }
    }


    public void getData(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Getting Data");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Call<List<CategoryDataHandler>> call= RetrofitController.getInstance().getApi().categories();
        call.enqueue(new Callback<List<CategoryDataHandler>>() {
            @Override
            public void onResponse(Call<List<CategoryDataHandler>> call, Response<List<CategoryDataHandler>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    List<CategoryDataHandler> list= response.body();
                    categoryAdapter = new CategoryAdapter(getActivity(),list);
                    recyclerView.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryDataHandler>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.categoryHolder> {
        Context context;
        List<CategoryDataHandler> categoryDataHandler;

        public CategoryAdapter(Context context, List<CategoryDataHandler> categoryDataHandler) {
            this.context=context;
            this.categoryDataHandler=categoryDataHandler;

            searchCategory = new ArrayList<>();
            searchCategory.addAll(categoryDataHandler);
        }

        public class categoryHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;

            public categoryHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.category_TV);
                imageView = itemView.findViewById(R.id.category_IV);
            }
        }

        @NonNull
        @Override
        public categoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_category, parent, false);
            return new categoryHolder(view);
        }

       /*public void filter(String searchedString) {
            searchedString = searchedString.toLowerCase(Locale.getDefault());
            categoryDataHandler.clear();
            if (searchedString.length() == 0) {
                categoryDataHandler.addAll(searchCategory);

            } else {
                for (CategoryDataHandler categoryDataHandler : searchCategory) {
                    if (categoryDataHandler.getCatName().toLowerCase(Locale.getDefault()).contains(searchedString)) {
                        //categoryDataHandler.add(categoryList);
                    }
                }
            }
            notifyDataSetChanged();
        }*/

        @Override
        public void onBindViewHolder(@NonNull HomePageFragment.CategoryAdapter.categoryHolder holder, int position) {
            Picasso.get().load(categoryDataHandler.get(position).getCatImgPath()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);
            holder.textView.setText(categoryDataHandler.get(position).getCatName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle = new Bundle();
                    bundle.putInt("id", position);
                    bundle.putString("categoryName", holder.textView.getText().toString());
                    Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return categoryDataHandler.size();
        }

    }
}