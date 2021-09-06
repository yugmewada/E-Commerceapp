package com.example.testproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testproject.RetrofitApiData.ConnectionDetector;
import com.example.testproject.RetrofitApiData.ConstantUrl;
import com.example.testproject.RetrofitApiData.RetrofitController;
import com.example.testproject.RetrofitApiData.UserDataHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    AlertDialog.Builder builder;
    MaterialButton loginButton;
    TextView textViewLink, textViewLink0;
    TextInputLayout inputLayoutEmail, inputLayoutPassword;
    SharedPreferences sharedPreferences;
    String shared_email, shared_password;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(ConstantUrl.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        shared_email = sharedPreferences.getString(ConstantUrl.EMAIL_KEY, null);
        shared_password = sharedPreferences.getString(ConstantUrl.PASS_KEY, null);

        inputLayoutEmail = findViewById(R.id.loginEmailAddress);
        inputLayoutPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        textViewLink0= findViewById(R.id.reg1);
        textViewLink = findViewById(R.id.regLink);


        inputLayoutEmail.setTranslationX(800);
        inputLayoutEmail.setAlpha(0);
        inputLayoutEmail.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        inputLayoutPassword.setTranslationX(800);
        inputLayoutPassword.setAlpha(0);
        inputLayoutPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        loginButton.setTranslationY(300);
        loginButton.setAlpha(0);
        loginButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        textViewLink0.setTranslationY(300);
        textViewLink0.setAlpha(0);
        textViewLink0.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        textViewLink.setTranslationY(300);
        textViewLink.setAlpha(0);
        textViewLink.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputLayoutEmail.getEditText().getText().toString().isEmpty()
                        || inputLayoutPassword.getEditText().getText().toString().isEmpty()) {
                    builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle(R.string.msgTitle);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setMessage("Please provide credentials");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    if (new ConnectionDetector(LoginActivity.this).isConnectingToInternet()) {
                        Login();
                    } else {
                        new ConnectionDetector(LoginActivity.this).connectiondetect();
                    }
                }
            }
        });

        textViewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finishAffinity();
            }
        });
    }

    private void Login() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        Call<UserDataHandler> loginModelCall = RetrofitController.getInstance().getApi()
                .login(inputLayoutEmail.getEditText().getText().toString(),
                        inputLayoutPassword.getEditText().getText().toString());

        loginModelCall.enqueue(new Callback<UserDataHandler>() {
            @Override
            public void onResponse(Call<UserDataHandler> call, Response<UserDataHandler> response) {

                UserDataHandler dataResponse = response.body();
                if (response.isSuccessful()) {
                    if (dataResponse.Status == String.valueOf(true)) {

                        for (int i = 0; i < dataResponse.response.size(); i++) {
                            sharedPreferences.edit().putString(ConstantUrl.ID_KEY, dataResponse.response.get(i).id).commit();
                            sharedPreferences.edit().putString(ConstantUrl.NAME_KEY, dataResponse.response.get(i).name).commit();
                            sharedPreferences.edit().putString(ConstantUrl.EMAIL_KEY, dataResponse.response.get(i).email).commit();
                            sharedPreferences.edit().putString(ConstantUrl.PHONE_KEY, dataResponse.response.get(i).contact).commit();
                            sharedPreferences.edit().putString(ConstantUrl.PASS_KEY, dataResponse.response.get(i).password).commit();
                        }

                        builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setCancelable(false);
                        builder.setTitle(R.string.msgTitle);
                        builder.setMessage(dataResponse.message);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                        progressDialog.dismiss();
                        builder.show();

                    } else {
                        builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setCancelable(false);
                        builder.setTitle(R.string.msgTitle);
                        builder.setMessage(dataResponse.message);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        progressDialog.dismiss();
                        builder.show();
                    }
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
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.msgTitle);
        builder.setMessage("Are you sure want to exit...?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (shared_email != null && shared_password != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finishAffinity();
        }
    }
}