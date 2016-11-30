package com.wlj.base.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.squareup.leakcanary.RefWatcher;
import com.wlj.base.util.AppContext;
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

    public BaseFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.w("dd", "onAttach " + getClass().getSimpleName() + getId());
    }

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
        Log.w("dd", "onCreate " + getClass().getSimpleName() + getId());
    }


    /**
     * 每次加载 都会调用onCreateView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view) {
        Log.w("dd", "onCreateView inflate  " + getClass().getSimpleName() + +getId());
            view = inflater.inflate(getlayout(), container,false);
            view.setMinimumHeight(((WindowManager) getActivity()
                    .getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getHeight());
            view.setMinimumWidth(((WindowManager) getActivity()
                    .getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getWidth());
        } else {
            Log.w("dd", "onCreateView 复用 " + getClass().getSimpleName() + +getId());
            ViewGroup viewParent = (ViewGroup) view.getParent();
            if (viewParent != null) {
                viewParent.removeAllViews();
//				viewParent = new FrameLayout(mcontext);
                viewParent.addView(view);
                return viewParent;
            }
        }
        initView();
        return view;
    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//
//        Log.w("dd", "onDestroyView " + getClass().getSimpleName() + getId());
//
//        FragmentManager f = getFragmentManager();
//        if (f != null && !f.isDestroyed()) {
//            final FragmentTransaction ft = f.beginTransaction();
//            if (ft != null) {
//                ft.remove(this).commit();
//            }
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = AppContext.getAppContext().getRefWatcher();
        refWatcher.watch(this);
        Log.w("dd", "onDestroy " + getClass().getSimpleName() + getId());
    }

    /**
     * Fragment的布局
     *
     * @return
     */
    protected abstract int getlayout();

    protected abstract void initView();
}
