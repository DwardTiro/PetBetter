package com.example.owner.petbetter;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by owner on 6/11/2017.
 */
public class Multi_Dex extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
