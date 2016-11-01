package com.wlj.base.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wlj.base.util.Log;

/**
 * 应用程序Fragment的基类
 * 
 * @author
 * @version 1.0
 * @created 2012-9-18
 */
public abstract class BaseFragment extends Fragment {
	protected Context mcontext;
	protected View view;// infalte的布局
	public BaseFragment(){    }
	/*
	 * 创建的时才加载
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 *  SOFT_INPUT_ADJUST_NOTHING:         不调整(输入法完全直接覆盖住,未开放此参数)
			SOFT_INPUT_ADJUST_PAN:                 把整个Layout顶上去露出获得焦点的EditText,不压缩多余空间，见图1
			SOFT_INPUT_ADJUST_RESIZE:            整个Layout重新编排,重新分配多余空间，见图2
			SOFT_INPUT_ADJUST_UNSPECIFIED:  系统自己根据内容自行选择上两种方式的一种执行(默认配置)
		 */
//		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		
		mcontext = getActivity().getApplicationContext();
		Log.w("dd", "onCreate");
	}

	/**
	 * 每次加载 都会调用onCreateView
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.w("dd", "onCreateView");
		if (null == view) {
			view = inflater.inflate(getlayout(), null);
			view.setMinimumHeight(((WindowManager) getActivity()
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay().getHeight());
			view.setMinimumWidth(((WindowManager) getActivity()
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay().getWidth());
			initView();
		} else {
			FrameLayout viewParent = (FrameLayout) view.getParent();
			if(viewParent != null){
				viewParent.removeAllViews();
				viewParent = new FrameLayout(mcontext);
				viewParent.addView(view);
				return viewParent;
			}
		}
		
		return view;
	}

	/**
	 * Fragment的布局
	 * 
	 * @return
	 */
	protected abstract int getlayout();

	protected abstract void initView();
}
