package com.example.background.Utils;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

public class BlurUtil {

    /**
     * bitmap -> drawable
     * @param context
     * @param bm
     * @return
     */
    public static Drawable getDrawable(Context context, Bitmap bm){
        return new BitmapDrawable(context.getResources(),bm);
    }
    /**
     使用到的工具类：
     *
     */
    public static Bitmap fastBlur(Context context, Bitmap sentBitmap, int radius)
    {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs,sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs,input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);/* e.g. 3.f */
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        return bitmap;
    }
}
