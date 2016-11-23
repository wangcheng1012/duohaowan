package com.hd.wlj.duohaowan.util;

import android.app.Activity;

import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.publish.publishModel;
import com.wlj.base.bean.Base;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.asyn.AsyncCall;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 分段上传
 */
public class UploadChucks extends BaseAsyncModle {

    private byte[] file_bytes;
    private Long chunk;
    private Long chunks;
    private String name;

    public UploadChucks() {
        super();
    }

    public UploadChucks(Activity paramActivity) {
        super(paramActivity);
    }

    public UploadChucks(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {

        HttpPost hp = new HttpPost(Urls.uploadChucks);
        hp.addParemeter("name", name);
        hp.addParemeter("chunk", chunk + "");
        hp.addParemeter("chunks", chunks + "");

        String file_string = HexM.encodeHexString(file_bytes);
        hp.addParemeter("file_bytes", file_string);
        asRequestModle.setHttpPost(hp);
//        asRequestModle.setShowLoading(true);
    }


    public void setFile_bytes(byte[] file_bytes) {
        this.file_bytes = file_bytes;
    }

    public void setChunk(Long chunk) {
        this.chunk = chunk;
    }

    public void setChunks(Long chunks) {
        this.chunks = chunks;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new publishModel(jsonObject);
    }



}
