package com.wlj.base.ob;

/**
 * 主题接口，对象使用此接口注册为观察着，或者把自己重观察者中删除
 * @author wlj
 *
 */
public interface Subject {

	public void registerObserver(Observer arg0);
	public void removeObserver(Observer arg0);
	public void notifyObservers(Object o);
	/**
	 * 
	 */
	public void deleteObservers();
	
}
