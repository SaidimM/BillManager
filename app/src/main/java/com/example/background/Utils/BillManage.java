package com.example.background.Utils;

import com.example.background.module.Bill;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BillManage {
    public List<Bill> getMonthOrders() {
        return monthOrders;
    }

    public void setMonthOrders(List<Bill> monthOrders) {
        this.monthOrders = monthOrders;
    }

    public List<Bill> getDayOrders() {
        return dayOrders;
    }

    public void setDayOrders(List<Bill> dayOrders) {
        this.dayOrders = dayOrders;
    }

    public List<Bill> getWeekOrders() {
        return weekOrders;
    }

    public void setWeekOrders(List<Bill> weekOrders) {
        this.weekOrders = weekOrders;
    }

    private List<Bill> monthOrders;
    private List<Bill> dayOrders;
    private List<Bill> weekOrders;
    private List<Bill> totalOrders;
    private Calendar calendar, tempCalendar;
    TimeZone zone = TimeZone.getTimeZone("GMT");
    SimpleDateFormat format;
    private String costOfMonth;

    public BillManage(ArrayList<Bill> bills) {
        calendar = new GregorianCalendar();
        tempCalendar = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-M-d HH:mm:ss",Locale.CHINA);
        format.setTimeZone(zone);
        calendar = new GregorianCalendar();
        totalOrders = bills;
        costOfMonth = this.getOrders(calendar.get(Calendar.MONTH));
    }

    public BillManage(ArrayList<Bill> bills, int month) {
        totalOrders = bills;
        calendar = new GregorianCalendar();
        tempCalendar = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-M-d HH:mm:ss", Locale.CHINA);
        format.setTimeZone(zone);
        costOfMonth = this.getOrders(month);
    }

    public String getCostOfMonth() {
        return costOfMonth;
    }

    public void setCostOfMonth(String costOfMonth) {
        this.costOfMonth = costOfMonth;
    }

    public List<Bill> getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(List<Bill> totalOrders) {
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
                date = format.parse(totalOrders.get(i).getTime());
                assert date != null;
                tempCalendar.setTime(date);
                System.out.println(date.getYear());
                if (calendar.get(Calendar.YEAR) == tempCalendar.get(Calendar.YEAR)) {
                    if (tempCalendar.get(Calendar.MONTH) == month) {
                        monthOrders.add(totalOrders.get(i));
                        sum += totalOrders.get(i).getCash();
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

    public float[] getScoreList(List<Bill> list) {
        float[] score = {0, 0, 0, 0, 0};
        tempCalendar = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-M-d HH:mm:ss", Locale.CHINA);
        format.setTimeZone(zone);
        Date date;
        try {
            for (Bill order : list) {
                date = format.parse(order.getTime());
                assert date != null;
                tempCalendar.setTime(date);
                score[tempCalendar.get(Calendar.DAY_OF_MONTH) / 7] += order.getCash();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return score;
    }

    public List<Bill> getSortList(int type, Calendar c1, Calendar c2) {
        List<Bill> sortedList = new ArrayList<>();
        Date date;
        try {
            for (Bill order : totalOrders) {
                if (order.getType() == type || type == -1) {
                    System.out.println("#################################################################################");
                    System.out.println(c1.get(Calendar.YEAR) + " " + c1.get(Calendar.MONTH) + " " + c1.get(Calendar.DAY_OF_MONTH) + " " + c2.get(Calendar.YEAR) + " " + c2.get(Calendar.MONTH) + " " + c2.get(Calendar.DAY_OF_MONTH));
                    date = format.parse(order.getTime());
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

    public List<Bill> getSortList(String s) {
        List<Bill> sortedList = new ArrayList<>();
        for (Bill order : totalOrders) {
            if (order.getName().contains(s) || order.getDealer().contains(s)) {
                sortedList.add(order);
            }
        }
        return sortedList;
    }

}
