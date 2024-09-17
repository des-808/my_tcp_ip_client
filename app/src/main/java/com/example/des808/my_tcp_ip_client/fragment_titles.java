package com.example.des808.my_tcp_ip_client;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class fragment_titles extends Fragment

{
    private OnFragmentItemClickListener mClickCallback;
    private OnFragmentInteractionListener mListener;
    public View v;
    public Toast toast;
    public CharSequence message;
    public ListView newlist;
    private ArrayList<adapter_listview> list;
    private CustomAdapter C_Adapter;
    private ListView listView;
    private AdapterView<?> arg0;
    private View arg1;
    private int posit;
    private long arg3;
    public boolean mMuteSelection;
    private static final String LOG_TAG = "LOG_TAG" ;


    public fragment_titles() {

    }

    // TODO: Rename and change types and number of parameters
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static fragment_titles newInstance(int number) {
        Bundle args = new Bundle();
        fragment_titles fragment = new fragment_titles();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBusTransmitterInt(1);}//шлем в активити привет. что фрагмент запустился. пускай шлёт пожелания

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {}
    }
    //######################################################################################################
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate( R.layout.fragment_title, container, false );
        newlist = (ListView) v.findViewById(R.id.list);
        newlist.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                mClickCallback.onClickSelected( position );
            }
        } );
        return v;
    }
    //######################################################################################################
    public interface OnFragmentItemClickListener {
        public void onClickSelected(int position);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        try {
            mClickCallback = (OnFragmentItemClickListener) context;
        } catch (ClassCastException e) { throw new ClassCastException(context.toString() + " must implement OnFragmentItemClickListener");}
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else { throw new RuntimeException( context.toString() + " must implement OnFragmentInteractionListener" );}

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

   public void EventBusTransmitterInt(int idf){
       message_event event = new message_event();
       event.setMessage( idf );
       EventBus.getDefault().post( event );

   }

}