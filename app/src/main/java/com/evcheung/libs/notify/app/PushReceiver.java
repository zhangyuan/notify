package com.evcheung.libs.notify.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.evcheung.libs.notify.app.dao.DaoMaster;
import com.evcheung.libs.notify.app.dao.DaoSession;
import com.evcheung.libs.notify.app.dao.Message;
import com.evcheung.libs.notify.app.dao.MessageDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "PushReceiver";
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private MessageDao messageDao;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "notes-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        messageDao = daoSession.getMessageDao();

        try {
            Log.d(TAG, (intent.getExtras().getString("com.avos.avoscloud.Data")));

            JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));

            if (json.getString("type").equals("message")) {
                String title = json.getString("title");
                String content = json.getString("content");

                Message message = new Message(null, title, content, new Date());

                messageDao.insert(message);
                Log.d(TAG, "insert message");
            }

            Log.d(TAG, "got action " + action + " on channel " + channel + " with:");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
