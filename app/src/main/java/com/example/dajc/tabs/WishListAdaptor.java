package com.example.dajc.tabs;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by DAJC on 2016-04-21.
 */
public class WishListAdaptor extends SimpleCursorAdapter {
    private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();
    private Cursor c;
    private Context context;
    public DBHelper dbh;
    String id;

    public WishListAdaptor(Context context, int layout, Cursor c, String[] from, int[] to, int flags, DBHelper dbh) {
        super(context, layout, c, from, to, flags);
        this.c = c;
        this.context = context;
        this.dbh = dbh;

        for (int i = 0; i < this.getCount(); i++) {
            itemChecked.add(i, false); // initializes all items value with false
        }

    }


    public View getView(final int pos, View inView, ViewGroup parent) {
        if (inView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inView = inflater.inflate(R.layout.rangee, null);
        }

        TextView tv_title = (TextView)inView.findViewById(R.id.rangee_title);
        TextView tv_sub = (TextView)inView.findViewById(R.id.rangee_sub);
        final ImageButton im = (ImageButton)inView.findViewById(R.id.state_button);

        c.moveToPosition(pos);
        String titre = c.getString(c.getColumnIndex(dbh.O_TITRE));
        String num_quartier = c.getString(c.getColumnIndex(dbh.O_QUARTIER));
        String quartier = dbh.retourneNom(dbh.TABLE_QUARTIERS, dbh.Q_ID, num_quartier, dbh.Q_NOM);
        id = c.getString(c.getColumnIndex(dbh.O_ID));

        // tring quart_oeuvre = dbh.retourneNom(DBHelper.TABLE_QUARTIERS, DBHelper.Q_ID, quart_nbr, DBHelper.Q_NOM);

        tv_title.setText(titre);
        tv_sub.setText(quartier);

        im.setBackgroundResource(R.mipmap.ic_favorite_active);
        im.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dbh.changeEtat(id, dbh.ETAT_NORMAL);
                im.setBackgroundResource(R.mipmap.ic_favorite_passive);
                Toast.makeText(context, "Oeuvre retirée des favoris", Toast.LENGTH_SHORT).show();

                WishListFragment.sca.notifyDataSetChanged();
            }


        });
        return inView;
    }
}