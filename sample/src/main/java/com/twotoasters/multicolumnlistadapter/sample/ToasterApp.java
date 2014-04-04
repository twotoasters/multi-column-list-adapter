package main.sample;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

public class ToasterApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this, false);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
