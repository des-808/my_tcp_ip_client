package com.example.des808.my_tcp_ip_client;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


@SuppressLint("ValidFragment")
public class fragment_exit extends DialogFragment{

    @SuppressLint("ValidFragment")
    public fragment_exit(String title) {
        Bundle args = new Bundle(  );
        args.putString( "title", title );
        setArguments( args );
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString( "title" );
        return new AlertDialog.Builder( getActivity() )
                //.setIcon( R.drawable.android3d)
                .setTitle( title )
                .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((my_tcp_ip_client) getActivity()).doPositiveClick();
                    }
                } )
                .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((my_tcp_ip_client) getActivity()).doNegativeClick();
                    }
                } ).create();
    }
    }





