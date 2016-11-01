package com.wlj.base.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

/**
 * 页面不缓存
 * 会保存当前界面，以及下一个界面和上一个界面（如果有），最多保存3个，其他会被销毁掉。 
 * @author wlj
 *
 */
public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> list;
	public MyFragmentStatePagerAdapter(FragmentManager fm,List<Fragment> _list) {
		super(fm);
		list = _list;
	}
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 * 
	 * <li>一个该类中新增的虚函数。</li>
	 * <li>函数的目的为生成新的 Fragment 对象。</li>
	 *	<li>Fragment.setArguments() 这种只会在新建 Fragment 时执行一次的参数传递代码，可以放在这里。</li>
	 *	<li>由于 FragmentStatePagerAdapter.instantiateItem() 在大多数情况下，都将调用 getItem() 来生成新的对象，
	 *		因此如果在该函数中放置与数据集相关的 setter 代码，基本上都可以在 instantiateItem() 被调用时执行，但这和
	 *		设计意图不符。毕竟还有部分可能是不会调用 getItem() 的。因此这部分代码应该放到 instantiateItem() 中</li>
	 */
	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#instantiateItem(android.view.ViewGroup, int)
	 * 
	 *1.除非碰到 FragmentManager 刚好从 SavedState 中恢复了对应的 Fragment 的情况外，该函数将会调用 getItem() 函数，生成新的 Fragment 对象。新的对象将被 FragmentTransaction.add()。
	 *2.FragmentStatePagerAdapter 就是通过这种方式，每次都创建一个新的 Fragment，而在不用后就立刻释放其资源，来达到节省内存占用的目的的。
	 */
	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {
		// TODO Auto-gene rated method stub
		return super.instantiateItem(arg0, arg1);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#destroyItem(android.view.ViewGroup, int, java.lang.Object)
	 * 
	 * 将 Fragment 移除，即调用 FragmentTransaction.remove()，并释放其资源
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}
	
	@Override
	public int getItemPosition(Object object) {
//		super.getItemPosition(object); 默认为 POSITION_UNCHANGED
//		PagerAdapter.POSITION_UNCHANGED   什么都不做
//		PagerAdapter.POSITION_NONE 则调用 PagerAdapter.destroyItem() 来去掉该对象，
//		并设置为需要刷新 (needPopulate = true) 以便触发PagerAdapter.instantiateItem() 来生成新的对象
		return PagerAdapter.POSITION_NONE;
	}
}
