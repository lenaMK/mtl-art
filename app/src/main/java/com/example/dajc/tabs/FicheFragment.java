package com.example.dajc.tabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DAJC on 2016-04-17.
 */
public class FicheFragment extends Fragment implements View.OnClickListener {

    static final int REQUEST_IMAGE_PICTURE = 1;
    String numOeuvre;
    String etat_o;
    DBHelper dbh;

    TextView title;
    TextView author;
    TextView date;
    TextView infos;
    TextView date_ajout;
    ImageView photo;
    ImageButton fav_b;
    ImageButton map_b;
    ImageButton cam_b;
    String lastUpdate;
    String today;
    EditText userC;
    RatingBar ratingBar;

    Cursor c;

    String titre_o;
    String tech_nbr;
    String cat_nbr;
    String quart_nbr ;
    String mat_nbr ;
    String dimension_o ;
    String uri_photo ;
    String date_oeuvre;
    String o_artistes;
    String dimension;
    String tech_oeuvre ;
    String cat_oeuvre ;
    String quart_oeuvre;
    String mat_oeuvre;
    String user_comment;
    int user_rating;

    String idDuJour;

    SharedPreferences changes;



    public FicheFragment (){
        this.dbh = FirstActivity.getDBH();
        today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Log.d("settings", " today = "+today);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fiche, container, false);

