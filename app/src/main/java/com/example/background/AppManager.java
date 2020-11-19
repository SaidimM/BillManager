package com.example.background;

import android.app.Application;
import com.raizlabs.android.dbflow.config.FlowManager;

public class AppManager extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
