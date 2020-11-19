package com.example.background.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.background.R;
import com.example.background.Utils.FileUtil;
import com.example.background.Utils.JxlRead;
import com.example.background.activities.PickActivity;

import java.io.File;
import java.util.Objects;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class TabFragment4 extends Fragment {
    private View view;
    private Button save, pick;
    private final int REQUEST_CODE = 101;
    private String path;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab4, container, false);
        save = view.findViewById(R.id.save);
        pick = view.findViewById(R.id.pick);
        initView();
        return view;
    }

    private void initView(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.
                        DIRECTORY_DOWNLOADS)+"/order.xls");
                JxlRead.xls2String(file);
            }
        });
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PickActivity.class));
            }
        });
    }

}
