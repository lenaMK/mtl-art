package com.example.dajc.tabs;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by DAJC on 2016-04-21.
 */
public class WishListFragment extends Fragment implements AdapterView.OnItemClickListener{
    DBHelper dbh;
    ListView lv;
    Cursor c;
    public static SimpleCursorAdapter sca;


    public WishListFragment(){
        //this.root = root;
        this.dbh = FirstActivity.getDBH();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listview, container, false);

        lv = (ListView)v.findViewById(R.id.listView);

        //Cursor here should just get the ones with etat equal to "wishlist"
        c = dbh.listeTableOrd(dbh.TABLE_OEUVRES, dbh.O_ETAT, dbh.ETAT_FAVORIS, dbh.O_TITRE);
        String[] from ={ DBHelper.O_ID, DBHelper.O_TITRE, DBHelper.O_QUARTIER };
        int[] to = {0, android.R.id.text1, android.R.id.text2};
        sca = new WishListAdaptor(getContext(), android.R.layout.simple_list_item_2, c, from, to, 0,dbh);


        lv.setAdapter(sca);

        lv.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        c.moveToPosition(position);
        String numOeuvre = c.getString(c.getColumnIndex(dbh.O_ID));
        Intent intent= new Intent(getActivity(), FicheActivity.class);
        intent.putExtra("numOeuvre", numOeuvre);
        startActivity(intent);
    }
}