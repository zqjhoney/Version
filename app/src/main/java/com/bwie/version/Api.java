package com.bwie.version;

import android.app.Application;

import org.xutils.x;

/**
 * Created by 张乔君 on 2017/9/13.
 */

public class Api extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.setDebug(true);

        x.Ext.init(this);

    }
}
