package com.cw.indexlistview;

import android.app.Application;
import android.content.Context;

public class IndexListviewApplication extends Application {

    public static IndexListviewApplication instance = null;
    public static Context mContext;
    
    public IndexListviewApplication() {
        mContext = this;
    }

    public static Context getContext()
    {
        if (null == mContext) {
            new IndexListviewApplication();
        }
        return mContext;
    }

    public static IndexListviewApplication getInstance()
    {
        if (instance == null) {
            instance = new IndexListviewApplication();
        }
        return instance;
    }

}
