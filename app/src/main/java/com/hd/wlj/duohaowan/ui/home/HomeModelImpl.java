package com.hd.wlj.duohaowan.ui.home;

import android.app.Activity;

import com.hd.wlj.duohaowan.Urls;
import com.wlj.base.bean.Banner;
import com.wlj.base.bean.Base;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.MsgContext;
import com.wlj.base.web.asyn.AsyncCall;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by wlj on 2016/10/26
 */

public class HomeModelImpl extends BaseAsyncModle {

    /**
     * 新闻消息
     */
    public final static int news = 2;
    /**
     * hot
     */
    public final static int hot = 3;
    /**
     * 最新
     */
    public final static int newest = 4;
    private Tag tag = Tag.in;

    public HomeModelImpl() {
    }

    public HomeModelImpl(Activity activity) {
        super(activity);
    }

    public HomeModelImpl(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle paramAsyncRequestModle) throws IOException {
        //首页banner图片
        HttpPost httpPost = new HttpPost(Urls.list_pub);
        httpPost.addParemeter("pubConlumnId", MsgContext.home_banner);

        paramAsyncRequestModle.setHttpPost(httpPost);
        paramAsyncRequestModle.setParserCla(Banner.class);
        paramAsyncRequestModle.setJiami(false);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle, int type) throws IOException {

        if (type == news) {
            HttpPost httpPost = new HttpPost(Urls.list_pub);
            httpPost.addParemeter("pubConlumnId", MsgContext.home_news);

            asRequestModle.setHttpPost(httpPost);

        } else if (type == hot) {
            //  tag_type  标签 1/推荐	2/热门	3(热门+推荐)
            HttpPost httpPost = new HttpPost(Urls.list_pub);
            httpPost.addParemeter("rootPubConlumnId", MsgContext.home_hot);
            httpPost.addParemeter("tag_type", "2");

            asRequestModle.setHttpPost(httpPost);

        } else if (type == newest) {
            HttpPost httpPost = new HttpPost(Urls.list_pub);
            httpPost.addParemeter("secondPubConlumnId", MsgContext.home_newest);

            asRequestModle.setHttpPost(httpPost);

        }
        asRequestModle.setJiami(false);

    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new HomeModelImpl(jsonObject);
    }

    public void loadBannerData(AsyncCall.OnAsyncBackListener lister) {

        AsyncCall request = Request();
        request.setOnAsyncBackListener(lister);
    }

