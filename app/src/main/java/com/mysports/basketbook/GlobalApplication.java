package com.mysports.basketbook;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.kakao.auth.KakaoSDK;

/**
 * Created by ldong on 2016-06-19.
 */
public class GlobalApplication extends Application{
    private static GlobalApplication mInstance;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();

       // Fabric.with(this, new Crashlytics());
        mInstance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public static Activity getCurrentActivity() {
        Log.d("TAG", "++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    /**
     * singleton
     * @return singleton
     */
    public static GlobalApplication getGlobalApplicationContext() {
        if(mInstance == null)
            throw new IllegalStateException("this application does not inherit GlobalApplication");
        return mInstance;
    }
}