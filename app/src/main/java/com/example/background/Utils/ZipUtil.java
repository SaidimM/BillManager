package com.example.background.Utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.background.Utils.FileUtil.getFileEncode;


public class ZipUtil {
    private static final int BUFF_SIZE = 4096;

    /*
    获取ZIP文件中的文件名和目录名
    */
    public static List<String> getEntryNames(String zipFilePath, String password){
        List<String> entryList = new ArrayList<>();
        ZipFile zf;
        try {
            zf = new ZipFile(zipFilePath);
            zf.setCharset(Charset.forName("gbk"));//默认UTF8，如果压缩包中的文件名是GBK会出现乱码
            if(zf.isEncrypted()){
                zf.setPassword(password.toCharArray());//设置压缩密码
            }
            for(FileHeader obj : zf.getFileHeaders()){
                String fileName = obj.getFileName();//文件名会带上层级目录信息
                entryList.add(fileName);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return entryList;
    }

    /*
    将ZIP包中的文件解压到指定目录
    */
    public static int extract(String zipFilePath, String password, String destDir){
        InputStream is = null;
        OutputStream os = null;
        ZipFile zf;
        try {
            zf = new ZipFile(zipFilePath);
            String code = FileUtil.getFileEncode(zipFilePath);
            if (code.equals("asci")) code = "UTF-8";
            zf.setCharset(StandardCharsets.ISO_8859_1);
            if(zf.isEncrypted()){
                zf.setPassword(password.toCharArray());
            }
            for(FileHeader obj : zf.getFileHeaders()){
                String destFile = destDir + "/" + obj.getFileName();
                File file = new File(destFile);
                if(!Objects.requireNonNull(file.getParentFile()).exists()){
                    file.getParentFile().mkdirs();//创建目录
                }
                is = zf.getInputStream(obj);
                os = new FileOutputStream(destFile);
                int readLen = -1;
                byte[] buff = new byte[BUFF_SIZE];
                while ((readLen = is.read(buff)) != -1) {
                    os.write(buff, 0, readLen);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            if (e.toString().contains("Wrong password!")) return 0;
        }finally{
            //关闭资源
            try{
                if(is != null){
                    is.close();
                }
            }catch(IOException ignored){}

            try{
                if(os != null){
                    os.close();
                }
            }catch(IOException ignored){}
        }
        return 1;
    }
}
