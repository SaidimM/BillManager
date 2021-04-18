package com.example.background.activities;

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
import com.example.background.fragment.TabFragment1;
import com.example.background.fragment.TabFragment2;
import com.example.background.fragment.TabFragment3;
import com.example.background.fragment.TabFragment4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuFragment extends Fragment {

    private ListView mListView;


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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity activity = (MainActivity) getActivity();
                assert activity != null;
                DrawerLayout mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawer(Gravity.START, true);
                switch (position) {
                    case 0:
                        activity.replaceFragment(new TabFragment1());
                        break;
                    case 1:
                        activity.replaceFragment(new TabFragment2());
                        break;
                    case 2:
                        activity.replaceFragment(new TabFragment3());
                        break;
                    case 3:
                    case 4:
                        activity.replaceFragment(new TabFragment4());
                        break;
                    default:
                        break;
                }
            }
        });

    }
}
