package com.example.background.Utils;

import com.example.background.module.Orders;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderManage {
    public List<Orders> getMonthOrders() {
        return monthOrders;
    }

    public void setMonthOrders(List<Orders> monthOrders) {
        this.monthOrders = monthOrders;
    }

    public List<Orders> getDayOrders() {
        return dayOrders;
    }

    public void setDayOrders(List<Orders> dayOrders) {
        this.dayOrders = dayOrders;
    }

    public List<Orders> getWeekOrders() {
        return weekOrders;
    }

    public void setWeekOrders(List<Orders> weekOrders) {
        this.weekOrders = weekOrders;
    }

    private List<Orders> monthOrders;
    private List<Orders> dayOrders;
    private List<Orders> weekOrders;
    private List<Orders> totalOrders;
    private Calendar calendar, tempCalendar;
    TimeZone zone = TimeZone.getTimeZone("GMT");
    SimpleDateFormat format;
    private String costOfMonth;

    public OrderManage() {
        calendar = new GregorianCalendar();
        tempCalendar = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
        format.setTimeZone(zone);
        calendar = new GregorianCalendar();
        totalOrders = new Select().from(Orders.class).queryList();
        costOfMonth = this.getOrders(calendar.get(Calendar.MONTH));
    }

    public OrderManage(int month) {
        calendar = new GregorianCalendar();
        tempCalendar = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
        format.setTimeZone(zone);
        totalOrders = new Select().from(Orders.class).queryList();
        costOfMonth = this.getOrders(month);
    }

    public String getCostOfMonth() {
        return costOfMonth;
    }

    public void setCostOfMonth(String costOfMonth) {
        this.costOfMonth = costOfMonth;
    }

    public List<Orders> getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(List<Orders> totalOrders) {
        this.totalOrders = totalOrders;
    }


    public String getOrders(int month) {
        monthOrders = new ArrayList<>();
        weekOrders = new ArrayList<>();
        dayOrders = new ArrayList<>();
        float sum = 0;
        Date date;
        try {
            for (int i = totalOrders.size() - 1; i >= 0; i--) {
                date = format.parse(totalOrders.get(i).time);
                assert date != null;
                tempCalendar.setTime(date);
                System.out.println(date.getYear());
                if (calendar.get(Calendar.YEAR) == tempCalendar.get(Calendar.YEAR)) {
                    if (tempCalendar.get(Calendar.MONTH) == month) {
                        monthOrders.add(totalOrders.get(i));
                        sum += totalOrders.get(i).cash;
                        if (calendar.get(Calendar.DAY_OF_MONTH) - tempCalendar.get(Calendar.DAY_OF_MONTH) <= 6) {
                            weekOrders.add(totalOrders.get(i));
                            if (calendar.get(Calendar.DAY_OF_MONTH) == tempCalendar.get(Calendar.DAY_OF_MONTH))
                                dayOrders.add(totalOrders.get(i));
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (new BigDecimal(sum)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
    }

    public float[] getScoreList(List<Orders> list) {
        float[] score = {0, 0, 0, 0, 0};
        tempCalendar = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
        format.setTimeZone(zone);
        Date date;
        try {
            for (Orders order : list) {
                date = format.parse(order.time);
                assert date != null;
                tempCalendar.setTime(date);
                score[tempCalendar.get(Calendar.DAY_OF_MONTH) / 7] += order.cash;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return score;
    }

    public List<Orders> getSortList(int type, Calendar c1, Calendar c2) {
        List<Orders> sortedList = new ArrayList<>();
        Date date;
        try {
            for (Orders order : totalOrders) {
                if (order.type == type || type == -1) {
                    System.out.println("#################################################################################");
                    System.out.println(c1.get(Calendar.YEAR) + " " + c1.get(Calendar.MONTH) + " " + c1.get(Calendar.DAY_OF_MONTH) + " " + c2.get(Calendar.YEAR) + " " + c2.get(Calendar.MONTH) + " " + c2.get(Calendar.DAY_OF_MONTH));
                    date = format.parse(order.time);
                    assert date != null;
                    tempCalendar.setTime(date);
                    if (tempCalendar.get(Calendar.YEAR) >= c1.get(Calendar.YEAR) && tempCalendar.get(Calendar.YEAR) <= c2.get(Calendar.YEAR)) {
                        if (tempCalendar.get(Calendar.MONTH) >= c1.get(Calendar.MONTH) && tempCalendar.get(Calendar.MONTH) <= c2.get(Calendar.MONTH)) {
                            if (tempCalendar.get(Calendar.DAY_OF_MONTH) >= c1.get(Calendar.DAY_OF_MONTH) && tempCalendar.get(Calendar.DAY_OF_MONTH) <= c2.get(Calendar.DAY_OF_MONTH)) {
                                sortedList.add(order);
                            }
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sortedList;
    }

    public List<Orders> getWordSortList(String s) {
        List<Orders> sortedList = new ArrayList<>();
        for (Orders order : totalOrders) {
            if (order.name.contains(s) || order.dealer.contains(s)) {
                sortedList.add(order);
            }
        }
        return sortedList;
    }

}
