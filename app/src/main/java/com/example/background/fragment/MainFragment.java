package com.example.background.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.background.R;
import com.example.background.Utils.BillManage;
import com.example.background.Utils.SPUtils;
import com.example.background.activities.MainActivity;
import com.example.background.module.Bill;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MainFragment extends BaseFragment {
    private AddFloatingActionButton add;
    private View view;
    private TextView month;
    private TextView cost;
    private TextView day;
    private RecyclerView recyclerView;
    private Calendar calendar, tempCalendar;
    private ImageView imageView;
    private BaseQuickAdapter<Bill, BaseViewHolder> mAdapter;
    private Date date;
    private int type;
    private RadioGroup radioGroup;
    private TimeZone zone = TimeZone.getTimeZone("GMT");
    private SimpleDateFormat format;
    private BillManage billManage;
    private final String[] months = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    };
    private final String[] daysOfWeek = {
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
    };

    private final int[] colors = {
            R.color.orange,
            R.color.green,
            R.color.mediumblue,
            R.color.lightpink,
            R.color.indianred,
            R.color.lightslategray,
            R.color.steelblue
    };
    private final int[] radios = {
            R.id.food,
            R.id.travel,
            R.id.bills,
            R.id.outdoor,
            R.id.edu,
            R.id.clothes,
            R.id.health,
            R.id.rests
    };

    private static final String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};    //请求状态码

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, container, false);
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            int REQUEST_PERMISSION_CODE = 2;
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
        }
        initView();
        initData();
        return view;
    }

    public void initView() {
        imageView = view.findViewById(R.id.image);
        month = view.findViewById(R.id.month);
        cost = view.findViewById(R.id.cost);
        day = view.findViewById(R.id.day);
        recyclerView = view.findViewById(R.id.list);
        add = view.findViewById(R.id.add);
        add.setOnClickListener(view -> initDialog());
    }

    public void initData() {
        Glide.with(this).load(SPUtils.getImagePath()).transition(withCrossFade()).into(imageView);
        billManage = new BillManage(bills);
        calendar = new GregorianCalendar();
        format = new SimpleDateFormat("yyyy-M-d HH:mm:ss", Locale.CHINA);
        format.setTimeZone(zone);
        month.setText(months[calendar.get(Calendar.MONTH)]);
        cost.setText("Total cost  ¥ " + getCost(billManage.getMonthOrders()));
        day.setText(daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new BaseQuickAdapter<Bill, BaseViewHolder>(R.layout.item_main_rv, billManage.getWeekOrders()) {
            @Override
            protected void convert(BaseViewHolder helper, Bill item) {
                helper.setText(R.id.content, item.getName());
                helper.setText(R.id.cash, "¥ " + (BigDecimal.valueOf(item.getCash())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
                helper.setBackgroundRes(R.id.color, colors[item.getType()]);
                try {
                    date = format.parse(item.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert date != null;
                tempCalendar = Calendar.getInstance();
                tempCalendar.setTime(date);
                helper.setText(R.id.day_of_week, calendar.get(Calendar.DAY_OF_MONTH) ==
                        tempCalendar.get(Calendar.DAY_OF_MONTH) ? "today"
                        : daysOfWeek[tempCalendar.get(Calendar.DAY_OF_WEEK) - 1]);
            }
        };
        //一行代码开启动画 默认CUSTOM动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setAdapter(mAdapter);
    }

    public void initToolbar(){
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("");
    }

    private void initDialog() {
        final AlertDialog customizeDialog = new AlertDialog.Builder(getContext()).create();
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_main, null);
        customizeDialog.setTitle("add new order");
        customizeDialog.setView(dialogView);
        ((TextView) dialogView.findViewById(R.id.tv_name)).setText("name");
        ((TextView) dialogView.findViewById(R.id.tv_cost)).setText("cost");
        ((TextView) dialogView.findViewById(R.id.tv_dealer)).setText("dealer");
        ((TextView) dialogView.findViewById(R.id.tv_type)).setText("type");
        ((TextView) dialogView.findViewById(R.id.tv_date)).setText("date");
        ((TextView) dialogView.findViewById(R.id.tv_time)).setText("time");
        ((Button) dialogView.findViewById(R.id.positive)).setText("confirm");
        radioGroup = dialogView.findViewById(R.id.radio_group);
        for (int i = 0; i < 8; i++) {
            final int finalI = i;
            ((RadioButton) dialogView.findViewById(radios[i])).setOnCheckedChangeListener((compoundButton, b) -> {
                for (int j = 0; j < 8; j++) ((RadioButton) radioGroup.findViewById(radios[j])).setChecked(false);
                ((RadioButton) radioGroup.findViewById(radios[finalI])).setChecked(type != finalI);
                type = finalI;
            });
        }

        dialogView.findViewById(R.id.positive).setOnClickListener(view -> {
            Bill order = new Bill();
            DatePicker datePicker = dialogView.findViewById(R.id.date);
            TimePicker timePicker = dialogView.findViewById(R.id.time);
            String name, cost, date, time, dealer;
            name = ((EditText) dialogView.findViewById(R.id.name)).getText().toString();
            cost = ((EditText) dialogView.findViewById(R.id.cost)).getText().toString();
            dealer = ((EditText) dialogView.findViewById(R.id.dealer)).getText().toString();
            date = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
            time = timePicker.getHour() + ":" + timePicker.getMinute() + ":" + calendar.get(Calendar.SECOND);
            if (!name.equals("") && !cost.equals("")) {
                order.setName(name);
                order.setCash(Float.parseFloat(cost));
                order.setTime(date + " " + time);
                order.setDealer(dealer);
                order.setType(type);
                billManage = new BillManage(bills);
                mAdapter.setNewData(billManage.getWeekOrders());
                mAdapter.notifyDataSetChanged();
                customizeDialog.dismiss();
            }
        });
        ((Button) dialogView.findViewById(R.id.negative)).setText(getString(R.string.cancel));
        dialogView.findViewById(R.id.negative).setOnClickListener(view -> customizeDialog.dismiss());
        customizeDialog.show();
    }

    private String getCost(List<Bill> list) {
        float sum = 0;
        for (Bill bill : list) sum += bill.getCash();
        return (new BigDecimal(sum)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onResume() {
        super.onResume();
        initToolbar();
        add.setColorNormal(Color.parseColor(accentColor));
        Glide.with(this).load(SPUtils.getImagePath()).transition(withCrossFade()).into(imageView);
    }
}
