package com.robust.toney;

import android.app.Application;
import android.content.Context;


import com.robust.toney.patch.utils.PatchProxy;


import java.io.File;


/**
 * Created by toney on 2017/2/9.
 */

public class MainApp extends Application {



    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        new PatchProxy(getExternalCacheDir().getParentFile().getParentFile().getParentFile().getParentFile().getAbsolutePath() + File.separator+"patch.dex",base).patch();
    }

}
