package com.example.background.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.example.background.R;
import com.example.background.Utils.ReadFile;
import com.example.background.Utils.color.*;
import com.example.background.fragment.*;
import com.example.background.module.Bill;
import com.example.background.module.FileBean;
import com.example.background.module.User;
import com.example.background.module.User_Table;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private CardView cardView;
    private String imagePath = "";
    private TextView name;
    private ImageView imageView;
    public static User user;
    private Toolbar toolbar;
    private String primaryColor, accentColor;
    private SharedPreferences sp;
    private Long mExitTime;
    private ArrayList<Bill> bills;
    private ArrayList<FileBean> files;
    private ArrayList<BaseFragment> fragments;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bills = new ArrayList<>();
        files = new ArrayList<>();
        fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new StatisticsFragment());
        fragments.add(new BillsFragment());
        fragments.add(new FileFragment());
        sp = this.getSharedPreferences("login", MODE_PRIVATE);
        user = new Select().from(User.class).where(User_Table.name.is(sp.getString("name", ""))).querySingle();
        if (user == null) startActivity(new Intent(this, LoginActivity.class));
        initToolbar();
        initView();
        new Thread(() -> {
            ReadFile.getDocumentData(this);
            bills = ReadFile.setOrdersFromFile(files);
        }).start();
    }

    public void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        cardView = findViewById(R.id.card_view);
        imageView = findViewById(R.id.user_image);
        name = findViewById(R.id.name);
        name.setText(user.name);
        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent);
        });
        initColors();
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
        replaceFragment(0);
    }

    public void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu); // 在toolbar最左边添加icon
        toolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private void initColors(){
        imagePath = user.portrait;
        if (!"".equals(imagePath)) {
            Color color = ReadColor.getPixColor(imagePath);
            String name = ReadColor.getTag(color);
            primaryColor = ReadColor.getColorString(color);
            Log.d("backgroundColor", name);
            toolbar.setBackgroundColor(Color.parseColor(primaryColor));
            RGB rgb = new RGB(color.red(), color.green(), color.blue());
            HSL hsl = Converter.RGB2HSL(rgb);
            hsl.setH(hsl.getH() + 180);
            hsl.setS(255 - hsl.getS());
            rgb = Converter.HSL2RGB(hsl);
            accentColor = rgb.toString();
            Color reversed = Color.valueOf(rgb.getRed(),rgb.getGreen(),rgb.getBlue());
            System.out.println(ReadColor.getTag(reversed));
        }
        if (imagePath != null)
            Glide.with(MainActivity.this).load(imagePath).transition(withCrossFade()).into(imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user.update();
        initColors();
    }

    public void replaceFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_view, fragments.get(index));
        transaction.addToBackStack(null);
        transaction.commit();
    }

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

    public ArrayList<Bill> getOrders() {
        return bills;
    }

    public void setOrders(ArrayList<Bill> bill) {
        this.bills = bill;
    }

    public ArrayList<FileBean> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileBean> files) {
        this.files = files;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public String getAccentColor() {
        return accentColor;
    }
}