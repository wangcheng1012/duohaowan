package com.hd.wlj.third.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.hd.wlj.third.R;
import com.hd.wlj.third.share.qq.QQshare;
import com.hd.wlj.third.share.sina.SinaShare;
import com.hd.wlj.third.share.weixin.WeiXinShare;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.wlj.base.adapter.CommonAdapter;
import com.wlj.base.adapter.ViewHolder;
import com.wlj.base.bean.Base;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.MathUtil;
import com.wlj.base.util.img.ImageFileCache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShareUI {
    private Activity activity;
    private CommonAdapter<Base> commonAdapter;
    private TextView fenXiangView;
    private List<Base> mDatas;
    public QQshare mQQshare;
    private ShareModle mShareModle;
    private SinaShare mSinaShare;
    private WeiXinShare mWeiXinShare;
    private String[] names = {"QQ好友","QQ空间","腾讯微博","新浪微博","微信好友","朋友圈",};
    private int[] pics = {R.drawable.logo_qq,R.drawable.logo_qzone,R.drawable.logo_tencentweibo,R.drawable.logo_sinaweibo,R.drawable.logo_wechat,R.drawable.logo_wechatmoments,};

    private Animation translate_Animation;

    public ShareUI(Activity activity,ShareModle mShareModle){
        this.activity = activity;
        this.mShareModle = mShareModle;

        translate_Animation = AnimationUtils.loadAnimation(activity,R.anim.slide_out_bottom);

        mQQshare = new QQshare(activity,mShareModle);
        mWeiXinShare = new WeiXinShare(activity);

        mDatas = new ArrayList<Base>();

        for (int i = 0; i < names.length; i++) {

            ShareModle sm = new ShareModle();
            sm.setName(names[i]);
            sm.setResPic(pics[i]);
            mDatas.add(sm);
        }

//        mSinaShare = new SinaShare() {
//            @Override
//            public void onnewintent(IWeiboShareAPI paramIWeiboShareAPI) {
//
//            }
//        };
    }

    private void setTopDrawable(TextView paramTextView) {
        Drawable localDrawable = paramTextView.getContext().getResources().getDrawable(MathUtil.parseInteger(paramTextView.getTag() + ""));
        localDrawable.setBounds(0, 0, localDrawable.getMinimumWidth(), localDrawable.getMinimumHeight());
        paramTextView.setCompoundDrawables(null, localDrawable, null, null);
    }

    public void callwebShouChang(String paramString, View paramView, int paramInt) {

    }

    public TextView getFenXiangView() {
        return this.fenXiangView;
    }

    public void setFenXiangView(TextView paramTextView) {
        this.fenXiangView = paramTextView;
    }


    public void showdialog(Boolean paramBoolean) {
        final Dialog localDialog = new Dialog(this.activity, R.style.dialog);
        View localView = LayoutInflater.from(activity).inflate(R.layout.share, null);
        localDialog.setContentView(localView);
        localDialog.show();
        Display localDisplay = this.activity.getWindowManager().getDefaultDisplay();
        LayoutParams localLayoutParams = localDialog.getWindow().getAttributes();
        localLayoutParams.height = (localDisplay.getHeight() - DpAndPx.dpToPx(this.activity, 20.0F));
        localLayoutParams.width = localDisplay.getWidth();
        localDialog.getWindow().setAttributes(localLayoutParams);
        localView.findViewById(R.id.nulltr).setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {

                localDialog.dismiss();
            }
        });
        localView.findViewById(R.id.space).setAnimation(this.translate_Animation);
        localView.findViewById(R.id.space).startAnimation(this.translate_Animation);
        localView.findViewById(R.id.canl).setOnClickListener(new OnClickListener() {

            public void onClick(View paramAnonymousView) {
                localDialog.dismiss();
            }
        });

        TextView localTextView = (TextView) localView.findViewById(R.id.shouchang_xx);
        if (!paramBoolean.booleanValue()) {
            int i = R.drawable.shouchang_xx;
            localTextView.setTag(Integer.valueOf(i));
            setTopDrawable(localTextView);
            localTextView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {

                    String str = Constants.shoucang;
                    int j = R.drawable.shouchang_xx;

                    int i = Integer.parseInt(v.getTag() + "");

                    if (R.drawable.shouchang_xx == i) {

                        j = R.drawable.shoucang_xx_red;
                    } else {
                        str = Constants.shoucang_remove;
                    }

                    ShareUI.this.callwebShouChang(str, v, j);

                }
            });
        }
        //复制链接
        localView.findViewById(R.id.fuzhilianjie).setOnClickListener(new OnClickListener() {

            public void onClick(View paramAnonymousView) {

                Activity localActivity = ShareUI.this.activity;
                ((ClipboardManager) localActivity.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("simple text", Constants.qq_targetUrl + mShareModle.getId()));
                localDialog.dismiss();
            }
        });

        GridView localGridView = (GridView) localView.findViewById(R.id.sharegridView1);
        this.commonAdapter = new CommonAdapter<Base>(this.activity, this.mDatas, R.layout.item_share_gridview) {

            public View getListItemview(ViewHolder paramAnonymousViewHolder, View v, Base base, int paramAnonymousInt, ViewGroup viewGroup) {

                ShareModle localShareModle = (ShareModle) base;
                paramAnonymousViewHolder.setText(R.id.textView1, localShareModle.getName());
                paramAnonymousViewHolder.setImageResource(R.id.imageview, localShareModle.getResPic());
                v.setTag(R.id.tag_first, base);
                return null;
            }
        };
        localGridView.setAdapter(this.commonAdapter);
        localGridView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View view, int postion, long paramAnonymousLong) {
                switch (postion) {
                    case 0:
                        ShareUI.this.mQQshare.login(0);
                        break;
                    case 1:
                        ShareUI.this.mQQshare.login(1);
                        break;
                    case 2:
                        ShareUI.this.mQQshare.login(2);
                        break;
                    case 3:
                        mSinaShare.sendMultiMessage();
                        break;
                    case 4:
                        //微信
                        weixinShare(false);
                        break;
                    case 5:
                        //微信盆友圈
                        weixinShare(true);
                        break;

                }

            }

            private void weixinShare(Boolean isFriend) {
                mWeiXinShare.setPengyouquan(isFriend);
                String str3 = new String(Base64.decode(mShareModle.getContent(), 0)).replaceAll("<[^>]*>", "");
                String str4 = new ImageFileCache().getPath("http://121.40.177.251:6940" + mShareModle.getPic());
                if (!new File(str4).exists()) {
                    mWeiXinShare.sendText(str3);
                } else {
                    mWeiXinShare.sendWebPage(Constants.fenxiangtarget + mShareModle.getId(), mShareModle.getName(), str3, BitmapFactory.decodeFile(str4));
                }
            }


        });


    }

}