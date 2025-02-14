package com.example.ourwhatsapp.Activities.Conversations;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.ourwhatsapp.R;
import com.example.ourwhatsapp.Utils;

import java.util.ArrayList;

public class ConversationsAdapter extends ArrayAdapter<Conversation> {
    private final LayoutInflater inflater;


    public ConversationsAdapter(Context ctx, ArrayList<Conversation> conversationArrayList) {
        super(ctx, R.layout.custom_conversation_item, conversationArrayList);

        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        Conversation conversation = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_conversation_item, parent, false);
        }

        try {
            Utils.displayBase64Image(conversation.getProfilePicture(), convertView.findViewById(R.id.profile_image));
        } catch (Exception ignored) {
            ((ImageView)convertView.findViewById(R.id.profile_image)).setImageResource(R.drawable.default_img);
        }
        ((TextView)convertView.findViewById(R.id.user_name)).setText(conversation.getUserName());
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            // Night mode is enabled
            ((TextView)convertView.findViewById(R.id.user_name)).setTextColor(Color.WHITE);
        } else {
            // Night mode is disabled
            ((TextView)convertView.findViewById(R.id.user_name)).setTextColor(Color.BLACK);
        }

        ((TextView)convertView.findViewById(R.id.last_massage)).setText(conversation.getLastMassage());
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            // Night mode is enabled
            ((TextView)convertView.findViewById(R.id.last_massage)).setTextColor(Color.WHITE);
        } else {
            // Night mode is disabled
            ((TextView)convertView.findViewById(R.id.last_massage)).setTextColor(Color.BLACK);
        }

        ((TextView)convertView.findViewById(R.id.time)).setText(Utils.reformatTime(conversation.getLastMassageSendingTime()));
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            // Night mode is enabled
            ((TextView)convertView.findViewById(R.id.time)).setTextColor(Color.WHITE);
        } else {
            // Night mode is disabled
            ((TextView)convertView.findViewById(R.id.time)).setTextColor(Color.BLACK);
        }

        if (conversation.getHasNewMessage() >= 1) {
            convertView.setBackgroundColor(Color.parseColor("#FFFFEB3B"));
        }

        return convertView;
    }
}