        title = (TextView) v.findViewById(R.id.titre);
        author = (TextView) v.findViewById(R.id.artiste);
        date = (TextView) v.findViewById(R.id.date);
        infos = (TextView) v.findViewById(R.id.info);
        photo = (ImageView) v.findViewById(R.id.photo);
        fav_b = (ImageButton) v.findViewById(R.id.button_fav);
        map_b = (ImageButton) v.findViewById(R.id.button_map);
        cam_b = (ImageButton) v.findViewById(R.id.button_cam);
        date_ajout = (TextView) v.findViewById(R.id.tv_date);
        userC = (EditText) v.findViewById(R.id.user_comment);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);

        fav_b.setOnClickListener(this);
        cam_b.setOnClickListener(this);
        map_b.setOnClickListener(this);
        ratingBar.setOnClickListener(this);

        changes = getActivity().getSharedPreferences("change_rating", Context.MODE_PRIVATE);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                SharedPreferences.Editor editor = changes.edit() ;
                editor.putBoolean("rating", true);
                editor.commit();
            }
        });


        userC.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = changes.edit() ;
                editor.putBoolean("comment", true);
                editor.commit();
            }

        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        //randomizes between works that are neither favorites nor in gallery, 1/day
        SharedPreferences settings = getActivity().getSharedPreferences("firstRun", Context.MODE_PRIVATE);
        if (settings.getBoolean("isFirstRun", true)) {
            //on first use, get a first prey of the day

            c = dbh.listeTableOrd(dbh.TABLE_OEUVRES, dbh.O_ETAT, dbh.ETAT_NORMAL, "random()");
            c.moveToFirst();
            idDuJour = c.getString(c.getColumnIndex(DBHelper.O_ID));
            c.close();

            //create a first lastUpdate
            lastUpdate = new SimpleDateFormat("yyyyMMdd").format(new Date());

            //update the shared prefs

            SharedPreferences.Editor editor = settings.edit() ;
            editor.putBoolean("isFirstRun", false);
            editor.putString("lastUpdate", lastUpdate);
            editor.putString("idDuJour", idDuJour);
            editor.commit();

        }

        //lastUpdate should be set on the first run and always be active after
        String lastU = settings.getString("lastUpdate", "notSet");

        if (lastU.equals("notSet")){
            //no last update
            Log.d("settings", "Houston we have a problem");
        }
        else if (lastU.equals(today)){
            //we have a prey of the day, let's load it
            String todayId = settings.getString("idDuJour", "notSet");
            if (todayId.equals("notSet")){
                Log.d("settings", "Houston we have another problem");
            } else {
                loadFiche(todayId);
            }
        } else {
            //we need a new prey, let's get it
            String newId;
            do{
                c = dbh.listeTableOrd(dbh.TABLE_OEUVRES, dbh.O_ETAT, dbh.ETAT_NORMAL, "random()");
                c.moveToFirst();
                newId = c.getString(c.getColumnIndex(DBHelper.O_ID));
                c.close();
            } while (newId == settings.getString("idDuJour", "notSet"));
            //just to be sure it's not the same as yesterday


            //update the Preferences
            SharedPreferences.Editor editor = settings.edit() ;
            String newLastU = new SimpleDateFormat("yyyyMMdd").format(new Date());
            editor.putString("lastUpdate", newLastU);
            editor.putString("idDuJour", newId);
            editor.commit();

            //load it
            loadFiche(newId);

        }


        //set content
        title.setText(titre_o);
        author.setText(o_artistes);
        date.setText(date_oeuvre);

        String information = "Quartier: " + quart_oeuvre + "\n" + "Dimensions: " + dimension + "\n"
                + "Catégorie: " + cat_oeuvre + "\n" + "Technique: " + tech_oeuvre + "\n"
                + "Matériau: " + mat_oeuvre;
        infos.setText(information);

        //image de l'oeuvre ou par défaut
        if (uri_photo.equals(dbh.URI_DEF)) {
            Picasso.with(getContext()).load(uri_photo).resize(500, 888).into(photo);
            photo.setVisibility(View.VISIBLE);
        } else {
            Bitmap bmImg = BitmapFactory.decodeFile(uri_photo);
            photo.setImageBitmap(bmImg);
        }


        //si l'oeuvre est dans les favoris, le bouton n'est "pas actif" donc blanc
        if (etat_o.equals(dbh.ETAT_FAVORIS)) {
            fav_b.setBackgroundResource(R.mipmap.ic_favorite_active);
            date_ajout.setVisibility(date_ajout.GONE);
        }
        //si l'oeuvre est dans la galerie, on ne peut pas prendre de photo ou l'ajouter aux favoris
        //par contre, on affiche la date à laquelle la photo a été prise
        else if (etat_o.equals(dbh.ETAT_GALERIE)) {

            fav_b.setBackgroundResource(R.mipmap.ic_favorite_passive);
            fav_b.setVisibility(fav_b.GONE);
            cam_b.setVisibility(cam_b.GONE);


            //date de la photo de l'utilisateur
            String date_photo = dbh.retourneDatephoto(numOeuvre);
            date_ajout.setText("photo du " + date_photo);

            //clickListener pour agrandir la photo
            photo.setOnClickListener(this);

            if (! user_comment.equals("")){
                userC.setText(user_comment);
            }
            if (user_rating != 0) {
                ratingBar.setRating((float) user_rating);
            }

        } else {
            fav_b.setBackgroundResource(R.mipmap.ic_favorite_passive);
            date_ajout.setVisibility(date_ajout.GONE);
            userC.setVisibility(userC.GONE);
            ratingBar.setVisibility(ratingBar.GONE);
        }
    }

    public void loadFiche(String id) {
        //get content for the Fiche

        Cursor c = dbh.retourneOeuvre(id);
        c.moveToFirst();
        //récupère les données dans c;
        titre_o = c.getString(c.getColumnIndex(DBHelper.O_TITRE));
        numOeuvre = c.getString(c.getColumnIndex(DBHelper.O_ID));
        tech_nbr = c.getString(c.getColumnIndex(DBHelper.O_TECHNIQUE));
        cat_nbr = c.getString(c.getColumnIndex(DBHelper.O_CATEGORIE));
        quart_nbr = c.getString(c.getColumnIndex(DBHelper.O_QUARTIER));
        mat_nbr = c.getString(c.getColumnIndex(DBHelper.O_MATERIAU));
        dimension_o = c.getString(c.getColumnIndex(DBHelper.O_DIMENSION));
        uri_photo = c.getString(c.getColumnIndex(DBHelper.O_URI_IMAGE));
        date_oeuvre = c.getString(c.getColumnIndex(DBHelper.O_DATE_PROD));
        etat_o = c.getString(c.getColumnIndex(DBHelper.O_ETAT));
        user_comment = c.getString (c.getColumnIndex(DBHelper.O_COMMENT));
        user_rating = c.getInt(c.getColumnIndex(DBHelper.O_RATING));

        o_artistes = dbh.retourneNomsArtistes(numOeuvre);
        dimension = dimension_o;

        tech_oeuvre = dbh.retourneNom(DBHelper.TABLE_TECHNIQUES, DBHelper.T_ID, tech_nbr, DBHelper.T_NOM);
        cat_oeuvre = dbh.retourneNom(DBHelper.TABLE_CATEGORIES, DBHelper.C_ID, cat_nbr, DBHelper.C_NOM);
        quart_oeuvre = dbh.retourneNom(DBHelper.TABLE_QUARTIERS, DBHelper.Q_ID, quart_nbr, DBHelper.Q_NOM);
        mat_oeuvre = dbh.retourneNom(DBHelper.TABLE_MATERIAUX, DBHelper.M_ID, mat_nbr, DBHelper.M_NOM);

        c.close();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == REQUEST_IMAGE_PICTURE && resultCode !=0){

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");

            if(imageBitmap != null) {
                photo.setImageBitmap(imageBitmap);
            }


            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            try {
                File f=File.createTempFile(
                        "MTL_ART_" + numOeuvre + "_", //prefix
                        ".jpg",        //suffix
                        storageDir      //directory
                );
                String currentPath = f.getAbsolutePath();
                Log.d("photo", "image : " + currentPath);
                FileOutputStream fout= new FileOutputStream(currentPath);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                //Log.d("photo", "dateTime stamp is "+timeStamp);

                dbh.ajoutePhoto(numOeuvre, currentPath, timeStamp);
                dbh.changeEtat(numOeuvre, dbh.ETAT_GALERIE);

                Intent refresh = new Intent(getContext(), MainActivity.class);
                startActivity(refresh);//Start the same Activity
                getActivity().finish();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.button_fav:

                if (etat_o.equals(dbh.ETAT_NORMAL)){

                    //méthode qui change l'état dans la base de données
                    dbh.changeEtat(numOeuvre, dbh.ETAT_FAVORIS);

                    Toast.makeText(getActivity(), "Oeuvre ajoutée aux favoris", Toast.LENGTH_SHORT).show();
                }
                else if (etat_o.equals(dbh.ETAT_FAVORIS)){

                    dbh.changeEtat(numOeuvre, dbh.ETAT_NORMAL);

                    Toast.makeText(getActivity(), "Oeuvre retirée des favoris", Toast.LENGTH_SHORT).show();

                }
                else{
                    Log.d("listViewCursorAdaptor", "on ne peut pas changer l'état: " + etat_o);

                }

                Intent refresh = new Intent(getContext(), MainActivity.class);
                startActivity(refresh);//Start the same Activity
                getActivity().finish();

                break;
            case R.id.button_map:

                intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("numOeuvre", numOeuvre);
                startActivity(intent);
                break;

            case R.id.button_cam:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, REQUEST_IMAGE_PICTURE);
                break;

            case R.id.photo:
                intent = new Intent (getActivity(), PhotoActivity.class);
                intent.putExtra("numOeuvre", numOeuvre);
                startActivity(intent);
                break;

            case R.id.ratingBar:
                Toast.makeText(getActivity(),
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Boolean changesComment;
        Boolean changesRating;

        changesComment = changes.getBoolean("comment", false);
        changesRating = changes.getBoolean("rating", false);

        if (changesComment== true) {
            String comment = String.valueOf(userC.getText());

            //add modified value to DB
            dbh.ajouteComment(numOeuvre, comment);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("comment", false);
            editor.commit();
        }

        if(changesRating== true){
            int rating = (int) ratingBar.getRating();
            //add modified value to DB
            dbh.ajouteRating(numOeuvre, rating);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("rating", false);
            editor.commit();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        Boolean changesComment;
        Boolean changesRating;

        changesComment = changes.getBoolean("comment", false);
        changesRating = changes.getBoolean("rating", false);

        if (changesComment == true) {
            String comment = String.valueOf(userC.getText());

            //add modified value to DB
            dbh.ajouteComment(numOeuvre, comment);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("comment", false);
            editor.commit();
        }

        if(changesRating == true){
            int rating = (int) ratingBar.getRating();
            //add modified value to DB
            dbh.ajouteRating(numOeuvre, rating);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("rating", false);
            editor.commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Boolean changesComment;
        Boolean changesRating;

        changesComment = changes.getBoolean("comment", false);
        changesRating = changes.getBoolean("rating", false);

        if (changesComment == true) {
            String comment = String.valueOf(userC.getText());

            //add modified value to DB
            dbh.ajouteComment(numOeuvre, comment);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("comment", false);
            editor.commit();
        }

        if(changesRating== true){
            int rating = (int) ratingBar.getRating();
            //add modified value to DB
            dbh.ajouteRating(numOeuvre, rating);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("rating", false);
            editor.commit();
        }
    }
}
