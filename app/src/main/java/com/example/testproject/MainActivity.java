package com.example.testproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.testproject.RetrofitApiData.ConstantUrl;
import com.example.testproject.RetrofitApiData.RetrofitController;
import com.example.testproject.RetrofitApiData.UserDataHandler;
import com.google.android.material.navigation.NavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ProgressDialog pg;
    AlertDialog.Builder builder;
    public static MaterialSearchView homeSearchView;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String shared_email, shared_password;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(ConstantUrl.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        shared_email = sharedPreferences.getString(ConstantUrl.EMAIL_KEY, null);
        shared_password = sharedPreferences.getString(ConstantUrl.PASS_KEY, null);


        navigationView = findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        TextView menu_name=headerView.findViewById(R.id.menu_name);
        TextView menu_email=headerView.findViewById(R.id.menu_email);
        menu_name.setText(sharedPreferences.getString(ConstantUrl.NAME_KEY,null));
        menu_email.setText(shared_email);

        homeSearchView = findViewById(R.id.search_View);
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePageFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
            toolbar.setTitle("Home");
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_profie:
                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                        finish();
                        break;

                    case R.id.nav_profie_update:
                        startActivity(new Intent(MainActivity.this, UpdateActivity.class));
                        finish();
                        break;

                    case R.id.nav_profie_delete:
                        builder= new AlertDialog.Builder(MainActivity.this);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setTitle(R.string.msgTitle);
                        builder.setMessage("Are you sure want to delete your profile");
                        builder.setCancelable(false);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProfile();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                        break;

                    case R.id.nav_logout:
                        editor = sharedPreferences.edit();
                        editor.clear().apply();

                        pg = new ProgressDialog(MainActivity.this);
                        pg.setMessage("Please Wait");
                        pg.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Logout Success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        }, 1000);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        homeSearchView.setMenuItem(item);
        return true;
    }

    public boolean onOptionsItemSelected( MenuItem item) {
       return true;
    }

    public void deleteProfile(){
        pg= new ProgressDialog(this);
        pg.setMessage("Deleting Profile");
        pg.show();

        Call<UserDataHandler> call= RetrofitController.getInstance().getApi().delete(
                sharedPreferences.getString(ConstantUrl.ID_KEY, null));
        call.enqueue(new Callback<UserDataHandler>() {
            @Override
            public void onResponse(Call<UserDataHandler> call, Response<UserDataHandler> response) {
                UserDataHandler dataHandler= response.body();
                if (response.isSuccessful()){
                    editor = sharedPreferences.edit();
                    editor.clear().apply();

                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setTitle(R.string.msgTitle);
                    builder.setMessage(dataHandler.message);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
                    pg.dismiss();
                    builder.show();
                }else {
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setTitle(R.string.msgTitle);
                    builder.setMessage(dataHandler.message);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    pg.dismiss();
                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<UserDataHandler> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

       @Override
    public void onBackPressed() {
        if (homeSearchView.isSearchOpen()) {
            homeSearchView.closeSearch();
        }else if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            builder=new AlertDialog.Builder(MainActivity.this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(R.string.msgTitle);
            builder.setMessage("Are you sure want to exit..?");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
}
