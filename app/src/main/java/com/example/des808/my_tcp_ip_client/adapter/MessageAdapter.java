package com.example.des808.my_tcp_ip_client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.des808.my_tcp_ip_client.MessageChat;
import com.example.des808.my_tcp_ip_client.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private final LayoutInflater inflater;
    private final List<MessageChat> messages;

    public MessageAdapter(Context context, List<MessageChat> msg) {
        this.messages = msg;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_recycler_view, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        MessageChat chat = messages.get(position);
        holder.messageView.setText(chat.getMessage());
        holder.timestampView.setText(chat.getTime());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        final TextView messageView,timestampView;
        MessageViewHolder(View itemView) {
            super(itemView);
            messageView = itemView.findViewById(R.id.message);
            timestampView = itemView.findViewById(R.id.time_stamp);
        }
    }
}
