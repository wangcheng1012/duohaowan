package com.wlj.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by wlj on 2016/10/28.
 */

public class GoToHelp {


    public static Intent go(Activity mContent, Class<?> cls) {
        Intent intent = new Intent(mContent, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContent.startActivity(intent);
        return intent;
    }
}
