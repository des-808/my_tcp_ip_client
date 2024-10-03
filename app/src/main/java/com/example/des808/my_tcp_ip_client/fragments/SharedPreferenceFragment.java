package com.example.des808.my_tcp_ip_client.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import com.example.des808.my_tcp_ip_client.Preferences_Class;
import com.example.des808.my_tcp_ip_client.R;
import com.example.des808.my_tcp_ip_client.interfaces.OnSettingsFragment;

public class SharedPreferenceFragment extends PreferenceFragmentCompat {
    private OnSettingsFragment mFragmentSettings;
    SharedPreferences preferences;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.root_preferences);
        preferences = getPreferenceManager().getSharedPreferences();

        //boolean andromeda = preferences.getBoolean("switch_andromeda",true );
        //String vedromeda = andromeda? "True" : "False";
        //Snackbar.make(getView(), vedromeda , Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsFragment){
            mFragmentSettings = (OnSettingsFragment) context;
        }
         else {
            throw new RuntimeException( context + " must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Preferences_Class p_class = new Preferences_Class();
        preferences = getPreferenceManager().getSharedPreferences();
        boolean andromeda = preferences.getBoolean("switch_andromeda", false);
        p_class.setAndromeda(andromeda);
        mFragmentSettings.on_SettingsFragment(p_class );
    }
}