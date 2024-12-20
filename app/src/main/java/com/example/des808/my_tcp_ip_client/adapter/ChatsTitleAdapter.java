package com.example.des808.my_tcp_ip_client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.des808.my_tcp_ip_client.R;
import com.example.des808.my_tcp_ip_client.TitleChatsItems;

import java.util.ArrayList;

public class ChatsTitleAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private static ArrayList<TitleChatsItems>data;

    public ChatsTitleAdapter(Context context, ArrayList<TitleChatsItems>data){
        inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        this.data = data;
    }
    //public ChatsTitleAdapter(fragment_titles fragment_titles, ArrayList<adapter_listview> list) { }

    @Override
    public int getCount(){ return data.size(); }

    @Override
    public TitleChatsItems getItem(int position){ return data.get(position); }

    //@Override
    public static TitleChatsItems getItemPosition(int position){ return data.get(position); }

    @Override
    public long getItemId(int position){return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate( R.layout.row_listview, null );
        }
        final TitleChatsItems item = data.get( position );

        TextView textname = (TextView) view.findViewById( R.id.row_name );
        TextView textipadr = (TextView) view.findViewById( R.id.row_ipadress );
        TextView textport = (TextView) view.findViewById( R.id.row_port );

        textname.setText( item.getName() );
        textipadr.setText( item.getIp_adr() );
        textport.setText( item.getPort() );
        return view;
    }


}


