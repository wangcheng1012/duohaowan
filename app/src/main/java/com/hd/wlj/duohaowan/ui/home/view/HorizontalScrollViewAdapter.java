package com.hd.wlj.duohaowan.ui.home.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.wlj.base.bean.Base;
import com.wlj.base.util.img.LoadImage;

import org.json.JSONObject;

public class HorizontalScrollViewAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Base> mDatas;

    public HorizontalScrollViewAdapter(Context context, List<Base> mDatas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount() {
        return mDatas.size();
    }

    public Object getItem(int position) {
        return mDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_home_hot, parent, false);
            viewHolder.mImg = (ImageView) convertView.findViewById(R.id.home_hot_img);
            int width = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
            viewHolder.mImg.setLayoutParams(new LinearLayout.LayoutParams(width/2-150, LinearLayout.LayoutParams.MATCH_PARENT));

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //
        Base base = mDatas.get(position);

        JSONObject resultJsonObject = base.getResultJsonObject();

        String pics = resultJsonObject.optString("pic");
        if (pics != null && pics.length() > 0) {
            LoadImage.getinstall().addTask(Urls.HOST + pics, viewHolder.mImg);
            LoadImage.getinstall().doTask();
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView mImg;
    }

}