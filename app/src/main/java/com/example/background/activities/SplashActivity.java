package com.example.background.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sp = this.getSharedPreferences("login",MODE_PRIVATE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                //之前登录过，直接进入主界面
                if (sp.getBoolean("isLogged",false)){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
