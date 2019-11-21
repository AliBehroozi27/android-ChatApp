package com.example.ruzbeh.chatapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruzbeh.chatapp.Activities.ChatListActivity;
import com.example.ruzbeh.chatapp.Modules.User;
import com.example.ruzbeh.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListRVAdapter extends RecyclerView.Adapter<ChatListRVAdapter.ContactViewHolder> {
    public Context context;
    public List<User> contacts;

    public ChatListRVAdapter(Context context, List<User> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contact_item_view, null, false);
        return new ContactViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        User contact = contacts.get(position);
        holder.username.setText(contact.username);
        if (false || contact.online) {
            holder.online.setText("online");
        } else {
            holder.online.setText("offline");
            holder.online.setTextColor(Color.rgb(200, 200, 200));
        }
        holder.profilePic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_android));
        holder.id.setText(contact.user_id);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView username, online ,id;
        ImageView profilePic;

        public ContactViewHolder(final View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_userNameItemView);
            online = itemView.findViewById(R.id.tv_onlineItemView);
            profilePic = itemView.findViewById(R.id.iv_contactPicItemView);
            id = itemView.findViewById(R.id.tv_idItemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView contactId = (TextView) view.findViewById(R.id.tv_idItemView);
                    ((ChatListActivity)context).openChat(contactId.getText().toString());
                }
            });
        }
    }
}
