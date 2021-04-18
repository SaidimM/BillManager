package com.example.background.Utils;

import com.example.background.module.Orders;
import jxl.Cell;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
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

    public static void xls2String(File file) {
        TimeZone zone = TimeZone.getTimeZone("GMT");
        SimpleDateFormat SF_date = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
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
                    order.save();
                }
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}