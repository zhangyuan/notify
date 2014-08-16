package com.evcheung.libs.notify.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.evcheung.libs.notify.app.dao.DaoMaster;
import com.evcheung.libs.notify.app.dao.DaoSession;
import com.evcheung.libs.notify.app.dao.MessageDao;
import com.evcheung.libs.notify.app.models.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends ActionBarActivity {
    public static String UPDATE_MESSAGES = "com.evcheung.libs.notify.app.UPDATE_MESSAGES";

    List<Message> messages = new ArrayList<Message>();
    private DaoSession daoSession;

    private IntentFilter filter = new IntentFilter(UPDATE_MESSAGES);
    private MessageReceiver receiver = new MessageReceiver();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        initializeSession();

        registerReceiver(receiver, filter);

        ListView messageListView = (ListView) findViewById(R.id.listView);

        adapter = new MessagesListAdapter(getApplicationContext(), R.layout.messages_list_entry, messages);

        updateMessages();
        messageListView.setAdapter(adapter);
    }

    private void updateMessages() {
        MessageDao messageDao = daoSession.getMessageDao();
        List<com.evcheung.libs.notify.app.dao.Message> localMessages = messageDao.queryBuilder().orderDesc(MessageDao.Properties.Id).list();

        messages.clear();
        for (int i = 0; i < localMessages.size(); i ++) {
            com.evcheung.libs.notify.app.dao.Message localMessage = localMessages.get(i);
            messages.add(new Message(localMessage.getId().toString(), localMessage.getTitle(), localMessage.getContent()));
        }
        adapter.notifyDataSetChanged();
    }

    private void initializeSession() {
        SQLiteDatabase db;
        DaoMaster daoMaster;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, NotifyApp.DB_NAME, null);
        db = helper.getReadableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    class MessageReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            updateMessages();
        }
    }
}
