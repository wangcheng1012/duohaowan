/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.wlj.base.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import com.wlj.base.RecyclingPagerAdapter;
import com.wlj.base.util.ListUtils;

/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public abstract class ImagePagerAdapter<T> extends RecyclingPagerAdapter {

//    private Context       context;
    private  List<T> imageIdList;

    private int           size;
    private boolean       isInfiniteLoop;

    public ImagePagerAdapter(List<T> imageIdList) {
//        this.context = context;
        this.imageIdList = imageIdList;
        this.size = ListUtils.getSize(imageIdList);
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
    	 return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(imageIdList);
//        return  ListUtils.getSize(imageIdList);
    }

    /**
     * get really position
     * 
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
//        ViewHolder holder;
//        if (view == null) {
//            holder = new ViewHolder();
//            view = holder.imageView = new ImageView(context);
//            view.setTag(holder);
//        } else {
//            holder = (ViewHolder)view.getTag();
//        }
//        holder.imageView.setImageResource(imageIdList.get(getPosition(position)));
        
        return getPageItemview(imageIdList.get(getPosition(position)),view,container);
    }

    public abstract View  getPageItemview(T item, View view, ViewGroup container);

//	private static class ViewHolder {
//
//        ImageView imageView;
//    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
