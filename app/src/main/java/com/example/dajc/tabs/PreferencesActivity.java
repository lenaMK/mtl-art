package com.example.dajc.tabs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by LenaMK on 07/03/2016.
 */


public class PreferencesActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    String firstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


       Toast.makeText(this, "Paramètres", Toast.LENGTH_SHORT).show();




    }


}
