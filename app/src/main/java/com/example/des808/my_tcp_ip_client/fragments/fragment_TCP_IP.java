package com.example.des808.my_tcp_ip_client.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    View view;
    private static final String PREFS_FILE = "com.example.des808.my_tcp_ip_client_preferences";
    private static final String KEY_ANDROMEDA = "switch_andromeda";
    private static final int PREFS_MODE = Context.MODE_PRIVATE;//Context.MODE_PRIVATE;
    private static final String FRAGMENT_NAME = "fragment_TCP_IP ";
    SharedPreferences sharedPreferencesMain;
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
        view = inflater.inflate( R.layout.fragment_tcp_ip, container, false );
        sharedPreferencesMain = getActivity() != null ? getActivity().getSharedPreferences(PREFS_FILE, PREFS_MODE) : null;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFragmentTCPIPInit.on_FragmentTCP_IP_Init();
        chatMessageOrChatAndromedaLayout();
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

    public void chatMessageOrChatAndromedaLayout(){
        View newView;
        ViewGroup fragmentLayout = (ViewGroup) getView().findViewById(R.id.fragmentLayout);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        fragmentLayout.removeAllViews();
        // Проверяем, существует ли ключ KEY_ANDROMEDA в файле PREFS_FILE
        if (sharedPreferencesMain.contains(KEY_ANDROMEDA)) {
            // Если ключ существует
            Log.d(LOG_TAG, FRAGMENT_NAME + KEY_ANDROMEDA + " -> " + sharedPreferencesMain.getBoolean(KEY_ANDROMEDA,false));
            if(sharedPreferencesMain.getBoolean(KEY_ANDROMEDA,false) ){
                newView = getLayoutInflater().inflate(R.layout.row_andromeda_send, null);
            }else {//false режим текста
                newView = getLayoutInflater().inflate(R.layout.row_chat_send, null);
            }
        } else {
            // Если ключ не существует, выводим сообщение об ошибке
            Log.e(LOG_TAG, "Ключ " + KEY_ANDROMEDA + " не найден в файле " + PREFS_FILE);
            return;
        }
        newView.setLayoutParams(params);
        fragmentLayout.addView(newView);
    }













}
