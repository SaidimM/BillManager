package com.example.background.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import com.example.background.activities.MainActivity;
import com.example.background.module.Bill;
import com.example.background.module.FileBean;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static android.content.ContentValues.TAG;


public class ReadFile {
    /**
     * 读取xls文件内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */

    private static final String[] types = {"餐饮美食", "旅游文化", "其他", "交通出行", "图书教育", "服饰美容", "医疗健康", "充值缴费"};

    public static ArrayList<Bill> readCsvFile(String path) {
        ArrayList<Bill> bill = new ArrayList<>();
        int time = 0, dealer = 0, name = 0, cash = 0, type = 0;
        String code;
        code = getFileEncode(path);
        if (code.equals("asci")) code = "GBK";
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path), code);
            BufferedReader reader = new BufferedReader(isr);//换成你的文件名
            reader.readLine();//第一行信息，为标题信息，不用，如果需要，注释掉
            ArrayList<String> lines = new ArrayList<>();
            Random random = new Random();
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                lines.add(line.replace("  ", ""));
            }
            int startLine = 0;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains("交易对方")) startLine = i;
            }
            String[] keys = lines.get(startLine).split(",");
            for (int i = 0; i < keys.length; i++) {
                switch (keys[i]) {
                    case "交易对方":
                        dealer = i;
                        break;
                    case "商品":
                    case "商品说明":
                        name = i;
                        break;
                    case "交易时间":
                        time = i;
                        break;
                    case "金额(元)":
                    case "金额":
                        cash = i;
                        break;
                }
            }
            boolean isContainsMoney = lines.get(startLine + 1).contains("¥");
            for (int i = startLine + 1; i < lines.size(); i++) {
                String[] values = lines.get(i).split(",");
                Bill order = new Bill();
                order.setName(values[name]);
                order.setType(random.nextInt(7));
                order.setDealer(values[dealer]);
                float cost = 0.00f;
                if (isContainsMoney) cost = Float.parseFloat(values[cash].substring(1));
                else cost = Float.parseFloat(values[cash]);
                order.setCash(cost);
                order.setTime(values[time]);
                bill.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bill;
    }



    /**
     * 获取手机文档数据
     *
     * @param
     */
    public static void getDocumentData(Context context) {
        String[] columns = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.DATA};
        String select = "(_data LIKE '%.csv')";
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Files
                .getContentUri("external"), columns, select, null, null);
        int columnIndexOrThrow_DATA = 0;
        if (cursor != null) {
            columnIndexOrThrow_DATA = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(columnIndexOrThrow_DATA);
                FileBean document = FileUtil.getFileInfoFromFile(new File(path));
                if (document.getFileName().contains("alipay")
                        || document.getFileName().contains("微信"))
                    ((MainActivity)context).getFiles().add(document);
                Log.d(TAG, " csv " + document);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    public static ArrayList<Bill> setOrdersFromFile(ArrayList<FileBean> files) {
        ArrayList<Bill> bill = new ArrayList<>();
        for (FileBean file : files) {
            if (file == null) continue;
            if (file.getIsSelect()) bill.addAll(Objects.requireNonNull(readCsvFile(file.getFilePath())));
        }
        return bill;
    }

    public static String getFileEncode(String path) {
        String charset = "asci";
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis = null;
        try {
            boolean checked = false;
            bis = new BufferedInputStream(new FileInputStream(path));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "Unicode";//UTF-16LE
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "Unicode";//UTF-16BE
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int len = 0;
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) //单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            //双字节 (0xC0 - 0xDF) (0x80 - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) { //也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
                //TextLogger.getLogger().info(loc + " " + Integer.toHexString(read));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ex) {
                }
            }
        }
        return charset;
    }

    private static String getEncode(int flag1, int flag2, int flag3) {
        String encode = "";
        // txt文件的开头会多出几个字节，分别是FF、FE（Unicode）,
        // FE、FF（Unicode big endian）,EF、BB、BF（UTF-8）
        if (flag1 == 255 && flag2 == 254) {
            encode = "Unicode";
        } else if (flag1 == 254 && flag2 == 255) {
            encode = "UTF-16";
        } else if (flag1 == 239 && flag2 == 187 && flag3 == 191) {
            encode = "UTF8";
        } else {
            encode = "asci";// ASCII码
        }
        return encode;
    }

}
