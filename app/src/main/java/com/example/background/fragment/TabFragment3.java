package com.example.background.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.background.R;
import com.example.background.Utils.OrderManage;
import com.example.background.activities.MainActivity;
import com.example.background.module.Orders;
import com.example.background.module.Orders_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class TabFragment3 extends Fragment {
    private View view;
    private RecyclerView recycler;
    private ImageButton sort;
    private EditText search;
    private BaseQuickAdapter<Orders,BaseViewHolder> mAdapter;
    private OrderManage orderManage;
    private Calendar calendar, tempCalendar;
    private TimeZone zone = TimeZone.getTimeZone("GMT");
    private SimpleDateFormat format;
    private Date date;
    private PopupWindow popup;
    private int type = -1;

    private final int[] colors = {
            Color.parseColor("#FFA500"),
            Color.parseColor("#008000"),
            Color.parseColor("#1D5795"),
            Color.parseColor("#FFB6C1"),
            Color.parseColor("#CD5C5C"),
            Color.parseColor("#778899"),
            Color.parseColor("#4682B4"),
            Color.parseColor("#FFFFFF")
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_tab3,container,false);
        calendar = new GregorianCalendar();
        tempCalendar = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
        format.setTimeZone(zone);
        initView();
        return view;
    }
    private void initView(){
        orderManage = new OrderManage();
        recycler = view.findViewById(R.id.recycler);
        sort = view.findViewById(R.id.sort);
        search = view.findViewById(R.id.search);
        initRecycler();
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSortDialog();
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                mAdapter.setNewData(orderManage.getWordSortList(textView.getText().toString()));
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void initRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        recycler.setNestedScrollingEnabled(false);
        mAdapter = new BaseQuickAdapter<Orders, BaseViewHolder>(R.layout.item_orders, orderManage.getTotalOrders()) {
            @Override
            protected void convert(BaseViewHolder helper, Orders item) {
                helper.setBackgroundColor(R.id.color, colors[item.type]);
                helper.setText(R.id.name, item.name);
                helper.setText(R.id.dealer, item.dealer);
                helper.setText(R.id.time, item.time);
                helper.setText(R.id.cash, item.cash + "");
            }
        };
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                initPopupWindow((Orders)adapter.getData().get(position),position);
                if(!popup.isShowing()) {
                    popup.showAtLocation(view, Gravity.CENTER,view.getScrollX(),view.getScrollY());
                }
                return true;
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recycler.setAdapter(mAdapter);
    }

    private void initPopupWindow(final Orders order, final int position) {
        View v = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(
                R.layout.menu_de_select, null);
        popup = new PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT , true);
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLite.delete(Orders.class)
                        .where(Orders_Table.id.eq(order.id))
                        .execute();
                Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
                orderManage = new OrderManage();
                mAdapter.remove(position);
                mAdapter.notifyItemRemoved(position);//刷新被删除的地方
                mAdapter.notifyItemRangeChanged(position,mAdapter.getItemCount()); //刷新被删除数据，以及其后面的数据
                popup.dismiss();
            }
        });
        v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initEditDialog(order);
                popup.dismiss();
            }
        });
        v.findViewById(R.id.linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });
        popup.setFocusable(true);
        //该属性设置为true则你在点击屏幕的空白位置也会退出
        popup.setTouchable(true);
        popup.setOutsideTouchable(true);
        popup.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#AAFFFFFF")));
        popup.setAnimationStyle(R.style.PopupAnimation);
    }

    private void initEditDialog(final Orders item) {
        final AlertDialog customizeDialog = new AlertDialog.Builder(getContext()).create();
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_main,null);
        customizeDialog.setTitle("edit order");
        customizeDialog.setView(dialogView);
        final DatePicker datePicker = dialogView.findViewById(R.id.date);
        final TimePicker timePicker = dialogView.findViewById(R.id.time);
        final EditText ed_name = dialogView.findViewById(R.id.name);
        final EditText ed_cost = dialogView.findViewById(R.id.cost);
        final EditText ed_dealer = dialogView.findViewById(R.id.dealer);
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radio_group);
        ((TextView)dialogView.findViewById(R.id.tv_name)).setText("name");
        ((TextView)dialogView.findViewById(R.id.tv_cost)).setText("cost");
        ((TextView)dialogView.findViewById(R.id.tv_dealer)).setText("dealer");
        ((TextView)dialogView.findViewById(R.id.tv_type)).setText("type");
        ((TextView)dialogView.findViewById(R.id.tv_date)).setText("date");
        ((TextView)dialogView.findViewById(R.id.tv_time)).setText("time");
        ((Button)dialogView.findViewById(R.id.positive)).setText("confirm");
        ed_name.setText(item.name);
        ed_cost.setText((new BigDecimal(item.cash)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"");
        ed_dealer.setText(item.dealer);
        radioGroup.check(radios[item.type]);
        type = item.type;
        for(int i=0;i<8;i++){
            final int finalI = i;
            ((RadioButton)dialogView.findViewById(radios[i])).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    for(int j = 0;j < 8;j++) ((RadioButton) radioGroup.findViewById(radios[j])).setChecked(false);
                    ((RadioButton) radioGroup.findViewById(radios[finalI])).setChecked(type != finalI);
                    type = finalI;
                }
            });
        }
        dialogView.findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, cost, date, time, dealer;
                name = ed_name.getText().toString();
                cost = ed_cost.getText().toString();
                dealer = ed_dealer.getText().toString();
                date = datePicker.getYear() + "-" + ((Integer)datePicker.getMonth()+1) + "-" + datePicker.getDayOfMonth();
                time = timePicker.getHour() + ":" + timePicker.getMinute() +":"+ calendar.get(Calendar.SECOND);
                if(!name.equals("")&&!cost.equals("")){
                    Orders product = SQLite.select()
                            .from(Orders.class)
                            .where(Orders_Table.id.eq(item.id))
                            .querySingle();//区别于queryList(),返回的是实体
                    if (product != null) {
                        product.name = name;
                        product.cash = Float.parseFloat(cost);
                        product.time = date + " " + time;
                        product.dealer = dealer;
                        product.type = type;
                        product.update();
                    }
                    orderManage = new OrderManage();
                    mAdapter.setNewData(orderManage.getTotalOrders());
                    mAdapter.notifyDataSetChanged();
                    customizeDialog.dismiss();
                }
            }
        });
        ((Button)dialogView.findViewById(R.id.negative)).setText("cancel");
        dialogView.findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customizeDialog.dismiss();
            }
        });
        customizeDialog.show();
    }

    private void initSortDialog() {
        type = -1;
        final AlertDialog customizeDialog = new AlertDialog.Builder(getContext()).create();
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sort,null);
        customizeDialog.setTitle("sort");
        customizeDialog.setView(dialogView);
        final DatePicker dateStart = dialogView.findViewById(R.id.date_start);
        final DatePicker dateEnd = dialogView.findViewById(R.id.date_end);
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radio_group);
        for(int i=0;i<8;i++){
            final int finalI = i;
            ((RadioButton)dialogView.findViewById(radios[i])).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    for(int j = 0;j < 8;j++) ((RadioButton) radioGroup.findViewById(radios[j])).setChecked(false);
                    ((RadioButton) radioGroup.findViewById(radios[finalI])).setChecked(type != finalI);
                    type = finalI;
                }
            });
        }
//        Calendar temp = new GregorianCalendar(dateStart.getYear(),dateStart.getMonth(),dateStart.getDayOfMonth());
//        dateEnd.setMinDate(temp.getTimeInMillis());
        ((Button)dialogView.findViewById(R.id.positive)).setText("yes");
        dialogView.findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar1 = new GregorianCalendar(dateStart.getYear(),dateStart.getMonth(),dateStart.getDayOfMonth());
                Calendar calendar2 = new GregorianCalendar(dateEnd.getYear(),dateEnd.getMonth(),dateEnd.getDayOfMonth());
                List<Orders> sorted = orderManage.getSortList(type,calendar1,calendar2);
                 mAdapter.setNewData(sorted);
                 mAdapter.notifyDataSetChanged();
                customizeDialog.dismiss();
            }
        });
        ((Button)dialogView.findViewById(R.id.negative)).setText("cancel");
        dialogView.findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customizeDialog.dismiss();
            }
        });
        customizeDialog.show();
    }
}
