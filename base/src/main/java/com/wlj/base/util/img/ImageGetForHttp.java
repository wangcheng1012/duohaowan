package com.wlj.base.util.img;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wlj.base.web.HttpPost;


public class ImageGetForHttp {

    private static final String LOG_TAG = "ImageGetForHttp";

    public static Bitmap downloadBitmap(String url) {

        try {
            HttpPost httpPost = new HttpPost(new URL(url));
            InputStream inputStream = httpPost.getInputStream();


            BitmapFactory.Options opt = new BitmapFactory.Options();

            opt.inPreferredConfig = Bitmap.Config.RGB_565;

            opt.inPurgeable = true;

            opt.inInputShareable = true;

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, opt);

            return bitmap;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
