package com.example.background.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.background.MyApplication;

public class SPUtils {
    public static String getImagePath(){
        SharedPreferences sharedPreferences =
                MyApplication.getInstance().getSharedPreferences("app", Context.MODE_PRIVATE);
        return sharedPreferences.getString("image_path", "");
    }

    public static void setImagePath(String path){
        SharedPreferences sharedPreferences =
                MyApplication.getInstance().getSharedPreferences("app", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("image_path", path).apply();
    }

    public static String getColorAccent(){
        SharedPreferences sharedPreferences =
                MyApplication.getInstance().getSharedPreferences("app", Context.MODE_PRIVATE);
        return sharedPreferences.getString("accent", "");
    }

    public static void setColorAccent(String accent){
        SharedPreferences sharedPreferences =
                MyApplication.getInstance().getSharedPreferences("app", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("accent", accent).apply();
    }

    public static String getColorPrimary(){
        SharedPreferences sharedPreferences =
                MyApplication.getInstance().getSharedPreferences("app", Context.MODE_PRIVATE);
        return sharedPreferences.getString("primary", "");
    }

    public static void setColorPrimary(String primary){
        SharedPreferences sharedPreferences =
                MyApplication.getInstance().getSharedPreferences("app", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("primary", primary).apply();
    }
}
