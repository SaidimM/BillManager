package com.example.background.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.example.background.R;
import com.example.background.fragment.TabFragment1;
import com.example.background.module.Orders;
import com.example.background.module.User;
import com.example.background.module.User_Table;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private CardView cardView;
    private TextView name;
    private ImageView imageView;
    public static User user;
    private SharedPreferences sp;
    protected boolean canExit;
    private Long mExitTime;
    private ArrayList<Orders> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orders = new ArrayList<>();
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

    //---------------------直接在activity哩实现这个方法就可以了------------------------------------------------------------

    // 再按一次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        // 当返回键按下的时候
        if (mExitTime == null) {
            mExitTime = System.currentTimeMillis();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                getSupportFragmentManager().popBackStack();
            } else {

                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();

                } else {
                    finish();
                }
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public ArrayList<Orders> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Orders> orders) {
        this.orders = orders;
    }
}