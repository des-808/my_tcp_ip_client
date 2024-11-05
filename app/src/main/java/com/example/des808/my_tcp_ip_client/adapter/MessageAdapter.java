package com.example.des808.my_tcp_ip_client.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.des808.my_tcp_ip_client.Chat;
import com.example.des808.my_tcp_ip_client.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {



    public interface OnItemClickListener {
        void onItemSelected(int position,View view, Chat chat);
    }

    public interface OnItemLongClickListener {
        boolean onItemSelected(int position,View view, Chat chat);
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        final TextView messageInView,timestampInView,messageOutView,timestampOutView;
        final View root_in,root_out,llIn,llOut;

        public ViewHolder(@NonNull View viewInOut) {
            super(viewInOut);
            llIn = viewInOut.findViewById(R.id.llIn);
            llOut = viewInOut.findViewById(R.id.llOut);
            root_out = viewInOut.findViewById(R.id.root_out);
            root_in = viewInOut.findViewById(R.id.root_in);
            messageInView = viewInOut.findViewById(R.id.messageIn);
            timestampInView = viewInOut.findViewById(R.id.time_stampIn);
            messageOutView = viewInOut.findViewById(R.id.messageOut);
            timestampOutView = viewInOut.findViewById(R.id.time_stampOut);
            int position2 = getAbsoluteAdapterPosition();
            int position3 = getLayoutPosition();

            //int position2 = getLayoutPosition();
            int finalPosition = position2;
            if(position2 == -1){position2 = 1;}
            final int position = position2;

            viewInOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClickListener!=null){
                        Log.d("MessageAdapterClick", "click "+ finalPosition);
                        mOnItemClickListener.onItemSelected(position,view,mDataSet.get(position));
                    }
                }
            });

            viewInOut.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(mOnItemLongClickListener!=null){
                        Log.d("MessageAdapterLongClick", "LongClick "+ finalPosition);
                        return mOnItemLongClickListener.onItemSelected(position,view,mDataSet.get(position));
                    }
                    return true;
                }
            });
        }
    }

    private ArrayList<Chat> mDataSet;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    @SuppressLint("NotifyDataSetChanged")
    public MessageAdapter( ArrayList<Chat> msg) {
        this.mDataSet = msg;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataSet(ArrayList<Chat> msg) {
        this.mDataSet = msg;
        notifyDataSetChanged();
    }

    //вызывается один раз
    //создает и возвращает для каждого элемента из источника данных ссылку на представление
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_in_out, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mDataSet.get(position);
        if (chat.isOutgoing()) {
            holder.llIn.setVisibility(View.GONE);
            holder.llOut.setVisibility(View.VISIBLE);
            holder.root_in.setVisibility(View.GONE);
            holder.root_out.setVisibility(View.VISIBLE);
            holder.messageInView.setVisibility(View.GONE);
            holder.timestampInView.setVisibility(View.GONE);
            holder.messageOutView.setVisibility(View.VISIBLE);
            holder.timestampOutView.setVisibility(View.VISIBLE);
            holder.messageOutView.setText(chat.getMessage_out());
            holder.timestampOutView.setText(chat.getMessage_time());
        } else {
            holder.llIn.setVisibility(View.VISIBLE);
            holder.llOut.setVisibility(View.GONE);
            holder.root_in.setVisibility(View.VISIBLE);
            holder.root_out.setVisibility(View.GONE);
            holder.messageOutView.setVisibility(View.GONE);
            holder.timestampOutView.setVisibility(View.GONE);
            holder.messageInView.setVisibility(View.VISIBLE);
            holder.timestampInView.setVisibility(View.VISIBLE);
            holder.messageInView.setText(chat.getMessage_in());
            holder.timestampInView.setText(chat.getMessage_time());
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

}


/*public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {
    interface OnChatClickListener {
        void onUserClick(Chat chat, int position);
    }
    private final OnChatClickListener onChatClickListener;
    private final LayoutInflater inflater;
    private final List<Chat> chats;
    ChatRecyclerAdapter(Context context, List<Chat> chats, OnChatClickListener onChatClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.chats = chats;
        this.onChatClickListener = onChatClickListener;
    }
    //вызывается один раз
//создает и возвращает для каждого элемента из источника данных ссылку на представление
    @NonNull
    @Override
    public ChatRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_recycler_view_in_out, parent, false);
        return new ViewHolder(view);
    }

    //привязка данных элемента из источника к представлению и его элементам интерфейса
    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Chat chat = chats.get(position);
        holder.photoView.setImageResource(user.getPhotoResource());
        holder.firstNameView.setText(chat.getFirstName());
        holder.lastNameView.setText(chat.getLastName());
        holder.ageView.setText(String.valueOf(chat.getAge()));
        holder.itemView.setOnClickListener(v -> onChatClickListener.onUserClick(chat, position));
    }
    //для проверки наличия данных в источнике
    @Override
    public int getItemCount() {
        return chats.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView photoView;
        final TextView firstNameView, lastNameView, ageView;
        ViewHolder(View view) {
            super(view);
            photoView = view.findViewById(R.id.photo_image_view);
            firstNameView = view.findViewById(R.id.firstname_text_view);
            lastNameView = view.findViewById(R.id.lastname_text_view);
            ageView = view.findViewById(R.id.age_text_view);
        }
    }
}*/
