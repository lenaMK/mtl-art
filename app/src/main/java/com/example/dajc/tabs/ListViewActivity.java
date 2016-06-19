package com.example.dajc.tabs;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by DAJC on 2016-04-21.
 */
public class ListViewActivity extends Activity {

    DBHelper dbh;
    public ListViewActivity(){
        this.dbh = FirstActivity.getDBH();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.listview);
        ListView lv = (ListView)findViewById(R.id.listView);



        //don't know how to order them by distance. Can't do it with SQL and have to think about
        // how to do it here. For now it shows them all by area

        Cursor c = dbh.listeTable(DBHelper.TABLE_OEUVRES, DBHelper.O_QUARTIER);
        String[] from ={ DBHelper.O_ID, DBHelper.O_TITRE, DBHelper.O_DIMENSION };
        int[] to = {0, android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter sca = new ListViewCursorAdaptor(this, android.R.layout.simple_list_item_2, c, from, to, 0,dbh);


        lv.setAdapter(sca);
    }


}
