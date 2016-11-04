package com.wlj.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by wlj on 2016/10/28.
 */

public class GoToHelp {


    public static Intent go(Activity mContent, Class<?> cls) {
        return go(mContent, cls, null);
    }

    public static Intent go(Activity mContent, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(mContent, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContent.startActivity(intent);
        return intent;
    }
}
