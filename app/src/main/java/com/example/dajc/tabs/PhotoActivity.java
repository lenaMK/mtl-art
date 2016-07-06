package com.example.dajc.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by LenaMK on 01/07/2016.
 */
public class PhotoActivity extends Activity{
    DBHelper dbh;

    String title;
    String author;
    String date_ajout;
    String uri_photo;
    String user_c;
    String numOeuvre;
    int user_r;

    TextView p_title;
    TextView p_author;
    TextView p_date_ajout;
    EditText p_user_c;
    ImageView photo;
    RatingBar p_rating;
    SharedPreferences changes;

    public PhotoActivity() {
        this.dbh = FirstActivity.getDBH();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.photo);

        p_title = (TextView) findViewById(R.id.photo_titre);
        p_author = (TextView) findViewById(R.id.photo_artiste);
        p_date_ajout = (TextView) findViewById(R.id.photo_dateAjout);
        p_user_c = (EditText) findViewById(R.id.photo_comment);
        photo = (ImageView) findViewById(R.id.photo_view);
        p_rating = (RatingBar) findViewById(R.id.ratingBar);

        Intent intent = getIntent();
        numOeuvre = intent.getStringExtra("numOeuvre");

        Cursor c = dbh.retourneOeuvre(numOeuvre);
        c.moveToFirst();

        //récupère les données dans c;
        title = c.getString(c.getColumnIndex(DBHelper.O_TITRE));
        uri_photo = c.getString(c.getColumnIndex(DBHelper.O_URI_IMAGE));
        date_ajout= c.getString(c.getColumnIndex(DBHelper.O_DATE_IMAGE));
        user_c = c.getString(c.getColumnIndex(DBHelper.O_COMMENT));
        user_r = c.getInt(c.getColumnIndex(DBHelper.O_RATING));
        c.close();


        p_title.setText(title);

        author = dbh.retourneNomsArtistes(numOeuvre);

        p_author.setText(author);

        //date de la photo
        p_date_ajout.setText(date_ajout);



        //image de l'oeuvre ou par défaut
        if (uri_photo.equals(dbh.URI_DEF)) {
            Picasso.with(this).load(uri_photo).resize(500, 888).into(photo);
            photo.setVisibility(View.VISIBLE);
        } else {
            Bitmap bmImg = BitmapFactory.decodeFile(uri_photo);
            photo.setImageBitmap(bmImg);
        }

        p_user_c.setText(user_c);
        p_rating.setRating((float) user_r);

        changes = getApplicationContext().getSharedPreferences("change_rating", Context.MODE_PRIVATE);

        p_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                SharedPreferences.Editor editor = changes.edit() ;
                editor.putBoolean("rating", true);
                editor.commit();
            }
        });

        p_user_c.addTextChangedListener(new TextWatcher() {

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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Boolean changesComment;
        Boolean changesRating;

        changesComment = changes.getBoolean("comment", false);
        changesRating = changes.getBoolean("rating", false);

        if (changesComment) {
            String comment = String.valueOf(p_user_c.getText());

            //add modified value to DB
            dbh.ajouteComment(numOeuvre, comment);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("comment", false);
            editor.commit();
        }

        if(changesRating){
            int rating = (int) p_rating.getRating();
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

        if (changesComment) {
            String comment = String.valueOf(p_user_c.getText());

            //add modified value to DB
            dbh.ajouteComment(numOeuvre, comment);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("comment", false);
            editor.commit();
        }

        if(changesRating){
            int rating = (int) p_rating.getRating();
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

        if (changesComment) {
            String comment = String.valueOf(p_user_c.getText());

            //add modified value to DB
            dbh.ajouteComment(numOeuvre, comment);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("comment", false);
            editor.commit();
        }

        if(changesRating){
            int rating = (int) p_rating.getRating();
            //add modified value to DB
            dbh.ajouteRating(numOeuvre, rating);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("rating", false);
            editor.commit();
        }

    }

}