package com.wlj.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * date 2015-5-5
 * @author Administrator
 * @param <T>
 * 通用adapter，不需要更改
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	protected LayoutInflater mInflater;
	protected Context mContext;
	public List<T> mDatas;
	protected final int mItemLayoutId;

	public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder = getViewHolder(position, convertView,
				parent);
			View view = getListItemview(viewHolder,viewHolder.getConvertView(), getItem(position),
					position, parent);
			if (view == null) {
				return viewHolder.getConvertView();
			} else {
				return view;
			}
	}

	public ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
				position);
	}

	public void addDatas(List list) {// 数据追加
		if (mDatas != null)
			mDatas.addAll(list);
		this.notifyDataSetChanged();
	}
	public void add(int position,T t){
		mDatas.add(position, t);
	}
	/**
	 * <pre>
	 * 返回列表项,作事件处理
	 * @param view itemView
	 * @param item itemdate
	 * @param position 
	 * @return 返回的view不为null时为adapter填充数据
	 * 
	 * <pre/>
	 */
	public abstract View getListItemview(ViewHolder viewHolder, View view,
			T item, int position, ViewGroup parent);
}

