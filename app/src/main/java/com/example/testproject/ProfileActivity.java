package com.example.testproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.testproject.RetrofitApiData.ConstantUrl;


public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout linearLayout;
    View view;
    TextView name, email, contact;
    SharedPreferences sharedPreferences;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences=getSharedPreferences(ConstantUrl.SHARED_PREF_KEY, Context.MODE_PRIVATE);

        name= findViewById(R.id.user_profile_name);
        email= findViewById(R.id.user_profile_email);
        contact=findViewById(R.id.user_profile_contact);

        toolbar=findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        getData();
    }

    private void getData(){
       name.setText(sharedPreferences.getString(ConstantUrl.NAME_KEY,null));
       email.setText(sharedPreferences.getString(ConstantUrl.EMAIL_KEY,null));
       contact.setText(sharedPreferences.getString(ConstantUrl.PHONE_KEY,null));
    }

    @Override
    public void onBackPressed() {
    }

}
