package com.example.background.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.background.R;
import com.example.background.Utils.BillManage;
import com.example.background.module.Bill;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.*;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

import java.math.BigDecimal;
import java.util.*;


public class StatisticsFragment extends BaseFragment {
    private View view;
    private PieChartView pieChart;
    private LineChartView lineChart;
    private List<SliceValue> sliceList = new ArrayList<>();
    private RecyclerView typeList, orderList;
    private BillManage billManage;
    private PopupWindow popup;
    private Calendar calendar;
    private int numOfMonth;
    private String costOfMonth;

    String[] date = {"1~7", "8~14", "14~21", "22~28", "29~"};//X轴的数据
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

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
    private static final String[] types = {
            "food",
            "trip",
            "top-up",
            "outdoor",
            "edu",
            "clothes",
            "health",
            "else"
    };
    private static final String[] monthsName = {
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

    private static final List<Integer> monthList = Arrays.asList
            (R.id.January,
                    R.id.February,
                    R.id.March,
                    R.id.April,
                    R.id.May,
                    R.id.June,
                    R.id.July,
                    R.id.August,
                    R.id.September,
                    R.id.October,
                    R.id.November,
                    R.id.December);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab2, container, false);
        calendar = new GregorianCalendar();
        initView();
        return view;
    }

    @Override
    public void initView() {

        typeList = view.findViewById(R.id.list_type);
        orderList = view.findViewById(R.id.bill);
        pieChart = view.findViewById(R.id.pie);
        lineChart = view.findViewById(R.id.line);
        billManage = new BillManage(bills);
        numOfMonth = calendar.get(Calendar.MONTH);
        toolbar.setTitle(monthsName[numOfMonth]);

        List<Bill> list = billManage.getMonthOrders();
        getAxisXLables();
        getAxisPoints(list);
        initLineChart(list);
        initPieChart(list);
        initRecycler(list);
        initPopupWindow();
        toolbar.setOnClickListener(v -> {
            if (!popup.isShowing()) {
                popup.showAtLocation(toolbar, Gravity.CENTER, 0, 0);
            }
        });
    }

    private void initData(int month) {
        toolbar.setTitle(monthsName[numOfMonth]);
        billManage = new BillManage(bills,month);
        List<Bill> list = billManage.getMonthOrders();
        getAxisXLables();
        getAxisPoints(list);
        initLineChart(list);
        initPieChart(list);
        initRecycler(list);
    }

    private void getAxisXLables() {
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    private void getAxisPoints(List<Bill> list) {
        mPointValues = new ArrayList<>();
        float[] score = billManage.getScoreList(list);
        for (int i = 0; i < score.length; i++) {
            // 构造函数传参 位置 值
            mPointValues.add(new PointValue(i, score[i]));
        }
    }

    private void initLineChart(List<Bill> list) {
        // 折线对象 传参数为点的集合对象   y轴值的范围自动生成 根据坐标的y值 不必自己准备数据
        Line line = new Line(mPointValues).setColor(Color.parseColor("#008080"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部  （顶部底部一旦设置就意味着x轴）
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(false); //x 轴分割线  每个x轴上 面有个虚线 与x轴垂直

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
//        axisY.setName("这是Y轴");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(Color.GRAY);
        data.setAxisYLeft(axisY);  //Y轴设置在左边（左面右面一旦设定就意味着y轴）
        //data.setAxisYRight(axisY);  //y轴设置在右边

        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setZoomEnabled(true);
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        //设置触摸事件
        lineChart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                Toast.makeText(getActivity(), "" + value.getY(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {

            }
        });//为图表设置值得触摸事件
        lineChart.setVisibility(View.VISIBLE);
        //注：下面的7，10只是代表一个数字去类比而已
        //当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
        //
        // 下面的这个api控制 滑动
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
//        v.bottom= 1;
//        v.top= 30;
        lineChart.setCurrentViewport(v);
    }

    private void initPieChart(List<Bill> list) {
        pieChart.setViewportCalculationEnabled(true);//设置饼图自动适应大小
        pieChart.setChartRotation(360, true);//设置饼图旋转角度，且是否为动画
        pieChart.setChartRotationEnabled(false);//设置饼图是否可以手动旋转
        pieChart.setCircleFillRatio(1);//设置饼图其中的比例
//        pieChart.setCircleOval();//设置饼图成椭圆形
        PieChartData pd = new PieChartData();//实例化PieChartData对象
        pd.setHasLabelsOutside(true);//设置饼图外面是否显示值
        pd.setHasCenterCircle(true);//设置饼图中间是否有第二个圈
        pd.setCenterCircleColor(Color.WHITE);//设置饼图中间圈的颜色
        pd.setCenterCircleScale((float) 0.75);////设置第二个圈的大小比例
        pd.setCenterText1(billManage.getCostOfMonth());//设置文本
        pd.setCenterText1Color(Color.GRAY);//设置文本颜色
        pd.setCenterText1FontSize(20);//设置文本大小
//        pd.setCenterText1Typeface();//设置文本字体
//        pd.setCenterText2(String centerText2);//设置第二个圈文本
//        pd.setCenterText2Color(int centerText2Color);//设置第二个圈文本颜色
//        pd.setCenterText2Typeface(Typeface text2Typeface);//设置第二个圈文本字体
//        pd.setValueLabelsTextColor(int valueLabelTextColor);//设置显示值的字体颜色
        pd.setSlicesSpacing(3);//设置数据间的间隙
        pd.setHasLabelsOnlyForSelected(true);//设置当值被选中才显示

        pd.setValues(getTypeList(list));//为饼图添加数据
        pieChart.setPieChartData(pd);//将数据设置给饼图
    }

    private List<SliceValue> getTypeList(List<Bill> list) {
        int[] values = new int[8];
        sliceList = new ArrayList<>();
        int type = 0;
        for (Bill order : list) {
            type = order.getType();
            values[type] += (BigDecimal.valueOf(order.getCash())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        for (int i = 0; i < 8; i++) {//循环为饼图设置数据
            sliceList.add(new SliceValue(values[i], colors[i]).setLabel(types[i]));
        }
        return sliceList;
    }

    private void initRecycler(List<Bill> list) {
        BaseQuickAdapter<Bill, BaseViewHolder> ordersAdapter;
        BaseQuickAdapter<SliceValue, BaseViewHolder> typeAdapter;
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        typeList.setLayoutManager(layoutManager1);
        typeAdapter = new BaseQuickAdapter<SliceValue, BaseViewHolder>(R.layout.item_type, getTypeList(list)) {
            @Override
            protected void convert(BaseViewHolder helper, SliceValue item) {
                helper.setBackgroundColor(R.id.color, item.getColor());
                helper.setText(R.id.name, String.valueOf(item.getLabel()));
                helper.setText(R.id.cash, item.getValue() + "");
            }
        };
        //一行代码开启动画 默认CUSTOM动画
        typeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        typeList.setAdapter(typeAdapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        orderList.setLayoutManager(layoutManager2);
        orderList.setNestedScrollingEnabled(false);
        ordersAdapter = new BaseQuickAdapter<Bill, BaseViewHolder>(R.layout.item_orders, sort(list)) {
            @Override
            protected void convert(BaseViewHolder helper, Bill item) {
                helper.setBackgroundColor(R.id.color, colors[item.getType()]);
                helper.setText(R.id.name, item.getName());
                helper.setText(R.id.dealer, item.getDealer());
                helper.setText(R.id.time, item.getTime());
                helper.setText(R.id.cash, item.getCash() + "");
            }
        };
        //一行代码开启动画 默认CUSTOM动画
        ordersAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        orderList.setAdapter(ordersAdapter);
    }

    public List<Bill> sort(List<Bill> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1 - i; j++) {
                if (list.get(j).getCash() < list.get(j + 1).getCash()) {
                    Bill temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        return list;
    }

    private void initPopupWindow() {
        View v = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(
                R.layout.menu_month_select, null);
        popup = new PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        for (int i = 0; i < monthList.size(); i++) {
            v.findViewById(monthList.get(i)).setOnClickListener(button -> {
                numOfMonth = monthList.indexOf(button.getId());
                initData(numOfMonth);
                popup.dismiss();
            });
        }
        v.findViewById(R.id.popup_back).setOnClickListener(v1 -> popup.dismiss());
        popup.setFocusable(true);
        //该属性设置为true则你在点击屏幕的空白位置也会退出
        popup.setTouchable(true);
        popup.setOutsideTouchable(true);
        popup.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#AAFFFFFF")));
        popup.setAnimationStyle(R.style.PopupAnimation);
    }
}
