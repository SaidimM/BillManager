package com.example.background.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.background.R;
import com.example.background.Utils.BillManage;
import com.example.background.Utils.FileUtil;
import com.example.background.Utils.ReadFile;
import com.example.background.Utils.ZipUtil;
import com.example.background.activities.MainActivity;
import com.example.background.module.Bill;
import com.example.background.module.FileBean;

import java.util.ArrayList;
import java.util.Calendar;

public class FileFragment extends BaseFragment {
    private View view;
    private RecyclerView recyclerView;
    private BaseQuickAdapter<FileBean, BaseViewHolder> adapter;
    private LottieAnimationView animationView;
    private FrameLayout frameLayout;
    private int fileIndex = 0;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(msg -> {
        if (msg.what == 2) {
            Toast.makeText(context, "bill loaded", Toast.LENGTH_SHORT).show();
        } else if (msg.what == 3) {
            Toast.makeText(context, "bill refreshed", Toast.LENGTH_SHORT).show();
        } else if (msg.what == 4){
            ReadFile.getDocumentData(context);
            adapter.notifyDataSetChanged();
        } else Toast.makeText(context, "wrong password!", Toast.LENGTH_SHORT).show();
        return true;
    });

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
                if (!item.getIsSelect()) helper.getView(R.id.item_card).setAlpha(0.7f);
                else helper.getView(R.id.item_card).setAlpha(1.0f);
                ImageView file = helper.getView(R.id.img);
                file.setBackground(null);
                if (item.getType() == 0) Glide.with(context).load(R.drawable.ic_zip).into(file);
                else Glide.with(context).load(R.drawable.ic_csv).into(file);
                helper.setText(R.id.tv_name, item.getFileName());
                helper.setText(R.id.tv_size, FileUtil.FormatFileSize(item.getFileSize()));
                helper.setText(R.id.tv_time, item.getTime());
            }
        };
        adapter.setOnItemClickListener((adapter, view, position) -> {
            FileBean file = (FileBean) adapter.getItem(position);
            if (file == null) return;
            if (file.getType() == FileBean.ZIP_FILE){
                fileIndex = position;
                initDialog((FileBean) adapter.getItem(position));
                return;
            }
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
    
    private void initDialog(FileBean file) {
        
        new MaterialDialog.Builder(context)
                .title("Password")
                .content("input password to unzip the file")
//                .widgetColor(Color.BLUE)//输入框光标的颜色
                .inputType(InputType.TYPE_CLASS_PHONE)//可以输入的类型-电话号码
                //前2个一个是hint一个是预输入的文字
                .input("","", (dialog, input) -> {
                    Log.i("yqy", "输入的是：" + input);
                    
                })
                .onPositive((dialog, which) -> {
                    if (dialog.getInputEditText() == null) return;
                    String pass = dialog.getInputEditText().getText().toString();
                    String path = file.getFilePath();
                    new Thread(() -> {
                        int result = ZipUtil.extract(path,pass, path.substring(0, path.lastIndexOf('/')));
                        ReadFile.getDocumentData(context);
                        if (result == 0) handler.sendEmptyMessage(0);
                        else handler.sendEmptyMessage(4);
                    }).start();
                }).show();
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
