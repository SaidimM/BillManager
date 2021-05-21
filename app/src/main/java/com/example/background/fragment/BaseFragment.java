package com.example.background.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.background.activities.MainActivity;
import com.example.background.module.Bill;
import com.example.background.module.FileBean;
import com.example.background.module.User;

import java.util.ArrayList;

public abstract class BaseFragment extends Fragment {
    protected boolean canExit;
    protected Context context;
    protected Toolbar toolbar;
    protected ArrayList<FileBean> files;
    protected ArrayList<Bill> bills;
    protected String primaryColor;
    protected String accentColor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract void initView();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        toolbar = ((MainActivity)context).getToolbar();
        files = ((MainActivity)context).getFiles();
        bills = ((MainActivity)context).getOrders();
        primaryColor = ((MainActivity)context).getPrimaryColor();
        toolbar.setBackgroundColor(Color.parseColor(primaryColor));
        accentColor = ((MainActivity)context).getAccentColor();
    }
}
