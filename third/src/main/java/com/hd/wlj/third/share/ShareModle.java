package com.hd.wlj.third.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wlj.base.bean.Base;
import com.wlj.base.web.HttpPost;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class ShareModle extends Base {
    private static final int THUMB_SIZE = 150;
    private static final long serialVersionUID = -5534536105095669110L;
    private String content;
    private String name;
    private String pic;
    private int resPic;


    public String getContent() {
        return this.content;
    }

    public String getName() {
        return this.name;
    }

    public String getPic() {
        return this.pic;
    }

    public Bitmap getPicBitmap() {

        try {
            Bitmap localBitmap1 = BitmapFactory.decodeStream(new HttpPost("http://121.40.177.251:6940" + this.pic).getInputStream());
            if (localBitmap1 != null) {
                Bitmap localBitmap3 = Bitmap.createScaledBitmap(localBitmap1, THUMB_SIZE, THUMB_SIZE, true);
                localBitmap1.recycle();
                return localBitmap3;
            }
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return null;
    }

    public int getResPic() {
        return this.resPic;
    }

    public Base parse(JSONObject paramJSONObject) throws JSONException {
        return null;
    }

    public void setContent(String paramString) {

        this.content = paramString;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setPic(String paramString) {

        this.pic = paramString;
    }

    public void setResPic(int paramInt) {
        this.resPic = paramInt;
    }
}