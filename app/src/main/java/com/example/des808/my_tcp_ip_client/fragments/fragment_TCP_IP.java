package com.example.des808.my_tcp_ip_client.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.des808.my_tcp_ip_client.R;
import com.example.des808.my_tcp_ip_client.interfaces.onFragment_TCP_IP_Init;

public class fragment_TCP_IP extends Fragment {
    private OnFragmentInteractionListener mListener;
    private onFragment_TCP_IP_Init mFragmentTCPIPInit;
    //private Button button;
    private static final String LOG_TAG = "LOG_TAG" ;


    public fragment_TCP_IP() {
        // Required empty public constructor
    }

    //@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    // TODO: Rename and change types and number of parameters
    public static fragment_TCP_IP newInstance(String param1, String param2, String param3) {
        fragment_TCP_IP fragment = new fragment_TCP_IP();
        Bundle args = new Bundle();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onFragment_TCP_IP_Init){
            mFragmentTCPIPInit = (onFragment_TCP_IP_Init) context;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException( context + " must implement OnFragmentInteractionListener" );
        }
        //Log.d(LOG_TAG, "onAttach FragmentTCP_IP");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(LOG_TAG, "onCreate FragmentTCP_IP");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View v = inflater.inflate( R.layout.fragment_tcp_ip, container, false );

        return v;}

    @Override
    public void onStart() {
        super.onStart();
        mFragmentTCPIPInit.on_FragmentTCP_IP_Init();
        //Log.d(LOG_TAG, "onStart FragmentTCP_IP");
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
        //Log.d(LOG_TAG, "onDeath FragmentTCP_IP");
        mFragmentTCPIPInit.on_FragmentTCP_IP_Disconnect();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
