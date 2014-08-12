package com.evcheung.libs.notify.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.evcheung.libs.notify.app.models.Message;

import java.util.ArrayList;

public class MessagesActivity extends ActionBarActivity {
    ArrayList<Message> messages = new ArrayList<Message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        ListView messageListView = (ListView) findViewById(R.id.listView);

        String data = getIntent().getStringExtra("com.avos.avoscloud.Data");

        if (data != null){
            messages.add(new Message("", data));
        }

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, messages);
        messageListView.setAdapter(adapter);
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

}
