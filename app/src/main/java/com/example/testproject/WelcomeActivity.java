package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    Handler handler;
    ImageView imgView;
    TextView textView;
    AlphaAnimation alphaAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        imgView=findViewById(R.id.imgWelcome);
        textView= findViewById(R.id.TV_welcome);

        alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2500);
        imgView.startAnimation(alphaAnimation);
        textView.startAnimation(alphaAnimation);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            }
        },2600);
    }
}