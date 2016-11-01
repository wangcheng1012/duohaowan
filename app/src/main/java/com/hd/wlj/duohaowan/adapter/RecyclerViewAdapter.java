package com.hd.wlj.duohaowan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wlj on 2016/10/28.
 */

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private int reslayout;
    private List<T> mData;

    public RecyclerViewAdapter(int reslayout){

        reslayout = reslayout;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T t = mData.get(position);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(reslayout, null);

        return new ViewHolder(inflate);
    }

    @Override
    public int getItemViewType(int position) {


        return super.getItemViewType(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
