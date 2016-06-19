package com.example.dajc.tabs.WebAPI;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.dajc.tabs.FirstActivity;

import java.io.IOException;

/**
 * Created by LenaMK on 24/04/2016.
 */
public class RunAPI  extends AsyncTask<String, Object, Oeuvre[]> {
    Oeuvre[] root;
    private Context context;
    Activity activity;
    int counter;

    public RunAPI (Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        this.counter = 1;
    }

    @Override
    protected Oeuvre[] doInBackground(String... params) {
        WebAPI web = new WebAPI(FirstActivity.getDBH());
        try {
            root = web.run();
        } catch (IOException e) {
            //erreur
        };

        return root;
    }

    @Override
    protected void onPreExecute() {
        if (counter > 1){
            // what to do...?
        } else {
            Toast.makeText(context, "Mise à jour des données", Toast.LENGTH_SHORT).show();
        }

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Oeuvre[] root) {
        super.onPostExecute(root);
        if (counter > 1){
            // what to do...?
        } else {
            Toast.makeText(context, "Mise à jour terminée", Toast.LENGTH_SHORT).show();
        }
        counter++;
        //adapter = new MyAdaptor();
        //list.setAdapter(adapter);
        //list.deferNotifyDataSetChanged();

    }
}
