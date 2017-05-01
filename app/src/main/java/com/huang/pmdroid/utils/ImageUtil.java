package com.huang.pmdroid.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/**
 * Created by huang on 2017/4/26.
 *
 */
public class ImageUtil {
    public static byte[] drawableToBitmap(Drawable pic){
        Bitmap bmp = ((BitmapDrawable)pic).getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    public static Drawable bitmapToDrawable(Context context, byte[] blob){
        //第二步，调用BitmapFactory的解码方法decodeByteArray把字节数组转换为Bitmap对象
        Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        //第三步，调用BitmapDrawable构造函数生成一个BitmapDrawable对象，该对象继承Drawable对象，所以在需要处直接使用该对象即可
        return new BitmapDrawable(context.getResources(),bmp);
    }
}
