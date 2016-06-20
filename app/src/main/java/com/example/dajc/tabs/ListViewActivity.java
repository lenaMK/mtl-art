package com.example.dajc.tabs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;

/**
 * Created by DAJC on 2016-04-21.
 */
public class ListViewActivity extends Activity {

    DBHelper dbh;

    String loc_longi;
    String loc_lati;
    RadioButton rb_dist;
    RadioButton rb_quart;

    public ListViewActivity(){
        this.dbh = FirstActivity.getDBH();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get data for user location
        Intent intent = getIntent();
        loc_longi = intent.getStringExtra("Geo_longi");
        loc_lati = intent.getStringExtra("Geo_lati");

        //for debug
        Log.d("ListView gps", "Longitude utilisateur = "+loc_longi);
        Log.d("ListView gps", "Latitude utilisateur = " + loc_lati);

        //set View
        setContentView(R.layout.listview);
        ListView lv = (ListView)findViewById(R.id.listView);

        rb_dist = (RadioButton) findViewById(R.id.rb_distance);
        rb_quart = (RadioButton) findViewById(R.id.rb_quartier);


        /*
               Need to set up the RadioButtons!
               By default, have distance set.
                    if the person doesn't have location active, notify and set by neighbourhood
        */



        /*
        switch (): which button is checked
            case dist:

                /* need a sorting algorithm

                    |artWork[i].lati-myPos.lati| = distance[i].lati
                    |artWork[i]-longi - myPos-longi| = distance[i]. longi
                    totalDistance[i] = distance[i].lati + distance[i].longi

                    Sort the array of totalDistances in order to show them by distance to user
                */
        /*

                break;
            case quart:
                    //shows them all by area
                    Cursor c = dbh.listeTable(DBHelper.TABLE_OEUVRES, DBHelper.O_QUARTIER);
                    String[] from ={ DBHelper.O_ID, DBHelper.O_TITRE, DBHelper.O_DIMENSION };
                    int[] to = {0, android.R.id.text1, android.R.id.text2};
                    SimpleCursorAdapter sca = new ListViewCursorAdaptor(this, android.R.layout.simple_list_item_2, c, from, to, 0,dbh);
                 break;

            //Is there another case? by name or will that be replaced by search query

        */





        //shows them all by area
        Cursor c = dbh.listeTable(DBHelper.TABLE_OEUVRES, DBHelper.O_QUARTIER);
        String[] from ={ DBHelper.O_ID, DBHelper.O_TITRE, DBHelper.O_DIMENSION };
        int[] to = {0, android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter sca = new ListViewCursorAdaptor(this, android.R.layout.simple_list_item_2, c, from, to, 0,dbh);


        lv.setAdapter(sca);
    }


}
