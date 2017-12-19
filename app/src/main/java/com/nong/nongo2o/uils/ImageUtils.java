package com.nong.nongo2o.uils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2017-12-16.
 */

public class ImageUtils {

    public static Bitmap getBitmap(final Context context, final String url, final int w, final int h) {
        Bitmap bitmap = null;

        if ("".equals(url) || url == null) {
            return null;
        }
        try {
            bitmap = Glide.with(context)
                    .load(url)
                    .asBitmap() //必须
                    .centerCrop()
                    .into(w, h)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
        Bitmap localBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        int i;
        int j;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            i = bitmap.getWidth();
            j = bitmap.getWidth();
        } else {
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
        while (true) {
            localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0,
                    80, 80), null);
            if (paramBoolean)
                bitmap.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                // F.out(e);
            }
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
    }
}
