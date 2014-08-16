package com.evcheung.libs.notify.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evcheung.libs.notify.app.models.Message;

import java.util.List;

public class MessagesListAdapter extends ArrayAdapter<Message> {
    private final List<Message> messages;
    private final int resource;

    public MessagesListAdapter(Context context, int resource, List<Message> messages) {
        super(context, resource, messages);
        this.messages = messages;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout newView;
        Message message = messages.get(position);

        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(inflater);
            layoutInflater.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }

        final TextView titleTextView = (TextView) newView.findViewById(R.id.title);
        titleTextView.setText(message.getTitle());

        final TextView contentTextView = (TextView) newView.findViewById(R.id.content);
        contentTextView.setText(message.getContent());

        return newView;
    }
}
