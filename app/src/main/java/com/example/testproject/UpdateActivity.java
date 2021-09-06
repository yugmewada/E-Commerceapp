package com.example.testproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.testproject.RetrofitApiData.ConnectionDetector;
import com.example.testproject.RetrofitApiData.ConstantUrl;
import com.example.testproject.RetrofitApiData.RetrofitController;
import com.example.testproject.RetrofitApiData.UserDataHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {
    Toolbar toolbar;
    HashMap<String, String> hashMap;
    TextInputLayout inputLayoutName,inputLayoutPhone, inputLayoutPass, inputLayoutConfirmPass;
    MaterialButton updateButton;
    AlertDialog.Builder msgBox;
    SharedPreferences sharedPreferences;

    public static final Pattern PHONE = Pattern.compile("^[0-9]{10}$");
    public static final Pattern NAME = Pattern.compile("^[a-zA-Z\\s]*$");
    public static final Pattern PASSWORD = Pattern.compile(
            "^(?=.*[0-9])" +
                    "(?=.*[a-z])" +             // at least 1 small alphbabet
                    "(?=.*[A-Z])" +            // at least 1 capital alphbabet
                    "(?=.*[!@#$%*+_])" +     // at least 1 special character
                    "(?=\\S+$)" +           // no white spaces
                    ".{4,}" +              // at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        toolbar = findViewById(R.id.updateToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateActivity.this, MainActivity.class));
                finish();
            }
        });

        sharedPreferences=getSharedPreferences(ConstantUrl.SHARED_PREF_KEY,MODE_PRIVATE);
        updateButton = findViewById(R.id.updateButton);

        inputLayoutPhone = findViewById(R.id.updatePhoneNumber);
        inputLayoutName = findViewById(R.id.updateName);
        inputLayoutPass = findViewById(R.id.updatePassword);
        inputLayoutConfirmPass = findViewById(R.id.updateConfirmPassword);

        getData();

        inputLayoutName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!NAME.matcher(inputLayoutName.getEditText().getText().toString()).matches()) {
                    inputLayoutName.setError("Invalid Name Format");
                } else {
                    inputLayoutName.setError(null);
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
                if (!PHONE.matcher(inputLayoutPhone.getEditText().getText().toString()).matches()) {
                    inputLayoutPhone.setError("Invalid Phone Format");
                } else {
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
                if (!PASSWORD.matcher(inputLayoutPass.getEditText().getText().toString()).matches()) {
                    inputLayoutPass.setHintTextColor(getColorStateList(R.color.red));
                    inputLayoutPass.setBoxStrokeColor(getColor(R.color.red));
                    inputLayoutPass.setHelperText("Password Couldn't Matched");
                } else {
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
                if (!inputLayoutConfirmPass.getEditText().getText().toString().trim()
                        .equals(inputLayoutPass.getEditText().getText().toString().trim())) {
                    inputLayoutConfirmPass.setHintTextColor(getColorStateList(R.color.red));
                    inputLayoutConfirmPass.setBoxStrokeColor(getColor(R.color.red));
                    inputLayoutConfirmPass.setHelperText("Password Couldn't Matched");
                } else {
                    inputLayoutConfirmPass.setHintTextColor(getColorStateList(R.color.green));
                    inputLayoutConfirmPass.setBoxStrokeColor(getColor(R.color.green));
                    inputLayoutConfirmPass.setHelperText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (
                        inputLayoutName.getEditText().getText().toString().isEmpty()
                                || inputLayoutPhone.getEditText().getText().toString().isEmpty()
                                || inputLayoutPass.getEditText().getText().toString().isEmpty()
                                || inputLayoutConfirmPass.getEditText().getText().toString().isEmpty()) {

                    msgBox = new AlertDialog.Builder(UpdateActivity.this);
                    msgBox.setTitle(R.string.app_name);
                    msgBox.setIcon(R.mipmap.ic_launcher);
                    msgBox.setMessage("You cannot update with empty fields");
                    msgBox.setCancelable(false);
                    msgBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            inputLayoutName.getEditText().requestFocus();
                        }
                    });
                    msgBox.show();
                }else if(!PHONE.matcher(inputLayoutPhone.getEditText().getText().toString()).matches()){
                    inputLayoutPhone.setError("Invalid Phone Number Format");
                }else if (!PASSWORD.matcher(inputLayoutPass.getEditText().getText().toString()).matches()){
                    inputLayoutPass.setError("Invalid Password Format");
                }else if(!inputLayoutConfirmPass.getEditText().getText().toString().trim().equals(inputLayoutPass.getEditText().getText().toString())){
                    inputLayoutConfirmPass.setError("Password didn't matched");
                    inputLayoutPass.getEditText().setText(null);
                    inputLayoutConfirmPass.getEditText().setText(null);
                    inputLayoutPass.getEditText().requestFocus();
                } else {
                    inputLayoutPass.setError(null);
                    inputLayoutPhone.setError(null);
                    inputLayoutName.setError(null);
                    inputLayoutConfirmPass.setError(null);

                    CheckConnection();
                }
            }

            private void CheckConnection() {

                if (new ConnectionDetector(UpdateActivity.this).isConnectingToInternet()) {
                   /* new*/ updateData()/*.execute()*/;
                } else {
                    new ConnectionDetector(UpdateActivity.this).connectiondetect();
                }
            }
        });
    }

    public void updateData(){

        ProgressDialog progressDialog = new ProgressDialog(UpdateActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        Call<UserDataHandler>call= RetrofitController.getInstance().getApi().updateData(
                sharedPreferences.getString(ConstantUrl.ID_KEY, null),
                inputLayoutName.getEditText().getText().toString(),
                inputLayoutPhone.getEditText().getText().toString(),
                 inputLayoutPass.getEditText().getText().toString());
        call.enqueue(new Callback<UserDataHandler>() {
            @Override
            public void onResponse(Call<UserDataHandler> call, Response<UserDataHandler> response) {
                UserDataHandler userDataHandler= response.body();
                if (response.isSuccessful()){
                    sharedPreferences.edit().putString(ConstantUrl.NAME_KEY, inputLayoutName.getEditText().getText().toString()).commit();
                    sharedPreferences.edit().putString(ConstantUrl.PHONE_KEY, inputLayoutPhone.getEditText().getText().toString()).commit();
                    sharedPreferences.edit().putString(ConstantUrl.PASS_KEY, inputLayoutPass.getEditText().getText().toString()).commit();

                    msgBox=new AlertDialog.Builder(UpdateActivity.this);
                    msgBox.setMessage(userDataHandler.message);
                    msgBox.setTitle(R.string.msgTitle);
                    msgBox.setIcon(R.mipmap.ic_launcher);
                    msgBox.setCancelable(false);
                    msgBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(UpdateActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                    msgBox.show();
                }else {
                    msgBox=new AlertDialog.Builder(UpdateActivity.this);
                    msgBox.setMessage(userDataHandler.message);
                    msgBox.setTitle(R.string.msgTitle);
                    msgBox.setIcon(R.mipmap.ic_launcher);
                    msgBox.setCancelable(false);
                    msgBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(UpdateActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                    msgBox.show();
                }
            }

            @Override
            public void onFailure(Call<UserDataHandler> call, Throwable t) {

            }
        });
    }


    public void getData(){
        inputLayoutName.getEditText().setText(sharedPreferences.getString(ConstantUrl.NAME_KEY,null));
        inputLayoutPhone.getEditText().setText(sharedPreferences.getString(ConstantUrl.PHONE_KEY,null));
        inputLayoutPass.getEditText().setText(sharedPreferences.getString(ConstantUrl.PASS_KEY,null));
        inputLayoutConfirmPass.getEditText().setText(sharedPreferences.getString(ConstantUrl.PASS_KEY,null));
    }

    @Override
    public void onBackPressed() {
    }
}