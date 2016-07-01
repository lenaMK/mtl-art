package com.example.dajc.tabs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

    TextView p_title;
    TextView p_author;
    TextView p_date_ajout;
    EditText p_user_c;
    ImageView photo;

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

        Intent intent = getIntent();
        numOeuvre = intent.getStringExtra("numOeuvre");

        Cursor c = dbh.retourneOeuvre(numOeuvre);
        c.moveToFirst();

        //récupère les données dans c;
        title = c.getString(c.getColumnIndex(DBHelper.O_TITRE));
        uri_photo = c.getString(c.getColumnIndex(DBHelper.O_URI_IMAGE));
        date_ajout= c.getString(c.getColumnIndex(DBHelper.O_DATE_IMAGE));

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



    }
}