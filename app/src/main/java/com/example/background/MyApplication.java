package com.example.background;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import com.raizlabs.android.dbflow.config.FlowManager;

public class MyApplication extends Application {
    public String APP_NAME;
    public boolean isDebug;

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FlowManager.init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }
}
