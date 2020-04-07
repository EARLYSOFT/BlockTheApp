package kr.co.earlysoft.sample;

import android.app.Application;

import kr.co.earlysoft.blocktheapp.EarlyActivityLifeCycleCallbacks;

public class ExtendApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new EarlyActivityLifeCycleCallbacks(getApplicationContext()));
    }

}
