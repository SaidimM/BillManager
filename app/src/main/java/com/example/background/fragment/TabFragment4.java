package com.example.background.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.background.R;
import com.example.background.Utils.FileUtil;
import com.example.background.Utils.JxlRead;
import com.example.background.activities.MainActivity;
import com.example.background.module.FileInfo;
import com.example.background.module.Orders;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class TabFragment4 extends BaseFragment {
    private View view;
    private RecyclerView recyclerView;
    private BaseQuickAdapter<FileInfo, BaseViewHolder> adapter;
    private LottieAnimationView animationView;
    private FrameLayout frameLayout;
    private ArrayList<FileInfo> files;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                frameLayout.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            frameLayout.setVisibility(View.GONE);
                            adapter.setNewData(files);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                        }
                    });
            } else if (msg.what == 2){
                Toast.makeText(context, "orders loaded", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab4, container, false);
        files = new ArrayList<>();
        initView();
        initRecycler();
        return view;
    }

    public void initView() {
        frameLayout = view.findViewById(R.id.animation_container);
        animationView = view.findViewById(R.id.animation_view);
        recyclerView = view.findViewById(R.id.recycler);
        animationView.setAnimation(R.raw.anim_search_file);
        animationView.playAnimation();

        new Thread(this::getFolderData).start();
    }

    private void initRecycler() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BaseQuickAdapter<FileInfo, BaseViewHolder>(R.layout.item_file) {
            @Override
            protected void convert(BaseViewHolder helper, FileInfo item) {
                if (!item.isSelect()) helper.getView(R.id.item_card).setAlpha(0.7f);
                helper.setText(R.id.tv_name, item.getFileName());
                helper.setText(R.id.tv_size, FileUtil.FormatFileSize(item.getFileSize()));
                helper.setText(R.id.tv_time, item.getTime());
            }
        };
        adapter.setOnItemClickListener((adapter, view, position) -> {
            new Thread(){
                @Override
                public void run(){
                    FileInfo file = (FileInfo) adapter.getItem(position);
                    if (file == null) return;
                    readFile(file.getFilePath());
                }
            }.start();
            view.findViewById(R.id.item_card)
                    .animate().alpha(1).setDuration(500).setInterpolator(
                            new AccelerateDecelerateInterpolator(getContext(),null));
        });
        recyclerView.setAdapter(adapter);
    }

    public void readFile(String path){
        ArrayList<Orders> orders = JxlRead.readCsvFile(path);
        for (Orders order : orders){
            if (!((MainActivity)context).getOrders().contains(order))
                ((MainActivity)context).getOrders().add(order);
        }
        handler.sendEmptyMessage(2);
    }

    /**
     * 遍历文件夹中资源
     */
    public void getFolderData() {
        getDocumentData();
        handler.sendEmptyMessage(1);
    }

    /**
     * 获取手机文档数据
     *
     * @param
     */
    public void getDocumentData() {
        String[] columns = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.DATA};
        String select = "(_data LIKE '%.csv')";
        ContentResolver contentResolver = Objects.requireNonNull(getContext()).getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Files.getContentUri("external"), columns, select, null, null);
        int columnIndexOrThrow_DATA = 0;
        if (cursor != null) {
            columnIndexOrThrow_DATA = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(columnIndexOrThrow_DATA);
                FileInfo document = FileUtil.getFileInfoFromFile(new File(path));
                if (document.getFileName().contains("alipay")
                        || document.getFileName().contains("微信"))
                    files.add(document);
                Log.d(TAG, " csv " + document);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }
}
