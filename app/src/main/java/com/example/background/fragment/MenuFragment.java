package com.example.background.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.example.background.R;
import com.example.background.activities.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuFragment extends Fragment {

    public ListView mListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View navView = inflater.inflate(R.layout.activity_menu, container, false);
        mListView = (ListView) navView.findViewById(R.id.menu_list_view);
        mListView.setDivider(null); // 去掉分割线
        initListView();
        clickEvents();

        return navView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initListView() {
        String[] data_zh = getResources().getStringArray(R.array.menu_zh);
        String[] data_en = getResources().getStringArray(R.array.menu_en);

        List<Map<String, Object>> listTest = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < data_zh.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("zh", data_zh[i]);
            listItem.put("en", data_en[i]);
            listTest.add(listItem);
        }

        // 创建SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), listTest, R.layout.item_list_array,
                new String[]{"zh", "en"}, new int[]{R.id.item_zh, R.id.item_en});

        // 为listview设置适配器
        mListView.setAdapter(simpleAdapter);
    }

    public void clickEvents() {
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            MainActivity activity = (MainActivity) getActivity();
            assert activity != null;
            DrawerLayout mDrawerLayout = activity.findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawer(Gravity.START, true);
            activity.replaceFragment(position);
        });
    }
}
