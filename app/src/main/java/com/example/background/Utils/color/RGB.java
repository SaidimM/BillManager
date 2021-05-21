package com.example.background.Utils.color;

public class RGB {

    int red;

    int green;

    int blue;

    public RGB(){ }

    public RGB(int red,int green,int blue){
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    public RGB(float red, float green, float blue) {
        this.red = (int) red;
        this.blue = (int) blue;
        this.green = (int) green;
    }

    public String toString() {
        String string = "#", temp;
        temp = Integer.toHexString(red);
        if (temp.length() == 1) temp = 0 + temp;
        string += temp;
        temp = Integer.toHexString(green);
        if (temp.length() == 1) temp = 0 + temp;
        string += temp;
        temp = Integer.toHexString(blue);
        if (temp.length() == 1) temp = 0 + temp;
        string += temp;
        return string;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}
