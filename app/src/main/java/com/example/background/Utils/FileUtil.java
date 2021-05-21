package com.example.background.Utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import com.example.background.module.FileBean;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FileUtil {
    /**
     * 根据URI获取文件真实路径（兼容多张机型）
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByUri(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {

            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion >= 19) { // api >= 19
                return getRealPathFromUriAboveApi19(context, uri);
            } else { // api < 19
                return getRealPathFromUriBelowAPI19(context, uri);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String type = documentId.split(":")[0];
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};

                //
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                filePath = getDataColumn(context, contentUri, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            } else if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else {
                //Log.e("路径错误");
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void copyFileByStream(File source, File dest) throws
            IOException {
        try (InputStream is = new FileInputStream(source);
             OutputStream os = new FileOutputStream(dest);) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }


    /**
     * 读取文件的最后修改时间的方法
     */
    public static String getFileLastModifiedTime(File f) {
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }




    public static FileBean getFileInfoFromFile(File file) {
        FileBean fileBean = new FileBean();
        fileBean.setFileName(file.getName());
        fileBean.setFilePath(file.getPath());
        fileBean.setFileSize(file.length());
        fileBean.setIsSelect(true);
//        fileInfo.setDirectory(file.isDirectory());
        fileBean.setTime(getFileLastModifiedTime(file));
        int lastDotIndex = file.getName().lastIndexOf(".");
        if (lastDotIndex > 0) {
            String fileSuffix = file.getName().substring(lastDotIndex + 1);
//            fileInfo.setSuffix(fileSuffix);
        }
        return fileBean;
    }

    public static String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
    /**
     * 利用文件url转换出文件名
     *
     * @param url
     * @return
     */
    public static String parseName(String url) {
        String fileName = null;
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = String.valueOf(System.currentTimeMillis());
            }
        }
        return fileName;
    }
    /**
     * 将url进行encode，解决部分手机无法下载含有中文url的文件的问题（如OPPO R9）
     *
     * @param url
     * @return
     * @author xch
     */
    public static String toUtf8String(String url) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = String.valueOf(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }
    public static String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
