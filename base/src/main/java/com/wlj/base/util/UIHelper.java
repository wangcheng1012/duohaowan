package com.wlj.base.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.wlj.base.R;
import com.wlj.base.util.Log;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.provider.Settings;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class UIHelper {

    public final static int LISTVIEW_ACTION_INIT = 51;
    public final static int LISTVIEW_ACTION_REFRESH = 52;
    public final static int LISTVIEW_ACTION_SCROLL = 53;
    public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 54;

    private static AlertDialog Progressbardlg;

    /**
     * 弹出Toast消息
     *
     * @param msg
     */
    public static void toastMessage(Context cont, String msg) {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastMessage(Context cont, int msg) {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastMessage(Context cont, String msg, int time) {
        Toast.makeText(cont, msg, time).show();
    }

    private static ProgressDialog progressDialog;

    public static void loading(String str, Activity mContext) {
        loadingClose( );
        progressDialog = ProgressDialog.show(mContext, "提示", str);
    }

    public static void loadingCir(Activity context) {
        loadingClose( );
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public static void loadingClose() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    /**
     * 点击返回监听事件
     *
     * @param activity
     * @return
     */
    public static OnClickListener finish(final Activity activity) {
        return new OnClickListener() {
            public void onClick(View v) {
                activity.finish();
            }
        };
    }

    /**
     * 打开gps提示框
     *
     * @param cont
     * @param crashReport
     */
    public static void GpsOpenTip(final Context cont, final String crashReport) {

        Builder builder = new Builder(cont);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.app_tipr);
        builder.setMessage(crashReport);
        builder.setPositiveButton("幸福地开启",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        cont.startActivity(intent);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("残忍地拒绝",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }


    public static void dialog(Context cont, String message, DialogInterface.OnClickListener positivelistener, DialogInterface.OnClickListener negativelistener) {

        Builder builder = new Builder(cont);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.app_tipr);
        builder.setMessage(message);
        builder.setPositiveButton("确认", positivelistener);
        if(negativelistener != null) {
            builder.setNegativeButton("取消", negativelistener);
        }
        builder.show();
    }

    public static void showProgressbar(Activity paramActivity, DialogInterface.OnCancelListener paramOnCancelListener) {
        closeProgressbar();

        Progressbardlg = new AlertDialog.Builder(paramActivity).create();
        Window localWindow = Progressbardlg.getWindow();
        localWindow.setBackgroundDrawable(new ColorDrawable(0));
        Progressbardlg.show();
        localWindow.setContentView(R.layout.progressbar);
        Progressbardlg.setOnCancelListener(paramOnCancelListener);
    }

    public static void closeProgressbar() {
        if(Progressbardlg != null && Progressbardlg.isShowing()) {
            Progressbardlg.dismiss();
            Progressbardlg = null;
        }
    }

}
