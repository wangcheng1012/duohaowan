package com.hd.wlj.duohaowan.ui.my.card;

import android.app.Activity;

import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.been.User;
import com.hd.wlj.duohaowan.ui.publish.border.BorderModel;
import com.wlj.base.bean.Base;
import com.wlj.base.util.StringUtils;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 */
public class CardModle extends BaseAsyncModle {

    private User user;

    public CardModle() {
        super();
    }

    public CardModle(Activity paramActivity) {
        super(paramActivity);
    }

    public CardModle(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle as) throws IOException {
        HttpPost httpPost = new HttpPost(Urls.updateArtistCard);

        httpPost.addParemeter("nick_name", user.getNickname());
        httpPost.addParemeter("intro", user.getIntro());
        httpPost.addParemeter("touxian", user.getTouxian());
        httpPost.addParemeter("pic", user.getCardbg());
        String cardPic = user.getCardPic();
        if(!StringUtils.isEmpty(cardPic)) {
            httpPost.addParemeter("pic_touxiang", "<file>" + cardPic + "</file>");
        }
        as.setHttpPost(httpPost);
        as.setShowLoading(true);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new CardModle(jsonObject);
    }


    public void setUser(User user) {
        this.user = user;
    }
}
