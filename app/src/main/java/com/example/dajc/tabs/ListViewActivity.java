package com.example.dajc.tabs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by DAJC on 2016-04-21.
 */
public class ListViewActivity extends Activity implements AdapterView.OnItemClickListener {

    DBHelper dbh;

    String user_longi;
    String user_lati;
    RadioButton rb_dist;
    RadioButton rb_quart;
    Cursor c;

    public ListViewActivity(){
        this.dbh = FirstActivity.getDBH();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get data for user location
        Intent intent = getIntent();
        user_longi = intent.getStringExtra("Geo_longi");
        user_lati = intent.getStringExtra("Geo_lati");

        //for debug
        Log.d("ListView gps", "Longitude utilisateur = "+user_longi);
        Log.d("ListView gps", "Latitude utilisateur = " + user_lati);

        //set View
        setContentView(R.layout.listview_tri);
        ListView lv = (ListView)findViewById(R.id.listView);

        lv.setOnItemClickListener(this);

        rb_dist = (RadioButton) findViewById(R.id.rb_distance);
        rb_quart = (RadioButton) findViewById(R.id.rb_quartier);

        if (user_longi.contentEquals("")){
            Toast.makeText(getApplicationContext(), "Activer le GPS pour le tri par distance", Toast.LENGTH_LONG).show();
            rb_quart.setChecked(true);
        }





        /*
        switch (): which button is checked
            case dist:

                /* need a sorting algorithm
                    1)get all the artworks (sorted by latitude)
                    2)create variables
                    3)get data for each
                    4)sort (at insertion or after?)
                        Cursor c = dbh.listeTable(DBHelper.TABLE_OEUVRES, DBHelper.O_COORD_LAT);

                        arrayList<[]> works; //check declaration
                        String art_name;
                        String art_lati;
                        String art_longi;
                        String art_state;

                        String lati_dist;
                        string longi_dist;
                        String total_dist;

                        c.moveToFirst();
                        while (!c.isAfterLast()) {
                            art_name = c.getString(c.getColumnIndex(DBHelper.O_TITRE));
                            art_lati = c.getString(c.getColumnIndex(DBHelper.O_COORD_LAT);
                            art_longi = c.getString(c.getColumnIndex(DBHelper.O_COORD_LONG);
                            art_state = c.getString(c.getColumnIndex(DBHelper.O_ETAT);

                            lati_dist = (user_lati - art_lati).toNonNegative() // find function
                            longi_dist = (user_longi - art_longi)

                            total_dist = lati_dist + longi_dist;

                            ???? sorting and which type to use
                        }


                    |artWork[i].lati-myPos.lati| = distance[i].lati
                    |artWork[i]-longi - myPos-longi| = distance[i]. longi
                    totalDistance[i] = distance[i].lati + distance[i].longi

                    Sort the array of totalDistances in order to show them by distance to user
                    How to transfer this array into the sca?!
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

            //Is there another case? by name? or will that be replaced by search query?

        */





        //shows them all by area
        c = dbh.listeTable(DBHelper.TABLE_OEUVRES, DBHelper.O_QUARTIER);
        String[] from ={ DBHelper.O_ID, DBHelper.O_TITRE, DBHelper.O_DIMENSION };
        int[] to = {0, android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter sca = new ListViewCursorAdaptor(this, android.R.layout.simple_list_item_2, c, from, to, 0,dbh);


        lv.setAdapter(sca);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("onItemClick", "item was clicked "+position);
        c.moveToPosition(position);
        String numOeuvre = c.getString(c.getColumnIndex(dbh.O_ID));
        Intent intent= new Intent(this, FicheActivity.class);
        intent.putExtra("numOeuvre", numOeuvre);
        startActivity(intent);
    }
}
