package com.example.testproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.testproject.RetrofitApiData.ConnectionDetector;
import com.example.testproject.RetrofitApiData.RetrofitController;
import com.example.testproject.RetrofitApiData.UserDataHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout inputLayoutName,inputLayoutemail,inputLayoutPhone,inputLayoutPass,inputLayoutConfirmPass;
    MaterialButton registerButton;
    AlertDialog.Builder msgBox;
    Toolbar toolbar;

    public static final Pattern PHONE = Pattern.compile("^[0-9]{10}$");
    public static final Pattern NAME = Pattern.compile("^[a-zA-Z\\s]*$");
    public static final Pattern PASSWORD = Pattern.compile(
                    "^(?=.*[0-9])"+
                    "(?=.*[a-z])"+             // at least 1 small alphbabet
                    "(?=.*[A-Z])"+            // at least 1 capital alphbabet
                    "(?=.*[!@#$%*+_])" +     // at least 1 special character
                    "(?=\\S+$)" +           // no white spaces
                    ".{4,}" +              // at least 4 characters
                    "$");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar=findViewById(R.id.backButton);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerButton=findViewById(R.id.registerButton);

        inputLayoutPhone=findViewById(R.id.registerPhoneNumber);
        inputLayoutName=findViewById(R.id.registerName);
        inputLayoutemail=findViewById(R.id.registerEmailAddress);
        inputLayoutPass=findViewById(R.id.registerPassword);
        inputLayoutConfirmPass=findViewById(R.id.registerConfirmPassword);

        inputLayoutName.setTranslationX(800);
        inputLayoutName.setAlpha(0);
        inputLayoutName.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        inputLayoutemail.setTranslationX(800);
        inputLayoutemail.setAlpha(0);
        inputLayoutemail.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        inputLayoutPhone.setTranslationX(800);
        inputLayoutPhone.setAlpha(0);
        inputLayoutPhone.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        inputLayoutPass.setTranslationX(800);
        inputLayoutPass.setAlpha(0);
        inputLayoutPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        inputLayoutConfirmPass.setTranslationX(800);
        inputLayoutConfirmPass.setAlpha(0);
        inputLayoutConfirmPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();


        registerButton.setTranslationY(300);
        registerButton.setAlpha(0);
        registerButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        
        inputLayoutName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!NAME.matcher(inputLayoutName.getEditText().getText().toString()).matches()){
                    inputLayoutName.setError("Invalid Name Format");
                }else {
                    inputLayoutName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputLayoutemail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!Patterns.EMAIL_ADDRESS.matcher(inputLayoutemail.getEditText().getText().toString()).matches()){
                    inputLayoutemail.setError("Invalid Email Format");
                }else {
                    inputLayoutemail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputLayoutPhone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!PHONE.matcher(inputLayoutPhone.getEditText().getText().toString()).matches()){
                    inputLayoutPhone.setError("Invalid Phone Format");
                }else {
                    inputLayoutPhone.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputLayoutPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!PASSWORD.matcher(inputLayoutPass.getEditText().getText().toString()).matches()){
                    inputLayoutPass.setHintTextColor(getColorStateList(R.color.red));
                    inputLayoutPass.setBoxStrokeColor(getColor(R.color.red));
                    inputLayoutPass.setHelperText("Password Couldn't Matched");
                }else {
                    inputLayoutPass.setHintTextColor(getColorStateList(R.color.green));
                    inputLayoutPass.setBoxStrokeColor(getColor(R.color.green));
                    inputLayoutPass.setHelperText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputLayoutConfirmPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!inputLayoutConfirmPass.getEditText().getText().toString().trim()
                        .equals(inputLayoutPass.getEditText().getText().toString().trim())){
                    inputLayoutConfirmPass.setHintTextColor(getColorStateList(R.color.red));
                    inputLayoutConfirmPass.setBoxStrokeColor(getColor(R.color.red));
                    inputLayoutConfirmPass.setHelperText("Password Couldn't Matched");
                }else {
                    inputLayoutConfirmPass.setHintTextColor(getColorStateList(R.color.green));
                    inputLayoutConfirmPass.setBoxStrokeColor(getColor(R.color.green));
                    inputLayoutConfirmPass.setHelperText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(
                        inputLayoutName.getEditText().getText().toString().isEmpty() ||
                        inputLayoutemail.getEditText().getText().toString().isEmpty() ||
                        inputLayoutPhone.getEditText().getText().toString().isEmpty() ||
                        inputLayoutPass.getEditText().getText().toString().isEmpty() ||
                        inputLayoutConfirmPass.getEditText().getText().toString().isEmpty()){

                    msgBox=new AlertDialog.Builder(RegisterActivity.this);
                    msgBox.setTitle(R.string.app_name);
                    msgBox.setIcon(R.mipmap.ic_launcher);
                    msgBox.setMessage("You cannot register with empty fields");
                    msgBox.setCancelable(false);
                    msgBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    msgBox.show();
                }else if(!NAME.matcher(inputLayoutName.getEditText().getText().toString()).matches()){
                    inputLayoutName.setError("Invalid Name Format");
                }else if(!PHONE.matcher(inputLayoutPhone.getEditText().getText().toString()).matches()){
                    inputLayoutPhone.setError("Invalid Phone Number Format");
                }else if (!Patterns.EMAIL_ADDRESS.matcher(inputLayoutemail.getEditText().getText().toString()).matches()){
                    inputLayoutemail.setError("Invalid Email Format");
                }else if (!PASSWORD.matcher(inputLayoutPass.getEditText().getText().toString()).matches()){
                    inputLayoutPass.setError("Invalid Password Format");
                }else if(!inputLayoutConfirmPass.getEditText().getText().toString().trim().equals(inputLayoutPass.getEditText().getText().toString())){
                    inputLayoutConfirmPass.setError("Password didn't matched");
                    inputLayoutPass.getEditText().setText(null);
                    inputLayoutConfirmPass.getEditText().setText(null);
                    inputLayoutPass.getEditText().requestFocus();
                } else {
                    inputLayoutPass.setError(null);
                    inputLayoutemail.setError(null);
                    inputLayoutPhone.setError(null);
                    inputLayoutName.setError(null);
                    inputLayoutConfirmPass.setError(null);

                    CheckConnection();
                }
            }
        });
    }

    public void CheckConnection(){

        if (new ConnectionDetector(RegisterActivity.this).isConnectingToInternet()) {
            RegisterData();
        } else {
            new ConnectionDetector(RegisterActivity.this).connectiondetect();
        }
    }


    private void RegisterData(){
        ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        Call<UserDataHandler> call= RetrofitController.getInstance().getApi().addData(
                inputLayoutName.getEditText().getText().toString(),
                inputLayoutemail.getEditText().getText().toString(),
                inputLayoutPhone.getEditText().getText().toString(),
                inputLayoutPass.getEditText().getText().toString());

        call.enqueue(new Callback<UserDataHandler>() {
            @Override
            public void onResponse(Call<UserDataHandler> call, Response<UserDataHandler> response) {

                UserDataHandler dataResponse= response.body();
                if (response.isSuccessful()) {
                    msgBox = new AlertDialog.Builder(RegisterActivity.this);
                    msgBox.setIcon(R.mipmap.ic_launcher);
                    msgBox.setMessage(dataResponse.message);
                    msgBox.setTitle(R.string.msgTitle);
                    msgBox.setCancelable(false);
                    msgBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
                    progressDialog.dismiss();
                    msgBox.show();
                }else {
                    msgBox = new AlertDialog.Builder(RegisterActivity.this);
                    msgBox.setIcon(R.mipmap.ic_launcher);
                    msgBox.setMessage(dataResponse.message);
                    msgBox.setTitle(R.string.msgTitle);
                    msgBox.setCancelable(false);
                    msgBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
                    progressDialog.dismiss();
                    msgBox.show();
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
    }
}