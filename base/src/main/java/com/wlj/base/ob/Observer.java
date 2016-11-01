package com.wlj.base.ob;
/**
 * 观察者必须实现的观察者接口，这个接口只有update（）一个方法，当主题状态改变时它被调用
 * @author wlj
 *
 */
public interface Observer {


	void update(Subject subject, Object o);
}
