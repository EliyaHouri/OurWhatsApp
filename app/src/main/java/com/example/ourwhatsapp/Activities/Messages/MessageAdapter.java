package com.example.ourwhatsapp.Activities.Messages;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ourwhatsapp.R;
import com.example.ourwhatsapp.Utils;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private final List<Message> messages;

    public MessageAdapter(Context context, int resource, List<Message> messages) {
        super(context, resource, messages);
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int reversedPosition = getCount() - 1 - position;
        Message message = messages.get(reversedPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_messages_item, parent, false);
        }

        TextView msgTextView = convertView.findViewById(R.id.msg);
        TextView timeTextView = convertView.findViewById(R.id.time);
        LinearLayout linearLayout = convertView.findViewById(R.id.mainLayout);

        msgTextView.setText(message.getContent());
        timeTextView.setText(Utils.reformatTime(message.getTime()));

        int backgroundResId;
        if (message.getMessageType() == Message.MessageType.SENT) {
            linearLayout.setGravity(Gravity.END);
            backgroundResId = R.drawable.send_chat_bubble_background;
        } else {
            linearLayout.setGravity(Gravity.START);
            backgroundResId = R.drawable.get_chat_bubble_background;
        }

        convertView.findViewById(R.id.constraintLayout).setBackgroundResource(backgroundResId);


        return convertView;
    }
}

