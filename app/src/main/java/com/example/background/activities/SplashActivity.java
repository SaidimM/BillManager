package com.example.background.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.example.background.R;
import com.example.background.Utils.SPUtils;
import com.example.background.Utils.color.Converter;
import com.example.background.Utils.color.HSL;
import com.example.background.Utils.color.RGB;
import com.example.background.Utils.color.ReadColor;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private String imagePath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();
        imagePath = SPUtils.getImagePath();
        LottieAnimationView animationView = findViewById(R.id.animation_view);
        animationView.setAnimation(R.raw.animation_wallet);
        animationView.playAnimation();
        animationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //之前登录过，直接进入主界面
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
