package com.example.background.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.background.R;
import com.example.background.Utils.FileUtil;
import com.example.background.Utils.ReadFile;
import com.example.background.activities.MainActivity;
import com.example.background.module.Bill;
import com.example.background.module.FileBean;

import java.util.ArrayList;

public class FileFragment extends BaseFragment {
    private View view;
    private RecyclerView recyclerView;
    private BaseQuickAdapter<FileBean, BaseViewHolder> adapter;
    private LottieAnimationView animationView;
    private FrameLayout frameLayout;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                Toast.makeText(context, "bill loaded", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                Toast.makeText(context, "bill refreshed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab4, container, false);
        initView();
        initRecycler();
        adapter.setNewData(((MainActivity) context).getFiles());
        recyclerView.animate().alpha(1).setDuration(500).start();
        return view;
    }

    public void initView() {
        toolbar.setBackgroundColor(Color.parseColor(primaryColor));
        frameLayout = view.findViewById(R.id.animation_container);
        animationView = view.findViewById(R.id.animation_view);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAlpha(0);
        toolbar.setTitle(files.size() + " items were found");
    }

    private void initRecycler() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BaseQuickAdapter<FileBean, BaseViewHolder>(R.layout.item_file) {
            @Override
            protected void convert(BaseViewHolder helper, FileBean item) {
                if (!item.getIsSelect()) helper.getView(R.id.item_card).setAlpha(0.6f);
                else helper.getView(R.id.item_card).setAlpha(1.0f);
                helper.setText(R.id.tv_name, item.getFileName());
                helper.setText(R.id.tv_size, FileUtil.FormatFileSize(item.getFileSize()));
                helper.setText(R.id.tv_time, item.getTime());
            }
        };
        adapter.setOnItemClickListener((adapter, view, position) -> {
            FileBean file = (FileBean) adapter.getItem(position);
            if (file == null) return;
            if (!file.getIsSelect()) {
                file.setIsSelect(true);
                new Thread(() -> {
                    FileBean file1 = (FileBean) adapter.getItem(position);
                    if (file1 == null) return;
                    readFile(file1.getFilePath());
                }).start();
                view.setTranslationZ(4);
                view.findViewById(R.id.item_card)
                        .animate().alpha(1).setDuration(500).setInterpolator(
                        new AccelerateDecelerateInterpolator(getContext(), null));
            } else {
                file.setIsSelect(false);
                new Thread(() -> {
                    ((MainActivity) context).setOrders(ReadFile.setOrdersFromFile(((MainActivity) context).getFiles()));
                    handler.sendEmptyMessage(3);
                }).start();
                view.setTranslationZ(2);
                view.findViewById(R.id.item_card)
                        .animate().alpha(0.6f).setDuration(500).setInterpolator(
                        new AccelerateDecelerateInterpolator(getContext(), null));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void readFile(String path) {
        ArrayList<Bill> bills = ReadFile.readCsvFile(path);
        for (Bill bill : bills) {
            if (!((MainActivity) context).getOrders().contains(bill))
                ((MainActivity) context).getOrders().add(bill);
        }
        handler.sendEmptyMessage(2);
    }

}
