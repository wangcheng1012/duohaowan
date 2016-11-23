package com.hd.wlj.duohaowan.view;


import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.view.imagezoom.ImageViewTouch;
import com.orhanobut.logger.Logger;

/**
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyDialogFragment extends DialogFragment {


    public static MyDialogFragment newInstance(String strurl) {

        Bundle args = new Bundle();
        args.putString("url", strurl);

        MyDialogFragment fragment = new MyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_touchimageview, null);

        ImageViewTouch  imageviewtouch = (ImageViewTouch) view.findViewById(R.id.dialog_touchimage);

        imageviewtouch.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                dismiss();
            }
        });

        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        Glide.with(this)
                .load(url)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Logger.i("isFromMemoryCache:%s,isFirstResource:%s",isFromMemoryCache,isFirstResource);
                        return false;
                    }
                })
                .into(imageviewtouch);

        return view;
    }

}
