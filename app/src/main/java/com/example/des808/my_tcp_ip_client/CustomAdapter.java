package com.example.des808.my_tcp_ip_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private static ArrayList<adapter_listview>data;

    public CustomAdapter(Context context, ArrayList<adapter_listview>data){
        inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        this.data = data;
    }

    //public CustomAdapter(fragment_titles fragment_titles, ArrayList<adapter_listview> list) { }

    @Override
    public int getCount(){ return data.size(); }

    @Override
    public adapter_listview getItem(int position){ return data.get(position); }

    //@Override
    public static adapter_listview getItemPosition(int position){ return data.get(position); }

    @Override
    public long getItemId(int position){return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate( R.layout.row_listview, null );
        }
        final adapter_listview item = data.get( position );

        TextView textname = (TextView) view.findViewById( R.id.row_name );
        TextView textipadr = (TextView) view.findViewById( R.id.row_ipadress );
        TextView textport = (TextView) view.findViewById( R.id.row_port );

        textname.setText( item.getTextname() );
        textipadr.setText( item.getTextipadr() );
        textport.setText( item.getTextport() );
        return view;
    }

   /* public void EventBusTransmitterInt(int idf){
        message_event event = new message_event();
        event.setMessage( idf );
        EventBus.getDefault().post( event );

    }*/
}


