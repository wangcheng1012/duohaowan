package com.wlj.base.util.img;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.wlj.base.util.Log;
import com.wlj.base.web.asyn.HexM;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Base64;
import android.widget.ImageView;

/**
 * @author wlj
 */
public class BitmapUtil {

    private static BitmapUtil imageUtil;

    public static BitmapUtil getInstall() {
        if (imageUtil == null) {
            imageUtil = new BitmapUtil();
        }
        return imageUtil;
    }

//	@SuppressLint("NewApi")
//	public int getBitmapSize(Bitmap bitmap) {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API
//																			// 12
//			return bitmap.getByteCount();
//		}
//		return bitmap.getRowBytes() * bitmap.getHeight(); // earlier version
//	}

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            if (Log.LOG)
                e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     *
     * @param angle
     *
     * @param bitmap
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        Log.e("PhotoGraphActivity", "旋转图片=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 图片资源的回收
     *
     * @param imageView
     */
    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    /**
     * 按规定长宽缩小图片
     *
     * @param in
     * @param in2
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static synchronized Bitmap decodeSampledBitmapFromStream(InputStream in, InputStream in2, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        // Calculate inSampleSize
        options2.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options2.inJustDecodeBounds = false;

        return BitmapFactory.decodeStream(in2, null, options2);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

//        //先根据宽度进行缩小
//        while (width / inSampleSize > reqWidth) {
//            inSampleSize++;
//        }
//        //然后根据高度进行缩小
//        while (height / inSampleSize > reqHeight) {
//            inSampleSize++;
//        }

        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);

            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }

    public String bitmaptoString(Drawable bitmap) {

        BitmapDrawable drawable = (BitmapDrawable) bitmap;

        return bitmaptoString(drawable.getBitmap());
    }

    public String bitmaptoString(Bitmap bitmap) {

        // 将Bitmap转换成字符串

        String string = null;

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();

        bitmap.compress(CompressFormat.PNG, 80, bStream);

        byte[] bytes = bStream.toByteArray();

//		string = Base64.encodeToString(bytes, Base64.DEFAULT);

        String s = HexM.encodeHexString(bytes);
        return s;

    }

//    /**
//     * 缩放 Bitmap,耗时
//     * @param resource
//     * @param reqWidth
//     * @param reqHeight
//     * @return
//     */
//    public Bitmap creatBitmapScaled(Bitmap resource, int reqWidth, int reqHeight) {
//        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//
//        resource.compress(Bitmap.CompressFormat.PNG, 50, bStream);
//
//        byte[] bytes = bStream.toByteArray();
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        final int height = resource.getHeight();
//        final int width = resource.getWidth();
//        int inSampleSize = 1;
//
//        //先根据宽度进行缩小
//        while (width / inSampleSize > reqWidth) {
//            inSampleSize++;
//        }
//        //然后根据高度进行缩小
//        while (height / inSampleSize > reqHeight) {
//            inSampleSize++;
//        }
//        options.inSampleSize = inSampleSize;
//
//        resource = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
//
//        return resource;
//    }

    /**
     * Resize a bitmap object to fit the passed width and height
     *
     * @param input
     *           The bitmap to be resized
     * @param destWidth
     *           Desired maximum width of the result bitmap
     * @param destHeight
     *           Desired maximum height of the result bitmap
     * @return A new resized bitmap
     * @throws OutOfMemoryError
     *            if the operation exceeds the available vm memory
     */
    public static Bitmap resizeBitmap( final Bitmap input, int destWidth, int destHeight, int rotation ) throws OutOfMemoryError {

        int dstWidth = destWidth;
        int dstHeight = destHeight;
        final int srcWidth = input.getWidth();
        final int srcHeight = input.getHeight();

        if ( rotation == 90 || rotation == 270 ) {
            dstWidth = destHeight;
            dstHeight = destWidth;
        }

        boolean needsResize = false;
        float p;
        if ( ( srcWidth > dstWidth ) || ( srcHeight > dstHeight ) ) {
            needsResize = true;
            if ( ( srcWidth > srcHeight ) && ( srcWidth > dstWidth ) ) {
                p = (float) dstWidth / (float) srcWidth;
                dstHeight = (int) ( srcHeight * p );
            } else {
                p = (float) dstHeight / (float) srcHeight;
                dstWidth = (int) ( srcWidth * p );
            }
        } else {
            dstWidth = srcWidth;
            dstHeight = srcHeight;
        }

        if ( needsResize || rotation != 0 ) {
            Bitmap output;

            if ( rotation == 0 ) {
                output = Bitmap.createScaledBitmap( input, dstWidth, dstHeight, true );
            } else {
                Matrix matrix = new Matrix();
                matrix.postScale( (float) dstWidth / srcWidth, (float) dstHeight / srcHeight );
                matrix.postRotate( rotation );
                output = Bitmap.createBitmap( input, 0, 0, srcWidth, srcHeight, matrix, true );
            }
            return output;
        } else
            return input;
    }


    /**
     * 根据计算的inSampleSize，得到压缩后图片
     *
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeBitmapFromFile(String pathName,int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

        return bitmap;
    }

}
