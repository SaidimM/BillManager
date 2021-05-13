package com.example.background.Utils;

import android.os.Environment;
import com.example.background.module.Orders;
import jxl.Cell;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.DateTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;


public class JxlRead {
    /**
     * 读取xls文件内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */

    private static final String[] types = {"餐饮美食", "旅游文化", "其他", "交通出行", "图书教育", "服饰美容", "医疗健康", "充值缴费"};

    public static void xls2String(String path) {
        File file = new File(path);
        TimeZone zone = TimeZone.getTimeZone("GMT");
        SimpleDateFormat SF_date = new SimpleDateFormat("yyyy-M-d HH:mm:ss", Locale.CHINA);
        SF_date.setTimeZone(zone);
        try {
            FileInputStream fis = new FileInputStream(file);
            StringBuilder sb = new StringBuilder();
            jxl.Workbook rwb = Workbook.getWorkbook(fis);
            Sheet[] sheet = rwb.getSheets();
            Orders order;
            Random random = new Random();
            for (int i = 0; i < sheet.length; i++) {
                Sheet rs = rwb.getSheet(i);
                for (int j = rs.getRows() - 8; j > 4; j--) {
                    Cell[] cells = rs.getRow(j);
                    for (Cell cell : cells) {
                        sb.append(cell.getContents());
                    }
                    order = new Orders();
                    order.time = SF_date.format(new DateTime((DateCell) cells[2]).getDate());
                    order.dealer = cells[7].getContents().replaceAll(" ", "");
                    order.name = cells[8].getContents().replaceAll(" ", "");
                    order.cash = Float.parseFloat(cells[9].getContents().trim());
                    order.type = random.nextInt(7);
                }
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Orders> readCsvFile(String path) {
        ArrayList<Orders> orders = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));//换成你的文件名
            reader.readLine();//第一行信息，为标题信息，不用，如果需要，注释掉
            ArrayList<String> lines = new ArrayList<>();
            Random random = new Random();
            while(true){
                String line = reader.readLine();
                if(line == null) break;
                lines.add(line);
            }
            for (int i = 16 ; i < lines.size() ; i ++){
                String[] values = lines.get(i).split(",");
                Orders order = new Orders(values[0], values[2],values[3],
                        Float.parseFloat(values[5].substring(1)),random.nextInt(7));
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
}
