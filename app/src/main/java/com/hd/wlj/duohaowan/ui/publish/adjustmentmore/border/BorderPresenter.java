package com.hd.wlj.duohaowan.ui.publish.adjustmentmore.border;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.hd.wlj.duohaowan.ui.publish.MergeBitmap;
import com.wlj.base.bean.Base;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by wlj on 2016/11/10.
 */
public class BorderPresenter extends BasePresenter<BorderView> {


    private final BorderModel borderModel;
    private Activity activity;

    public BorderPresenter(Activity activity) {
        this.activity = activity;
        borderModel = new BorderModel();
    }

    public void loadBorderClassifyData() {
        borderModel.Request()
                .setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
                    @Override
                    public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                        if (view != null) {
                            view.showBorder(list);
                        }
                    }

                    @Override
                    public void fail(Exception paramException) {

                    }
                });

    }




    public void loadSenceData(int width, int height) {
        borderModel.setHeight(height+"");
        borderModel.setWidth(width +"");
        borderModel.Request(BorderModel.type_sence)
                .setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
                    @Override
                    public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                        if (view != null) {
                            view.showBorder(list);
                        }
                    }

                    @Override
                    public void fail(Exception paramException) {

                    }
                });
    }


    public void loadBorderBiliData(View v, MergeBitmap mergeBitmap) {

        Base tag = (Base) v.getTag(R.id.tag_first);
        JSONObject jsonObject = tag.getResultJsonObject();
        String pub_id = jsonObject.optString("pub_id");

        Bitmap workBitmap = mergeBitmap.getWorkBitmap();
        borderModel.setWidth(workBitmap.getWidth()+"");
        borderModel.setHeight(workBitmap.getHeight()+"");
        borderModel.setBackgroundWall_id(mergeBitmap.getBackgroundId());
        borderModel.setPaintingFrameConlumn_id(pub_id);

        borderModel.Request(BorderModel.type_border_bili)
                .setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
                    @Override
                    public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                        if (view != null) {
                            view.showBorderBili(list);
                        }
                    }

                    @Override
                    public void fail(Exception paramException) {

                    }
                });
    }
}
