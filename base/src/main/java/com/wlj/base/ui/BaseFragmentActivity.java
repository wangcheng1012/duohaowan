package com.wlj.base.ui;

import com.wlj.base.R;
import com.wlj.base.util.AppManager;
import com.wlj.base.util.statusbar.StatusBarUtil;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * 应用程序Activity的基类
 * 
 * @author
 * @version 1.0
 * @created 2012-9-18
 */

public class BaseFragmentActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		AppManager.getAppManager().addActivity(this);

	}

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		super.setContentView(layoutResID);
		setStatusBar();
	}

	protected void setStatusBar() {

		StatusBarUtil.setTranslucent(this, 127);

	};

	@Override
	protected void onPause() {
		System.gc();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		// 其实这里什么都不要做
		super.onConfigurationChanged(newConfig);
	}
}
