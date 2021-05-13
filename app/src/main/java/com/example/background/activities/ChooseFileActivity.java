package com.example.background.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.background.R;
import com.example.background.Utils.FileUtil;
import com.example.background.module.FileInfo;

import java.io.File;
import java.util.ArrayList;

public class ChooseFileActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ProgressDialog progressDialog;
    private BaseQuickAdapter<FileInfo, BaseViewHolder> pdfAdapter;
    private ArrayList<FileInfo> pdfData = new ArrayList<>();
    private String TAG = ChooseFileActivity.class.getSimpleName();


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initRecyclerView();
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);
        initViews();
        initListener();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.rv_pdf);

        showDialog();

        new Thread() {
            @Override
            public void run() {
                super.run();
                getFolderData();
            }
        }.start();
    }

    private void initListener() {

    }

    private void showDialog() {
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage("正在加载数据中...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }


    private void initRecyclerView() {
        pdfAdapter = new BaseQuickAdapter<FileInfo, BaseViewHolder>(R.layout.item_file) {

            @Override
            protected void convert(BaseViewHolder helper, FileInfo item) {
                if (item == null) {
                    return;
                }
                helper.setText(R.id.tv_name, item.getFileName());
                helper.setText(R.id.tv_size, FileUtil.FormatFileSize(item.getFileSize()));
                helper.setText(R.id.tv_time, item.getTime());
            }
        };
        View notDataView = getLayoutInflater().inflate(R.layout.pdf_empty_view, (ViewGroup) mRecyclerView.getParent(), false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(pdfAdapter);

        pdfAdapter.setOnItemClickListener((adapter, view, position) -> {

            if (!pdfData.get(position).isSelect()) {
                if (getSelectNumber() >= 3) {
                    Toast.makeText(ChooseFileActivity.this, "最多可选择3个文件", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            for (int i = 0; i < pdfData.size(); i++) {
                if (i == position) {
                    if (pdfData.get(i).isSelect()) {
                        pdfData.get(i).setSelect(false);
                    } else {
                        pdfData.get(i).setSelect(true);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        });
        if (pdfData != null && pdfData.size() > 0) {

            for (int i = 0; i < pdfData.size(); i++) {
                pdfData.get(i).setSelect(false);
            }
            pdfAdapter.setNewData(pdfData);
        } else {
            pdfAdapter.setEmptyView(notDataView);
        }
        progressDialog.dismiss();
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
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Files.getContentUri("external"), columns, select, null, null);
        int columnIndexOrThrow_DATA = 0;
        if (cursor != null) {
            columnIndexOrThrow_DATA = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(columnIndexOrThrow_DATA);
                FileInfo document = FileUtil.getFileInfoFromFile(new File(path));
                pdfData.add(document);
                Log.d(TAG, " csv " + document);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }
    private int getSelectNumber() {
        int k = 0;
        for (int i = 0; i < pdfData.size(); i++) {
            if (pdfData.get(i).isSelect()) {
                k++;
            }
        }
        return k;
    }
}