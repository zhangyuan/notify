package com.evcheung.libs.notify.app;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class NotifyApp extends Application{
    public static final String DB_NAME = "notify-db";

    @Override
    public void onCreate() {
        String appId = null;
        String appKey = null;

        try {
            InputStream inputStream = getAssets().open("app.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            appId = properties.getProperty("app_id");
            appKey = properties.getProperty("app_key");
        } catch (IOException e) {
        }

        AVOSCloud.initialize(this, appId, appKey);
        AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
        PushService.setDefaultPushCallback(this, MessagesActivity.class);
        PushService.subscribe(this, "public", MessagesActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground();
    }
}