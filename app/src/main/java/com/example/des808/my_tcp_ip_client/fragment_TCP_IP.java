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
import android.widget.Button;
import android.widget.Switch;

import org.greenrobot.eventbus.EventBus;

public class fragment_TCP_IP extends Fragment {
   // private OnFragmentItemClickListenerSwitch mClickCallbackSwitch;
    private OnFragmentInteractionListener mListener;
    private Button button;
    private View v;
    private static final String LOG_TAG = "LOG_TAG" ;
    public Switch sw;

    public fragment_TCP_IP() {
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    // TODO: Rename and change types and number of parameters
    public static fragment_TCP_IP newInstance(String param1, String param2, String param3) {
        fragment_TCP_IP fragment = new fragment_TCP_IP();
        Bundle args = new Bundle();
        fragment.setArguments( args );
        return fragment;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException( context.toString() + " must implement OnFragmentInteractionListener" );
        }
        //mClickCallbackSwitch = (OnFragmentItemClickListenerSwitch) context;
        //Log.d(LOG_TAG, "onAttach");
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(LOG_TAG, "onCreate");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        v = inflater.inflate( R.layout.fragment_tcp_ip, container, false );
        /*sw = (Switch) v.findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mClickCallbackSwitch.onClickSelectedSwitch( true );
                } else {
                    mClickCallbackSwitch.onClickSelectedSwitch( false );
                }
            }
        });*/

        return v;}

   /* public interface OnFragmentItemClickListenerSwitch {
        public void onClickSelectedSwitch(boolean x);
    }*/

    @Override
    public void onStart() {
        super.onStart();
        EventBusTransmitterInt(2);
        //Log.d(LOG_TAG, "onStart");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        //Log.d(LOG_TAG, "onDeath");
        EventBusTransmitterInt(3);
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
