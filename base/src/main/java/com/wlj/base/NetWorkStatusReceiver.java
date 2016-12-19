package com.wlj.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.wlj.base.web.asyn.NetWorkUtils;

public class NetWorkStatusReceiver extends BroadcastReceiver {
 
  public NetWorkStatusReceiver() {
 
  }
 
  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
      int apnType = NetWorkUtils.getAPNType(context);
      if(apnType > 1 ){
        Toast.makeText(context, "网络切换成2G/3G请注意流量", Toast.LENGTH_LONG).show();
      }

    }
  }
}