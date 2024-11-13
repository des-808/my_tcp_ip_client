package com.example.des808.my_tcp_ip_client.fragments;

//import android.app.Fragment;

//import android.app.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.des808.my_tcp_ip_client.adapter.ChatsTitleAdapter;
import com.example.des808.my_tcp_ip_client.R;
import com.example.des808.my_tcp_ip_client.TitleChatsItems;
import com.example.des808.my_tcp_ip_client.interfaces.onListViewFragmentTitle;

import java.util.ArrayList;

public class fragment_titles extends Fragment
{
    private OnFragmentItemClickListener mClickCallback;
    private OnFragmentInteractionListener mListener;
    public onListViewFragmentTitle ONListViewFragmentTitle;
    public View v;
    public Toast toast;
    public CharSequence message;
    public ListView newlist;
    private ArrayList<TitleChatsItems> list;
    private ChatsTitleAdapter C_Adapter;
    private ListView listView;
    private AdapterView<?> arg0;
    private View arg1;
    private int posit;
    private long arg3;
    public boolean mMuteSelection;
    private static final String LOG_TAG = "LOG_TAG" ;
    private Bundle savedInstanceState;

    public fragment_titles() {
        // Required empty public constructor
    }
    //public interface OnStartFragmentTitle { }

    // TODO: Rename and change types and number of parameters
    //@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static fragment_titles newInstance(int number) {
        Bundle args = new Bundle();
        fragment_titles fragment = new fragment_titles();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        ONListViewFragmentTitle.on_ListViewFragmentTitleInit() ; //шлем в активити привет. что фрагмент запустился. пускай шлёт пожелания
        }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate( savedInstanceState );

        /*OnBackPressedCallback callback = new OnBackPressedCallback(true *//* enabled by default *//*) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);*/

        if (getArguments() != null) {}

    }
    //######################################################################################################
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
    public void onAttach(@Nullable Context context) {
        super.onAttach( context );
        if (context instanceof onListViewFragmentTitle)
        {
            try {
                ONListViewFragmentTitle = (onListViewFragmentTitle) context;
            }catch (ClassCastException e)
            {
                throw new ClassCastException(context.toString() + " must implement ONListViewFragmentTitle");
            }
        }
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





}