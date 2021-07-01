package com.example.background.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.background.R;
import com.example.background.Utils.FileUtil;
import com.example.background.Utils.ReadFile;
import com.example.background.Utils.SPUtils;
import com.example.background.Utils.color.Converter;
import com.example.background.Utils.color.HSL;
import com.example.background.Utils.color.RGB;
import com.example.background.Utils.color.ReadColor;

import java.io.File;
import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class UserActivity extends AppCompatActivity {
    public static final int RC_CHOOSE_PHOTO = 2;
    public static final int RC_TAKE_PHOTO = 1;
    private ConstraintLayout userLayout;
    private String filePath = "";
    private ArrayList<String> colors;
    private ImageView userImage;
    private RecyclerView colorsRv;
    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        colors = new ArrayList<>();
        userImage = findViewById(R.id.userImage);
        colorsRv = findViewById(R.id.colors);
        Glide.with(UserActivity.this)
                .load(SPUtils.getImagePath())
                .apply(RequestOptions.placeholderOf(R.drawable.main_back_pic))
                .into(userImage);
        userLayout = findViewById(R.id.userLayout);
        userImage.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
                ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_CHOOSE_PHOTO);
            } else {
                //已授权，获取照片
                choosePhoto();
            }
        });
        getColors();
        initRecycler();
        userLayout.setOnClickListener(view -> {
            if (filePath == null) finish();
            if (!filePath.equals("") ) {
                SPUtils.setImagePath(filePath);
            }
            finish();
        });
    }

    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_CHOOSE_PHOTO) {
            if (data == null) return;
            Uri uri = data.getData();
            filePath = FileUtil.getFilePathByUri(this, uri);
            if (!TextUtils.isEmpty(filePath)) {
                SPUtils.setImagePath(filePath);
                //将照片显示在 ivImage上
                Glide.with(UserActivity.this).load(filePath)
                        .apply(RequestOptions.placeholderOf(R.drawable.main_back_pic))
                        .transition(withCrossFade()).into(userImage);
                getColors();
            }
        }
    }

    /**
     * 权限申请结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_TAKE_PHOTO:   //拍照权限申请返回
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
            case RC_CHOOSE_PHOTO:   //相册选择照片权限申请返回
                choosePhoto();
                break;
        }
    }

    private void initRecycler(){
        colorsRv.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_color) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.getView(R.id.content).setBackgroundColor(Color.parseColor(item));
            }
        };
        colorsRv.setItemAnimator(new DefaultItemAnimator());
        colorsRv.setAdapter(adapter);
    }

    private void getColors(){
        Bitmap bitmap = ReadColor.getBitmap(SPUtils.getImagePath());
        new Thread(() -> {
            colors = ReadColor.getColors(bitmap);
            handler.sendEmptyMessage(0);
        }).start();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0 : adapter.setNewData(colors);
            }
            return true;
        }
    });
}