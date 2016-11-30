package com.hd.wlj.third.share.weixin;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.hd.wlj.third.R;
import com.hd.wlj.third.share.Constants;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wlj.base.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class WeiXinShare {

    private static final int THUMB_SIZE = 150;
    private IWXAPI api;
    private Activity mActivity;
    private boolean pengyouquan;

    public WeiXinShare(Activity paramActivity) {
        this.mActivity = paramActivity;
        regToWX();
    }


    private byte[] bmpToByteArray(Bitmap paramBitmap, boolean paramBoolean) {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        paramBitmap.compress(CompressFormat.PNG, 100, localByteArrayOutputStream);
        if (paramBoolean)
            paramBitmap.recycle();
        byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
        try {
            localByteArrayOutputStream.close();
            return arrayOfByte;
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    private String buildTransaction(String paramString) {
        return paramString + System.currentTimeMillis();
    }

    /**
     *
     * @param paramBitmap
     * @return
     */
    private WXMediaMessage imgObj(Bitmap paramBitmap) {
        WXImageObject localWXImageObject = new WXImageObject(paramBitmap);
        WXMediaMessage localWXMediaMessage = new WXMediaMessage();
        localWXMediaMessage.mediaObject = localWXImageObject;
        Bitmap localBitmap = Bitmap.createScaledBitmap(paramBitmap, THUMB_SIZE, THUMB_SIZE, true);
        paramBitmap.recycle();
        localWXMediaMessage.thumbData = bmpToByteArray(localBitmap, true);
        return localWXMediaMessage;
    }

    /**
     * itmapFactory.decodeFile(paramString);
     * @param paramString  本地路径
     * @return
     */
    private WXMediaMessage imgObj(String paramString) {
        WXImageObject localWXImageObject = new WXImageObject();
        localWXImageObject.setImagePath(paramString);
        WXMediaMessage localWXMediaMessage = new WXMediaMessage();
        localWXMediaMessage.mediaObject = localWXImageObject;
        Bitmap localBitmap1 = BitmapFactory.decodeFile(paramString);
        Bitmap localBitmap2 = Bitmap.createScaledBitmap(localBitmap1, THUMB_SIZE, THUMB_SIZE, true);
        localBitmap1.recycle();
        localWXMediaMessage.thumbData = bmpToByteArray(localBitmap2, true);
        return localWXMediaMessage;
    }

    /**
     * new URL(paramString).openStream()
     * @param paramString url 网络地址
     * @return
     */
    private WXMediaMessage imgUrl(String paramString) {
        WXImageObject localWXImageObject = new WXImageObject();
        localWXImageObject.imagePath  = paramString;
        WXMediaMessage localWXMediaMessage = new WXMediaMessage();
        localWXMediaMessage.mediaObject = localWXImageObject;
        Bitmap localBitmap1 = null;
        try {
            localBitmap1 = BitmapFactory.decodeStream(new URL(paramString).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap localBitmap2 = Bitmap.createScaledBitmap(localBitmap1, THUMB_SIZE, THUMB_SIZE, true);
        localBitmap1.recycle();
        localWXMediaMessage.thumbData = bmpToByteArray(localBitmap2, true);
        return localWXMediaMessage;
    }

    private void regToWX() {
        this.api = WXAPIFactory.createWXAPI(this.mActivity, Constants.WX_APP_ID, false);
        this.api.registerApp(Constants.WX_APP_ID);
    }

    private void sendMessage(WXMediaMessage paramWXMediaMessage) {
        SendMessageToWX.Req localReq = new SendMessageToWX.Req();
        localReq.transaction = (System.currentTimeMillis() + "image");
        localReq.message = paramWXMediaMessage;
        if (this.pengyouquan) {
            localReq.scene = SendMessageToWX.Req.WXSceneTimeline;
        }else{
            localReq.scene = SendMessageToWX.Req.WXSceneSession;
        }
        this.api.sendReq(localReq);
    }

    private WXMediaMessage textObj(String paramString) {
        WXTextObject localWXTextObject = new WXTextObject();
        localWXTextObject.text = paramString;
        WXMediaMessage localWXMediaMessage = new WXMediaMessage();
        localWXMediaMessage.mediaObject = localWXTextObject;
        localWXMediaMessage.description = paramString;
        return localWXMediaMessage;
    }

    private WXMediaMessage webPageShare(String webpageUrl, String title, String description, Bitmap bitmap) {
        if (!this.api.isWXAppInstalled()) {
            Toast.makeText(this.mActivity, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return null;
        }
        WXWebpageObject localWXWebpageObject = new WXWebpageObject();
        localWXWebpageObject.webpageUrl = webpageUrl;
        WXMediaMessage wxMediaMessage = new WXMediaMessage(localWXWebpageObject);
        wxMediaMessage.title = title.substring(0, Math.min(title.length(), 256));
        wxMediaMessage.description = description.substring(0, Math.min(description.length(), 512));
        if (bitmap != null) {
            Bitmap localBitmap = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
            bitmap.recycle();
            wxMediaMessage.setThumbImage(localBitmap);
        }
        return wxMediaMessage;
    }

    /**
     *
     * @param paramBitmap data
     * @param localpath  本地地址
     * @param url 网络地址
     */
    public void sendImg(Bitmap paramBitmap, String localpath, String url) {

        if (paramBitmap != null)
            sendMessage(imgObj(paramBitmap));
        if (StringUtils.isEmpty(localpath))
            sendMessage(imgObj(localpath));
        if(StringUtils.isEmpty(url))
            sendMessage(imgUrl(url));
    }

    public void sendText(String paramString) {
        sendMessage(textObj(paramString));
    }

    public void sendWebPage(String webpageUrl, String title, String description, Bitmap bitmap) {
        sendMessage(webPageShare(webpageUrl, title, description, bitmap));
    }

    public void setPengyouquan(boolean paramBoolean) {
        this.pengyouquan = paramBoolean;
    }
}