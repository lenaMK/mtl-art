package com.example.dajc.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by DAJC on 2016-04-17.
 */
public class SimpleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.simple_fragment, container, false);
        TextView text = (TextView)v.findViewById(R.id.testfragment);
        Bundle args = getArguments();
        int fragmentid = args.getInt("id");
        text.setText("Fragment "+fragmentid);
        return v;
    }
}
