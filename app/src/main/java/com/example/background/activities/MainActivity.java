package com.example.background.activities;

import android.content.SharedPreferences;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.background.R;
import com.example.background.Utils.OrderManage;
import com.example.background.fragment.TabFragment1;
import com.example.background.module.Orders;
import com.example.background.module.User;
import com.example.background.module.User_Table;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private CardView cardView;
    private TextView name;
    private ImageView imageView;
    public static User user;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = this.getSharedPreferences("login", MODE_PRIVATE);
        user = new Select().from(User.class).where(User_Table.name.is(sp.getString("name", ""))).querySingle();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        cardView = findViewById(R.id.card_view);
        imageView = findViewById(R.id.user_image);
        name = findViewById(R.id.name);
        name.setText(user.name);
        if (user.portrait != null)
            Glide.with(MainActivity.this).load(user.portrait).transition(withCrossFade()).into(imageView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu); // 在toolbar最左边添加icon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;
                float leftScale = 0.5f + slideOffset * 0.5f;
                drawerView.setAlpha(leftScale);
                drawerView.setScaleX(leftScale);
                drawerView.setScaleY(leftScale);
                mContent.setPivotX(0);
                mContent.setPivotY(mContent.getHeight() / 2);
                mContent.setScaleX(rightScale);
                mContent.setScaleY(rightScale);
                mContent.setTranslationX(drawerView.getWidth() * slideOffset);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                cardView.setRadius(20);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                cardView.setRadius(0);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        replaceFragment(new TabFragment1());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user.portrait != null)
            Glide.with(MainActivity.this).load(user.portrait).transition(withCrossFade()).into(imageView);
        user.save();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_view, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}