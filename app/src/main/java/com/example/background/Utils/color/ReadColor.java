package com.example.background.Utils.color;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class ReadColor {

    private static final Map<String, String> colors = new HashMap<>();

    public static void setColors() {
        colors.put("000", "黑色");
        colors.put("011", "黑色");
        colors.put("110", "黑色");
        colors.put("001", "黑色");
        colors.put("100", "黑色");
        colors.put("111", "黑色");
        colors.put("101", "黑色");
        colors.put("010", "黑色");
        colors.put("544", "浅粉色");
        colors.put("533", "粉色");
        colors.put("534", "浅粉色");
        colors.put("523", "粉色");
        colors.put("535", "浅粉色");
        colors.put("524", "粉色");
        colors.put("513", "深粉色");
        colors.put("525", "粉色");
        colors.put("514", "深粉色");
        colors.put("503", "深粉色");
        colors.put("515", "深粉色");
        colors.put("504", "深粉色");
        colors.put("554", "白色");
        colors.put("555", "白色");
        colors.put("545", "白色");
        colors.put("222", "灰色");
        colors.put("333", "灰色");
        colors.put("444", "灰色");
        colors.put("200", "深红色");
        colors.put("310", "深红色");
        colors.put("201", "深红色");
        colors.put("300", "深红色");
        colors.put("311", "深红色");
        colors.put("410", "红色");
        colors.put("520", "浅红色");
        colors.put("202", "深红色");
        colors.put("301", "深红色");
        colors.put("400", "红色");
        colors.put("411", "红色");
        colors.put("510", "红色");
        colors.put("312", "浅红色");
        colors.put("521", "浅红色");
        colors.put("401", "红色");
        colors.put("500", "红色");
        colors.put("511", "红色");
        colors.put("302", "浅红色");
        colors.put("412", "浅红色");
        colors.put("522", "浅红色");
        colors.put("501", "红色");
        colors.put("402", "浅红色");
        colors.put("512", "浅红色");
        colors.put("502", "浅红色");
        colors.put("440", "浅橙色");
        colors.put("430", "深橙色");
        colors.put("441", "浅橙色");
        colors.put("420", "深橙色");
        colors.put("431", "深橙色");
        colors.put("530", "橙色");
        colors.put("442", "浅橙色");
        colors.put("421", "深橙色");
        colors.put("432", "深橙色");
        colors.put("531", "橙色");
        colors.put("422", "深橙色");
        colors.put("532", "橙色");
        colors.put("550", "黄色");
        colors.put("540", "深黄色");
        colors.put("551", "黄色");
        colors.put("541", "深黄色");
        colors.put("552", "浅黄色");
        colors.put("542", "深黄色");
        colors.put("553", "浅黄色");
        colors.put("543", "浅黄色");
        colors.put("130", "青色");
        colors.put("230", "深青色");
        colors.put("054", "青色");
        colors.put("131", "青色");
        colors.put("033", "深青色");
        colors.put("231", "深青色");
        colors.put("055", "青色");
        colors.put("132", "青色");
        colors.put("154", "浅青色");
        colors.put("133", "深青色");
        colors.put("232", "深青色");
        colors.put("155", "浅青色");
        colors.put("233", "深青色");
        colors.put("255", "浅青色");
        colors.put("355", "浅青色");
        colors.put("455", "浅青色");
        colors.put("140", "青色");
        colors.put("141", "青色");
        colors.put("120", "深青色");
        colors.put("043", "青色");
        colors.put("142", "青色");
        colors.put("044", "深青色");
        colors.put("121", "深青色");
        colors.put("143", "深青色");
        colors.put("122", "深青色");
        colors.put("144", "深青色");
        colors.put("045", "青色");
        colors.put("225", "深青色");
        colors.put("050", "绿色");
        colors.put("150", "绿色");
        colors.put("051", "绿色");
        colors.put("030", "深绿色");
        colors.put("250", "绿色");
        colors.put("151", "绿色");
        colors.put("052", "绿色");
        colors.put("031", "深绿色");
        colors.put("251", "绿色");
        colors.put("152", "绿色");
        colors.put("053", "绿色");
        colors.put("350", "浅绿色");
        colors.put("032", "深绿色");
        colors.put("252", "绿色");
        colors.put("153", "绿色");
        colors.put("450", "浅绿色");
        colors.put("351", "浅绿色");
        colors.put("253", "绿色");
        colors.put("451", "浅绿色");
        colors.put("352", "浅绿色");
        colors.put("254", "绿色");
        colors.put("452", "浅绿色");
        colors.put("353", "浅绿色");
        colors.put("453", "浅绿色");
        colors.put("354", "浅绿色");
        colors.put("454", "浅绿色");
        colors.put("040", "绿色");
        colors.put("041", "绿色");
        colors.put("020", "深绿色");
        colors.put("240", "绿色");
        colors.put("042", "绿色");
        colors.put("340", "深绿色");
        colors.put("021", "深绿色");
        colors.put("241", "绿色");
        colors.put("341", "深绿色");
        colors.put("022", "深绿色");
        colors.put("242", "绿色");
        colors.put("342", "深绿色");
        colors.put("243", "绿色");
        colors.put("343", "深绿色");
        colors.put("012", "深蓝色");
        colors.put("034", "浅蓝色");
        colors.put("013", "深蓝色");
        colors.put("112", "深蓝色");
        colors.put("035", "浅蓝色");
        colors.put("134", "浅蓝色");
        colors.put("113", "深蓝色");
        colors.put("014", "蓝色");
        colors.put("135", "浅蓝色");
        colors.put("234", "浅蓝色");
        colors.put("015", "蓝色");
        colors.put("114", "蓝色");
        colors.put("235", "浅蓝色");
        colors.put("115", "蓝色");
        colors.put("023", "深蓝色");
        colors.put("002", "深蓝色");
        colors.put("024", "蓝色");
        colors.put("123", "蓝色");
        colors.put("244", "浅蓝色");
        colors.put("145", "浅蓝色");
        colors.put("003", "深蓝色");
        colors.put("102", "深蓝色");
        colors.put("025", "蓝色");
        colors.put("124", "蓝色");
        colors.put("344", "浅蓝色");
        colors.put("245", "浅蓝色");
        colors.put("223", "浅蓝色");
        colors.put("103", "深蓝色");
        colors.put("004", "蓝色");
        colors.put("125", "蓝色");
        colors.put("345", "浅蓝色");
        colors.put("224", "浅蓝色");
        colors.put("005", "蓝色");
        colors.put("104", "蓝色");
        colors.put("105", "蓝色");
        colors.put("212", "深紫色");
        colors.put("213", "深紫色");
        colors.put("334", "浅紫色");
        colors.put("214", "深紫色");
        colors.put("313", "紫色");
        colors.put("335", "浅紫色");
        colors.put("434", "浅紫色");
        colors.put("215", "深紫色");
        colors.put("314", "紫色");
        colors.put("413", "紫色");
        colors.put("435", "浅紫色");
        colors.put("315", "紫色");
        colors.put("414", "紫色");
        colors.put("415", "紫色");
        colors.put("323", "浅紫色");
        colors.put("203", "深紫色");
        colors.put("324", "浅紫色");
        colors.put("423", "浅紫色");
        colors.put("445", "浅紫色");
        colors.put("204", "深紫色");
        colors.put("303", "紫色");
        colors.put("325", "浅紫色");
        colors.put("424", "浅紫色");
        colors.put("205", "深紫色");
        colors.put("304", "紫色");
        colors.put("403", "紫色");
        colors.put("425", "浅紫色");
        colors.put("305", "紫色");
        colors.put("404", "紫色");
        colors.put("405", "紫色");
        colors.put("505", "紫色");
        colors.put("220", "深棕色");
        colors.put("330", "浅棕色");
        colors.put("210", "深棕色");
        colors.put("221", "深棕色");
        colors.put("320", "棕色");
        colors.put("331", "浅棕色");
        colors.put("211", "深棕色");
        colors.put("321", "棕色");
        colors.put("332", "浅棕色");
        colors.put("322", "棕色");
        colors.put("443", "浅棕色");
        colors.put("433", "浅棕色");
    }

    public static Color getPixColor(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10;
        Bitmap src = BitmapFactory.decodeFile(path, options);
        return getColorFromBitmap(src);
    }

    public static Color getPixColor(Context context, int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10;
        Bitmap src = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        return getColorFromBitmap(src);
    }

    public static Color getColorFromBitmap(Bitmap src) {
        float R = 0, G = 0, B = 0;
        Map<Color, Integer> colors = new HashMap<>();
        int colorAvgVal = 5;
        int pixelColor;
        int height = src.getHeight();
        int width = src.getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelColor = src.getPixel(x, y);
                R = Math.round((float) Color.red(pixelColor) / colorAvgVal) * colorAvgVal;
                G = Math.round((float) Color.green(pixelColor) / colorAvgVal) * colorAvgVal;
                B = Math.round((float) Color.blue (pixelColor) / colorAvgVal) * colorAvgVal;
                Color color = Color.valueOf(R, G, B);
                if (colors.containsKey(color)) {
                    int value = colors.get(color) + 1;
                    colors.put(color, value);
                } else colors.put(color, 1);
            }
        }
        float cn = height * width;
        ArrayList<Color> colorArrays = new ArrayList<>();
        Map<Color,Integer> sorted = colors
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        for (Map.Entry<Color, Integer> entry : sorted.entrySet()) {
            Color mapKey = entry.getKey();
            Integer mapValue = entry.getValue();
            colorArrays.add(mapKey);
            System.out.println(getTag(mapKey) + ":" + mapValue + "  占比为" + mapValue / cn * 100 + "%");
        }
        Color result = new Color();
        for (Color c : colorArrays) {
            if (!getTag(c).equals("黑色") && !getTag(c).equals("白色") && !getTag(c).contains("灰")) {
                result = c;
                break;
            }
        }
        return result;
    }


    public static String getColorString(Color color) {
        String string = "#";
        string += Integer.toHexString((int) color.red());
        string += Integer.toHexString((int) color.green());
        string += Integer.toHexString((int) color.blue());
        return string;
    }

    public static String getTag(Color color) {
        setColors();
        int colorAvgVal = 51;
        int r = Math.round(color.red() / colorAvgVal);
        int g = Math.round(color.green() / colorAvgVal);
        int b = Math.round(color.blue() / colorAvgVal);
        String k = String.format("%s%s%s", r, g, b);
        return colors.get(k);
    }

    public static Color getAccent(Color primary) {
        TransformColor transform = new TransformColor();
        /*
         * Increase or decrease color brightness, saturation, or hue.
         */
        Pixel pixel = new Pixel();
        pixel.setRGB(primary.toArgb());
        transform.rgbToHsl(pixel);
        pixel.luminosity *= (0.01);
        if (pixel.luminosity < 0.0)
            pixel.luminosity = 0.0;
        else if (pixel.luminosity > 1.0)
            pixel.luminosity = 1.0;
        if (pixel.saturation < 0.0)
            pixel.saturation = 0.0;
        else if (pixel.saturation > 1.0)
            pixel.saturation = 1.0;
        pixel.hue *= (0.01) * 50;
        if (pixel.hue < 0.0)
            pixel.hue += 1.0;
        else if (pixel.hue > 1.0)
            pixel.hue -= 1.0;
        transform.hslToRgb(pixel);
        return Color.valueOf(pixel.getRGB());
    }

    public static Color modulate(double percent_hue, double percent_saturation, double percent_brightness, Pixel pixel) {
        TransformColor transform = new TransformColor();
        /*
         * Increase or decrease color brightness, saturation, or hue.
         */
        transform.rgbToHsl(pixel);
        pixel.luminosity *= (0.01) * percent_brightness;
        if (pixel.luminosity < 0.0)
            pixel.luminosity = 0.0;
        else if (pixel.luminosity > 1.0)
            pixel.luminosity = 1.0;
        pixel.saturation *= (0.01) * percent_saturation;
        if (pixel.saturation < 0.0)
            pixel.saturation = 0.0;
        else if (pixel.saturation > 1.0)
            pixel.saturation = 1.0;
        pixel.hue *= (0.01) * percent_hue;
        if (pixel.hue < 0.0)
            pixel.hue += 1.0;
        else if (pixel.hue > 1.0)
            pixel.hue -= 1.0;
        transform.hslToRgb(pixel);
        return Color.valueOf(pixel.getRGB());
    }

    public static Color contrast(double percent, Pixel pixel) {
        TransformColor transform = new TransformColor();
        double alpha = percent / 100.0;

        /*
         * Enhance contrast: dark color become darker, light color become lighter.
         */
        transform.rgbToHsl(pixel);
        pixel.luminosity += alpha * (alpha * (Math.sin(Math.PI * (pixel.luminosity - alpha)) + 1.0) - pixel.luminosity);
        if (pixel.luminosity > 1.0)
            pixel.luminosity = 1.0;
        else if (pixel.luminosity < 0.0)
            pixel.luminosity = 0.0;
        transform.hslToRgb(pixel);
        return Color.valueOf(pixel.getRGB());
    }


}