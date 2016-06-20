package com.example.dajc.tabs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DAJC on 2016-04-20.
 */
public class FicheActivity extends Activity implements View.OnClickListener {
    static final int REQUEST_IMAGE_PICTURE = 1;
    DBHelper dbh;
    static int position;

    TextView title;
    TextView author;
    TextView date;
    TextView infos;
    TextView date_ajout;
    ImageView photo;
    ImageButton fav_b;
    ImageButton map_b;
    ImageButton cam_b;

    String numOeuvre;

    String etat_o;
    String id;


    public FicheActivity() {
        this.dbh = FirstActivity.getDBH();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fiche);

        title = (TextView) findViewById(R.id.titre);
        author = (TextView) findViewById(R.id.artiste);
        date = (TextView) findViewById(R.id.date);
        infos = (TextView) findViewById(R.id.info);
        photo = (ImageView) findViewById(R.id.photo);
        fav_b = (ImageButton) findViewById(R.id.button_fav);
        map_b = (ImageButton) findViewById(R.id.button_map);
        cam_b = (ImageButton) findViewById(R.id.button_cam);
        date_ajout = (TextView) findViewById(R.id.tv_date);

        Intent intent = getIntent();
        numOeuvre = intent.getStringExtra("numOeuvre");

        Cursor c = dbh.retourneOeuvre(numOeuvre);
        c.moveToFirst();

        //récupère les données dans c;
        String titre_o = c.getString(c.getColumnIndex(DBHelper.O_TITRE));
        String tech_nbr = c.getString(c.getColumnIndex(DBHelper.O_TECHNIQUE));
        String cat_nbr = c.getString(c.getColumnIndex(DBHelper.O_CATEGORIE));
        String quart_nbr = c.getString(c.getColumnIndex(DBHelper.O_QUARTIER));
        String mat_nbr = c.getString(c.getColumnIndex(DBHelper.O_MATERIAU));
        String dimension_o = c.getString(c.getColumnIndex(DBHelper.O_DIMENSION));
        String uri_photo = c.getString(c.getColumnIndex(DBHelper.O_URI_IMAGE));
        String date_oeuvre = c.getString(c.getColumnIndex(DBHelper.O_DATE_PROD));
        etat_o = c.getString(c.getColumnIndex(DBHelper.O_ETAT));
        c.close();
        //set title
        title.setText(titre_o);


        //set artist(s) name(s)
        String o_artistes = dbh.retourneNomsArtistes(numOeuvre);
        author.setText(o_artistes);

        //date de la photo
        date.setText(date_oeuvre);

        //dimensions de l'oeuvre
        String dimension = dimension_o;

        //technique
        String tech_oeuvre = dbh.retourneNom(DBHelper.TABLE_TECHNIQUES, DBHelper.T_ID, tech_nbr, DBHelper.T_NOM);
        String cat_oeuvre = dbh.retourneNom(DBHelper.TABLE_CATEGORIES, DBHelper.C_ID, cat_nbr, DBHelper.C_NOM);
        String quart_oeuvre = dbh.retourneNom(DBHelper.TABLE_QUARTIERS, DBHelper.Q_ID, quart_nbr, DBHelper.Q_NOM);
        String mat_oeuvre = dbh.retourneNom(DBHelper.TABLE_MATERIAUX, DBHelper.M_ID, mat_nbr, DBHelper.M_NOM);


        String information = "Quartier: " + quart_oeuvre + "\n" + "Dimensions: " + dimension + "\n"
                + "Catégorie: " + cat_oeuvre + "\n" + "Technique: " + tech_oeuvre + "\n"
                + "Matériau: " + mat_oeuvre;
        infos.setText(information);

        //image de l'oeuvre ou par défaut
        if (uri_photo.equals(dbh.URI_DEF)) {
            Picasso.with(this).load(uri_photo).resize(500, 888).into(photo);
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
            Log.d("fiche", "in etat_galerie and trying to hide visibility");
            fav_b.setBackgroundResource(R.mipmap.ic_favorite_passive);
            fav_b.setVisibility(fav_b.GONE);
            cam_b.setVisibility(cam_b.GONE);

            //date de la photo de l'utilisateur
            String date_photo = dbh.retourneDatephoto(numOeuvre);
            date_ajout.setText("photo du " + date_photo);
        } else {
            fav_b.setBackgroundResource(R.mipmap.ic_favorite_passive);
            date_ajout.setVisibility(date_ajout.GONE);
        }


        fav_b.setOnClickListener(this);
        cam_b.setOnClickListener(this);
        map_b.setOnClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICTURE && resultCode != 0) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            if (imageBitmap != null) {
                photo.setImageBitmap(imageBitmap);
            }


            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            try {
                File f = File.createTempFile(
                        "MTL_ART_" + numOeuvre + "_", //prefix
                        ".jpg",        //suffix
                        storageDir      //directory
                );
                String currentPath = f.getAbsolutePath();

                FileOutputStream fout = new FileOutputStream(currentPath);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                dbh.ajoutePhoto(numOeuvre, currentPath, timeStamp);
                dbh.changeEtat(numOeuvre, dbh.ETAT_GALERIE);

                GalleryFragment.sca.notifyDataSetChanged();

                Intent refresh = new Intent(this, FicheActivity.class);
                refresh.putExtra("numOeuvre", numOeuvre);
                startActivity(refresh);//Start the same Activity
                this.finish();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_fav:

                if (etat_o.equals(dbh.ETAT_NORMAL)) {

                    //méthode qui change l'état dans la base de données
                    dbh.changeEtat(numOeuvre, dbh.ETAT_FAVORIS);

                    Toast.makeText(this, "Oeuvre ajoutée aux favoris", Toast.LENGTH_SHORT).show();
                    WishListFragment.sca.notifyDataSetChanged();
                } else if (etat_o.equals(dbh.ETAT_FAVORIS)) {

                    dbh.changeEtat(numOeuvre, dbh.ETAT_NORMAL);

                    Toast.makeText(this, "Oeuvre retirée des favoris", Toast.LENGTH_SHORT).show();
                    WishListFragment.sca.notifyDataSetChanged();
                } else {
                    Log.d("listViewCursorAdaptor", "on ne peut pas changer l'état: " + etat_o);
                }

                Intent refresh = new Intent(this, FicheActivity.class);
                refresh.putExtra("numOeuvre", numOeuvre);
                startActivity(refresh);//Start the same Activity
                this.finish();

                break;
            case R.id.button_map:

                intent = new Intent(this, MapActivity.class);
                intent.putExtra("numOeuvre", numOeuvre);
                startActivity(intent);

                break;

            case R.id.button_cam:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, REQUEST_IMAGE_PICTURE);

        }
    }
}