    /**
     * 首页 顶部文字新闻
     *
     * @param onAsyncBackListener
     */
    public void loadNews(AsyncCall.OnAsyncBackListener onAsyncBackListener) {
        Request(news).setOnAsyncBackListener(onAsyncBackListener);
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public enum Tag {
        in,//里面
        out //弹出
    }
}

/**
 * news
 * {
 * "id" : "5812cc1f78e0802052dd7a14",
 * "name" : "“云南味道”咖啡拉花艺术大赛亮相中国国际食品博览会",
 * "intro" : "MTDmnIgyN+aXpe+8jOS4gOWcuuWIq+W8gOeUn+mdoueahOKAnOS6keWNl+WRs+mBk+KAneWSluWVoeaLieiKseiJuuacr+Wkp+i1m+WwhuWcqOS4reWbveWbvemZhemjn+WTgeWNmuiniOS8mueOsOWcuijkuIrmtbflsZXop4jkuK3lv4Mp5ouJ5byA5bqP5bmV44CC5q2k5qyh5aSn6LWb5piv5Zyo6L+Z5bqn57uP5Y6G5LqGNjHlubTpo47pm6jljoblj7LnmoTogIHlu7rnrZHkuK3kuL7lip7nmoTnrKzkuIDlnLrlhbPkuo7lkpbllaHnmoTmr5TotZvvvIzkuZ/mmK/po5/ljZrkvJrlr7nlkpbllaHotZvkuovlkozlkpbllaHmlofljJblsZXnpLrnmoTnrKzkuIDmrKHlsJ3or5XvvIzorqnmiJHku6zmnaXkuIDotbfnnIvnnIvvvIzov5nlnLrmtLvliqjmnInlk6rkupvnnIvngrnjgIIg",
 * "content" : "MTDmnIgyN+aXpe+8jOS4gOWcuuWIq+W8gOeUn+mdoueahOKAnOS6keWNl+WRs+mBk+KAneWSluWVoeaLieiKseiJuuacr+Wkp+i1m+WwhuWcqOS4reWbveWbvemZhemjn+WTgeWNmuiniOS8mueOsOWcuijkuIrmtbflsZXop4jkuK3lv4Mp5ouJ5byA5bqP5bmV44CC5q2k5qyh5aSn6LWb5piv5Zyo6L+Z5bqn57uP5Y6G5LqGNjHlubTpo47pm6jljoblj7LnmoTogIHlu7rnrZHkuK3kuL7lip7nmoTnrKzkuIDlnLrlhbPkuo7lkpbllaHnmoTmr5TotZvvvIzkuZ/mmK/po5/ljZrkvJrlr7nlkpbllaHotZvkuovlkozlkpbllaHmlofljJblsZXnpLrnmoTnrKzkuIDmrKHlsJ3or5XvvIzorqnmiJHku6zmnaXkuIDotbfnnIvnnIvvvIzov5nlnLrmtLvliqjmnInlk6rkupvnnIvngrnjgII=",
 * "state" : "1",
 * "rootPubConlumnId" : "56ef9534d812a83901549457",
 * "rootPubConlumnName" : "资讯管理",
 * "secondPubConlumnId" : "57fca5d9ef722c216b767c97",
 * "secondPubConlumnName" : "首页内容",
 * "thirdPubConlumnId" : "57fca5ffef722c216b767c99",
 * "thirdPubConlumnName" : "首页文字滚动资讯"
 * }
 * <p>
 * hot
 * <p>
 * {
 * "id" : "5812cc1f78e0802052dd7a14",
 * "name" : "“云南味道”咖啡拉花艺术大赛亮相中国国际食品博览会",
 * "intro" : "MTDmnIgyN+aXpe+8jOS4gOWcuuWIq+W8gOeUn+mdoueahOKAnOS6keWNl+WRs+mBk+KAneWSluWVoeaLieiKseiJuuacr+Wkp+i1m+WwhuWcqOS4reWbveWbvemZhemjn+WTgeWNmuiniOS8mueOsOWcuijkuIrmtbflsZXop4jkuK3lv4Mp5ouJ5byA5bqP5bmV44CC5q2k5qyh5aSn6LWb5piv5Zyo6L+Z5bqn57uP5Y6G5LqGNjHlubTpo47pm6jljoblj7LnmoTogIHlu7rnrZHkuK3kuL7lip7nmoTnrKzkuIDlnLrlhbPkuo7lkpbllaHnmoTmr5TotZvvvIzkuZ/mmK/po5/ljZrkvJrlr7nlkpbllaHotZvkuovlkozlkpbllaHmlofljJblsZXnpLrnmoTnrKzkuIDmrKHlsJ3or5XvvIzorqnmiJHku6zmnaXkuIDotbfnnIvnnIvvvIzov5nlnLrmtLvliqjmnInlk6rkupvnnIvngrnjgIIg",
 * "content" : "MTDmnIgyN+aXpe+8jOS4gOWcuuWIq+W8gOeUn+mdoueahOKAnOS6keWNl+WRs+mBk+KAneWSluWVoeaLieiKseiJuuacr+Wkp+i1m+WwhuWcqOS4reWbveWbvemZhemjn+WTgeWNmuiniOS8mueOsOWcuijkuIrmtbflsZXop4jkuK3lv4Mp5ouJ5byA5bqP5bmV44CC5q2k5qyh5aSn6LWb5piv5Zyo6L+Z5bqn57uP5Y6G5LqGNjHlubTpo47pm6jljoblj7LnmoTogIHlu7rnrZHkuK3kuL7lip7nmoTnrKzkuIDlnLrlhbPkuo7lkpbllaHnmoTmr5TotZvvvIzkuZ/mmK/po5/ljZrkvJrlr7nlkpbllaHotZvkuovlkozlkpbllaHmlofljJblsZXnpLrnmoTnrKzkuIDmrKHlsJ3or5XvvIzorqnmiJHku6zmnaXkuIDotbfnnIvnnIvvvIzov5nlnLrmtLvliqjmnInlk6rkupvnnIvngrnjgII=",
 * "state" : "1",
 * "rootPubConlumnId" : "56ef9534d812a83901549457",
 * "rootPubConlumnName" : "资讯管理",
 * "secondPubConlumnId" : "57fca5d9ef722c216b767c97",
 * "secondPubConlumnName" : "首页内容",
 * "thirdPubConlumnId" : "57fca5ffef722c216b767c99",
 * "thirdPubConlumnName" : "首页文字滚动资讯"
 * }
 * <p>
 * newest
 * <p>
 * {
 * "id" : "5812cc1f78e0802052dd7a14",
 * "name" : "“云南味道”咖啡拉花艺术大赛亮相中国国际食品博览会",
 * "intro" : "MTDmnIgyN+aXpe+8jOS4gOWcuuWIq+W8gOeUn+mdoueahOKAnOS6keWNl+WRs+mBk+KAneWSluWVoeaLieiKseiJuuacr+Wkp+i1m+WwhuWcqOS4reWbveWbvemZhemjn+WTgeWNmuiniOS8mueOsOWcuijkuIrmtbflsZXop4jkuK3lv4Mp5ouJ5byA5bqP5bmV44CC5q2k5qyh5aSn6LWb5piv5Zyo6L+Z5bqn57uP5Y6G5LqGNjHlubTpo47pm6jljoblj7LnmoTogIHlu7rnrZHkuK3kuL7lip7nmoTnrKzkuIDlnLrlhbPkuo7lkpbllaHnmoTmr5TotZvvvIzkuZ/mmK/po5/ljZrkvJrlr7nlkpbllaHotZvkuovlkozlkpbllaHmlofljJblsZXnpLrnmoTnrKzkuIDmrKHlsJ3or5XvvIzorqnmiJHku6zmnaXkuIDotbfnnIvnnIvvvIzov5nlnLrmtLvliqjmnInlk6rkupvnnIvngrnjgIIg",
 * "content" : "MTDmnIgyN+aXpe+8jOS4gOWcuuWIq+W8gOeUn+mdoueahOKAnOS6keWNl+WRs+mBk+KAneWSluWVoeaLieiKseiJuuacr+Wkp+i1m+WwhuWcqOS4reWbveWbvemZhemjn+WTgeWNmuiniOS8mueOsOWcuijkuIrmtbflsZXop4jkuK3lv4Mp5ouJ5byA5bqP5bmV44CC5q2k5qyh5aSn6LWb5piv5Zyo6L+Z5bqn57uP5Y6G5LqGNjHlubTpo47pm6jljoblj7LnmoTogIHlu7rnrZHkuK3kuL7lip7nmoTnrKzkuIDlnLrlhbPkuo7lkpbllaHnmoTmr5TotZvvvIzkuZ/mmK/po5/ljZrkvJrlr7nlkpbllaHotZvkuovlkozlkpbllaHmlofljJblsZXnpLrnmoTnrKzkuIDmrKHlsJ3or5XvvIzorqnmiJHku6zmnaXkuIDotbfnnIvnnIvvvIzov5nlnLrmtLvliqjmnInlk6rkupvnnIvngrnjgII=",
 * "state" : "1",
 * "rootPubConlumnId" : "56ef9534d812a83901549457",
 * "rootPubConlumnName" : "资讯管理",
 * "secondPubConlumnId" : "57fca5d9ef722c216b767c97",
 * "secondPubConlumnName" : "首页内容",
 * "thirdPubConlumnId" : "57fca5ffef722c216b767c99",
 * "thirdPubConlumnName" : "首页文字滚动资讯"
 * }
 */

/**
 * hot
 *
 * {
 "id" : "5812cc1f78e0802052dd7a14",
 "name" : "“云南味道”咖啡拉花艺术大赛亮相中国国际食品博览会",
 "intro" : "MTDmnIgyN+aXpe+8jOS4gOWcuuWIq+W8gOeUn+mdoueahOKAnOS6keWNl+WRs+mBk+KAneWSluWVoeaLieiKseiJuuacr+Wkp+i1m+WwhuWcqOS4reWbveWbvemZhemjn+WTgeWNmuiniOS8mueOsOWcuijkuIrmtbflsZXop4jkuK3lv4Mp5ouJ5byA5bqP5bmV44CC5q2k5qyh5aSn6LWb5piv5Zyo6L+Z5bqn57uP5Y6G5LqGNjHlubTpo47pm6jljoblj7LnmoTogIHlu7rnrZHkuK3kuL7lip7nmoTnrKzkuIDlnLrlhbPkuo7lkpbllaHnmoTmr5TotZvvvIzkuZ/mmK/po5/ljZrkvJrlr7nlkpbllaHotZvkuovlkozlkpbllaHmlofljJblsZXnpLrnmoTnrKzkuIDmrKHlsJ3or5XvvIzorqnmiJHku6zmnaXkuIDotbfnnIvnnIvvvIzov5nlnLrmtLvliqjmnInlk6rkupvnnIvngrnjgIIg",
 "content" : "MTDmnIgyN+aXpe+8jOS4gOWcuuWIq+W8gOeUn+mdoueahOKAnOS6keWNl+WRs+mBk+KAneWSluWVoeaLieiKseiJuuacr+Wkp+i1m+WwhuWcqOS4reWbveWbvemZhemjn+WTgeWNmuiniOS8mueOsOWcuijkuIrmtbflsZXop4jkuK3lv4Mp5ouJ5byA5bqP5bmV44CC5q2k5qyh5aSn6LWb5piv5Zyo6L+Z5bqn57uP5Y6G5LqGNjHlubTpo47pm6jljoblj7LnmoTogIHlu7rnrZHkuK3kuL7lip7nmoTnrKzkuIDlnLrlhbPkuo7lkpbllaHnmoTmr5TotZvvvIzkuZ/mmK/po5/ljZrkvJrlr7nlkpbllaHotZvkuovlkozlkpbllaHmlofljJblsZXnpLrnmoTnrKzkuIDmrKHlsJ3or5XvvIzorqnmiJHku6zmnaXkuIDotbfnnIvnnIvvvIzov5nlnLrmtLvliqjmnInlk6rkupvnnIvngrnjgII=",
 "state" : "1",
 "rootPubConlumnId" : "56ef9534d812a83901549457",
 "rootPubConlumnName" : "资讯管理",
 "secondPubConlumnId" : "57fca5d9ef722c216b767c97",
 "secondPubConlumnName" : "首页内容",
 "thirdPubConlumnId" : "57fca5ffef722c216b767c99",
 "thirdPubConlumnName" : "首页文字滚动资讯"
 }
 */

/**
 * newest
 *
 * {
 "id" : "5812cc1f78e0802052dd7a14",
 "name" : "“云南味道”咖啡拉花艺术大赛亮相中国国际食品博览会",
 "intro" : "MTDmnIgyN+aXpe+8jOS4gOWcuuWIq+W8gOeUn+mdoueahOKAnOS6keWNl+WRs+mBk+KAneWSluWVoeaLieiKseiJuuacr+Wkp+i1m+WwhuWcqOS4reWbveWbvemZhemjn+WTgeWNmuiniOS8mueOsOWcuijkuIrmtbflsZXop4jkuK3lv4Mp5ouJ5byA5bqP5bmV44CC5q2k5qyh5aSn6LWb5piv5Zyo6L+Z5bqn57uP5Y6G5LqGNjHlubTpo47pm6jljoblj7LnmoTogIHlu7rnrZHkuK3kuL7lip7nmoTnrKzkuIDlnLrlhbPkuo7lkpbllaHnmoTmr5TotZvvvIzkuZ/mmK/po5/ljZrkvJrlr7nlkpbllaHotZvkuovlkozlkpbllaHmlofljJblsZXnpLrnmoTnrKzkuIDmrKHlsJ3or5XvvIzorqnmiJHku6zmnaXkuIDotbfnnIvnnIvvvIzov5nlnLrmtLvliqjmnInlk6rkupvnnIvngrnjgIIg",
 "content" : "MTDmnIgyN+aXpe+8jOS4gOWcuuWIq+W8gOeUn+mdoueahOKAnOS6keWNl+WRs+mBk+KAneWSluWVoeaLieiKseiJuuacr+Wkp+i1m+WwhuWcqOS4reWbveWbvemZhemjn+WTgeWNmuiniOS8mueOsOWcuijkuIrmtbflsZXop4jkuK3lv4Mp5ouJ5byA5bqP5bmV44CC5q2k5qyh5aSn6LWb5piv5Zyo6L+Z5bqn57uP5Y6G5LqGNjHlubTpo47pm6jljoblj7LnmoTogIHlu7rnrZHkuK3kuL7lip7nmoTnrKzkuIDlnLrlhbPkuo7lkpbllaHnmoTmr5TotZvvvIzkuZ/mmK/po5/ljZrkvJrlr7nlkpbllaHotZvkuovlkozlkpbllaHmlofljJblsZXnpLrnmoTnrKzkuIDmrKHlsJ3or5XvvIzorqnmiJHku6zmnaXkuIDotbfnnIvnnIvvvIzov5nlnLrmtLvliqjmnInlk6rkupvnnIvngrnjgII=",
 "state" : "1",
 "rootPubConlumnId" : "56ef9534d812a83901549457",
 "rootPubConlumnName" : "资讯管理",
 "secondPubConlumnId" : "57fca5d9ef722c216b767c97",
 "secondPubConlumnName" : "首页内容",
 "thirdPubConlumnId" : "57fca5ffef722c216b767c99",
 "thirdPubConlumnName" : "首页文字滚动资讯"
 }

 */
