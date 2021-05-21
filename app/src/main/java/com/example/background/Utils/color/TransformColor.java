package com.example.background.Utils.color;

public class TransformColor {
    public static final double MaxRGB = 255.0;

    public void rgbToHsl(Pixel pixel) {
        int red = pixel.red;
        int blue = pixel.blue;
        int green = pixel.green;

        double b, delta, g, max, min, r;

        double hue, saturation, luminosity;
        /*
         * Convert RGB to HSL colorspace.
         */
        r = (double) red / MaxRGB;
        g = (double) green / MaxRGB;
        b = (double) blue / MaxRGB;
        max = Math.max(r, Math.max(g, b));
        min = Math.min(r, Math.min(g, b));

        hue = 0.0;
        saturation = 0.0;
        luminosity = (min + max) / 2.0;
        delta = max - min;
        if (delta == 0.0) {
            pixel.hue = hue;
            pixel.saturation = saturation;
            pixel.luminosity = luminosity;
            return;
        }
        saturation = delta / ((luminosity <= 0.5) ? (min + max) : (2.0 - max - min));
        if (r == max)
            hue = (g == min ? 5.0 + (max - b) / delta : 1.0 - (max - g) / delta);
        else if (g == max)
            hue = (b == min ? 1.0 + (max - r) / delta : 3.0 - (max - b) / delta);
        else
            hue = (r == min ? 3.0 + (max - g) / delta : 5.0 - (max - r) / delta);
        hue /= 6.0;

        pixel.hue = hue;
        pixel.saturation = saturation;
        pixel.luminosity = luminosity;
    }

    public void hslToRgb(Pixel pixel) {
        double hue, saturation, luminosity;
        // int red, green, blue;
        double b, g, r, v, x, y, z;

        hue = pixel.hue;
        saturation = pixel.saturation;
        luminosity = pixel.luminosity;

        /*
         * Convert HSL to RGB colorspace.
         */
        v = (luminosity <= 0.5) ? (luminosity * (1.0 + saturation))
                : (luminosity + saturation - luminosity * saturation);
        if (saturation == 0.0) {
            pixel.red = (int) (MaxRGB * luminosity + 0.5);
            pixel.green = (int) (MaxRGB * luminosity + 0.5);
            pixel.blue = (int) (MaxRGB * luminosity + 0.5);
            return;
        }
        y = 2.0 * luminosity - v;
        x = y + (v - y) * (6.0 * hue - Math.floor(6.0 * hue));
        z = v - (v - y) * (6.0 * hue - Math.floor(6.0 * hue));
        switch ((int) (6.0 * hue)) {
            case 0:
                r = v;
                g = x;
                b = y;
                break;
            case 1:
                r = z;
                g = v;
                b = y;
                break;
            case 2:
                r = y;
                g = v;
                b = x;
                break;
            case 3:
                r = y;
                g = z;
                b = v;
                break;
            case 4:
                r = x;
                g = y;
                b = v;
                break;
            case 5:
                r = v;
                g = y;
                b = z;
                break;
            default:
                r = v;
                g = x;
                b = y;
                break;
        }
        pixel.red = (int) (MaxRGB * r + 0.5);
        pixel.green = (int) (MaxRGB * g + 0.5);
        pixel.blue = (int) (MaxRGB * b + 0.5);
    }
}