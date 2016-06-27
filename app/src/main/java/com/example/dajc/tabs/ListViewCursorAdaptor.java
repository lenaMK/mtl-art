package com.example.dajc.tabs;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by DAJC on 2016-04-19.
 */
public class ListViewCursorAdaptor extends SimpleCursorAdapter {

    //based on the code from this site
    //http://stackoverflow.com/questions/4803756/android-cursoradapter-listview-and-checkbox

    private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();
    private Cursor c;
    private Context context;
    public DBHelper dbh;
    ImageButton state_view;

    public ListViewCursorAdaptor(Context context, int layout, Cursor c, String[] from, int[] to, int flags, DBHelper dbh) {
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
            inView = inflater.inflate(R.layout.rangee_dist, null);
        }

        TextView tv_title = (TextView)inView.findViewById(R.id.rangee_title);
        TextView tv_sub = (TextView)inView.findViewById(R.id.rangee_sub);
        state_view = (ImageButton)inView.findViewById(R.id.state_button);

        c.moveToPosition(pos);
        String titre = c.getString(c.getColumnIndex(dbh.O_TITRE));
        String num_quartier = c.getString(c.getColumnIndex(dbh.O_QUARTIER));
        String quartier = dbh.retourneNom(dbh.TABLE_QUARTIERS, dbh.Q_ID, num_quartier, dbh.Q_NOM);


        //montrer la date de la photo, plutôt pour la galerie qu'ici
        //final String date = c.getString(c.getColumnIndex(dbh.O_DATE_IMAGE));

        tv_title.setText(titre);
        tv_sub.setText(quartier);        //set distance instead if known ("distance:"+dist+" km")



        //set icon in function of state
        final String etat = c.getString(c.getColumnIndex(dbh.O_ETAT));
        final String id = c.getString(c.getColumnIndex(dbh.O_ID));

        if (etat.equals(dbh.ETAT_NORMAL)) {
            state_view.setBackgroundResource(R.mipmap.ic_favorite_passive);
        } else if (etat.equals(dbh.ETAT_FAVORIS)){
            state_view.setBackgroundResource(R.mipmap.ic_favorite_active);
        }
        else {
            state_view.setBackgroundResource(R.mipmap.ic_gallery_active);
        }


        state_view.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                ImageButton ib = (ImageButton) v.findViewById(R.id.state_button);

                if (etat.equals(dbh.ETAT_NORMAL)){
                    dbh.changeEtat(id, dbh.ETAT_FAVORIS);
                    ib.setBackgroundResource(R.mipmap.ic_favorite_active);
                    Toast.makeText(context, "Oeuvre ajoutée aux favoris", Toast.LENGTH_SHORT).show();
                }
                else if (etat.equals(dbh.ETAT_FAVORIS)){
                    dbh.changeEtat(id, dbh.ETAT_NORMAL);
                    ib.setBackgroundResource(R.mipmap.ic_favorite_passive);
                    Toast.makeText(context, "Oeuvre retirée des favoris", Toast.LENGTH_SHORT).show();
                }
                else if (etat.equals(dbh.ETAT_GALERIE)){
                    String date_photo = dbh.retourneDatephoto(id);
                    Toast.makeText(context, "Oeuvre photographiée le "+ date_photo, Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d("listViewCursorAdaptor", "on ne peut pas changer l'état: " + etat);

                }



                }

        });


        return inView;


    }


}
