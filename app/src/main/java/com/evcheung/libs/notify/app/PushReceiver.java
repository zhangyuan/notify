package com.evcheung.libs.notify.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.evcheung.libs.notify.app.dao.DaoMaster;
import com.evcheung.libs.notify.app.dao.DaoSession;
import com.evcheung.libs.notify.app.dao.Message;
import com.evcheung.libs.notify.app.dao.MessageDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "PushReceiver";

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private MessageDao messageDao;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Bundle bundle = intent.getExtras();

        if (bundle == null) {
            return;
        }
        String channel = bundle.getString("com.avos.avoscloud.Channel");

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, NotifyApp.DB_NAME, null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        messageDao = daoSession.getMessageDao();

        String raw_data = intent.getExtras().getString("com.avos.avoscloud.Data");
        Log.d(TAG, raw_data);

        JSONObject json;
        String messageType;
        try {
            json = new JSONObject(raw_data);
            messageType = json.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        if (messageType.equals("message")) {
            Message message = saveMessage(json);
            if (message != null) {
                Intent updateMessageIntent = new Intent();
                updateMessageIntent.setAction(MessagesActivity.UPDATE_MESSAGES);
                context.sendBroadcast(updateMessageIntent);

                notifyMessage(context, message);
            }
        }

        db.close();

        Log.d(TAG, "got action " + action + " on channel " + channel + " with: ");

    }

    private void notifyMessage(Context context, Message message) {
        NotificationManager notificationManger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        final String messageActivityClassName = MessagesActivity.class.getName();
        int icon = R.drawable.abc_ic_go;
        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, "Notify", when);

        Intent notificationIntent = new Intent(context, MessagesActivity.class);
        PendingIntent pendIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, message.getTitle(), message.getContent(), pendIntent);
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        Log.d(TAG, "notify " + messageActivityClassName);
        notificationManger.notify(0, notification);
    }

    public Message saveMessage(JSONObject json) {
        long id;
        try {
            id = json.getLong("id");
            String title = json.getString("title");
            String content = json.getString("content");

            QueryBuilder qb = messageDao.queryBuilder();
            List list = qb.where(MessageDao.Properties.Id.eq(id)).limit(1).build().list();

            Message message;
            if (list.size() > 0) {
                message = (Message) list.get(0);
                message.setTitle(title);
                message.setContent(content);
                messageDao.update(message);
            } else {
                message = new Message(id, title, content, new Date());
                messageDao.insert(message);
            }

            Log.d(TAG, "save message");

            return message;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
