package com.example.dajc.tabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dajc.tabs.WebAPI.Artiste;
import com.example.dajc.tabs.WebAPI.Oeuvre;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LenaMK on 20/03/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "art_mtl.db";
    static final int DB_VERSION = 22;

    //structure des tables
    //nom des tables
    static final String TABLE_OEUVRES = "oeuvres";
    static final String TABLE_ARTISTES = "artistes";
    static final String TABLE_QUARTIERS = "quartiers";
    static final String TABLE_MATERIAUX = "materiaux";
    static final String TABLE_TECHNIQUES = "techniques";
    static final String TABLE_CATEGORIES = "categories";
    static final String TABLE_OEUVRE_ARTISTE ="oeuvre_artiste";


    //nom des colonnes
    //table oeuvres
    static final String O_ID = "_id";
    static final String O_TITRE = "titre";
    static final String O_DIMENSION = "dimension";
    static final String O_COORD_LAT = "latitude";
    static final String O_COORD_LONG = "longitude";
    static final String O_URI_IMAGE = "uri_image";
    static final String O_DATE_IMAGE = "date_image"; //format date?
    static final String O_ETAT = "etat"; //format pour options: basic, wishlist, gallerie
    static final String O_QUARTIER ="quartier";
    static final String O_MATERIAU ="materiau";
    static final String O_TECHNIQUE ="technique";
    static final String O_CATEGORIE ="categorie";
    static final String O_DATE_PROD = "date_production";
    static final String O_COMMENT = "commentaire";
    static final String O_RATING = "rating";

    //table artistes
    static final String A_ID ="_id";
    static final String A_PRENOM ="prenom_artiste";
    static final String A_NOM ="nom_artiste";
    static final String A_NOM_COLLECTIF ="nom_collectif_artiste";

    //table quartiers
    static final String Q_ID="_id";
    static final String Q_NOM ="nom_quartier";

    //table materiaux
    static final String M_ID="_id";
    static final String M_NOM ="nom_materiau";

    //table techniques
    static final String T_ID="_id";
    static final String T_NOM ="nom_technique";

    //table categories
    static final String C_ID="_id";
    static final String C_NOM ="nom_categorie";

    //table oeuvre-artiste
    static final String OA_ID_ARTISTE="id_artiste";
    static final String OA_ID_OEUVRE ="id_oeuvre";

    //valeurs pour l'état d'une oeuvre
    static final String ETAT_NORMAL = "normal";
    static final String ETAT_FAVORIS = "favoris";
    static final String ETAT_GALERIE = "galerie";

    //uri par défaut
    static final String URI_DEF="http://www-ens.iro.umontreal.ca/~krausele/emptyImage.png";

    //rating par défaut
    static final int RATING_DEF = 0;

    // ouvrir un seul db pour tous les fragments/activités...
    private static SQLiteDatabase db = null;


    //constructeur:
    //super exige un context vu que la classe n'est en lien
    // avec aucune activité
    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        if (db == null){
            db = getWritableDatabase();
        }

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //créer la table artistes
        String sqlArtistes = "create table "+TABLE_ARTISTES
                +" ("
                +A_ID+" integer not null, "
                +A_PRENOM+" text, "
                +A_NOM+" text, "
                +A_NOM_COLLECTIF+" text, "
                +"primary key ("+A_ID+") "
                +") ";

        Log.d("SQL", sqlArtistes);
        db.execSQL(sqlArtistes);


        //créer la table quartiers
        String sqlQuartiers= "create table "+TABLE_QUARTIERS
                +" ("
                +Q_ID+" integer primary key, "
                +Q_NOM+" text "
                +") ";

        Log.d("SQL", sqlQuartiers);
        db.execSQL(sqlQuartiers);


        //créer la table materiaux
        String sqlMateriaux= "create table "+TABLE_MATERIAUX
                +" ("
                +M_ID+" integer primary key, "
                +M_NOM+" text "
                +") ";

        Log.d("SQL", sqlMateriaux);
        db.execSQL(sqlMateriaux);


        //créer la table techniques
        String sqlTechniques= "create table "+TABLE_TECHNIQUES
                +" ("
                +T_ID+" integer primary key, "
                +T_NOM+" text "
                +") ";

        Log.d("SQL", sqlTechniques);
        db.execSQL(sqlTechniques);


        //créer la table categories
        String sqlCategories= "create table "+TABLE_CATEGORIES
                +" ("
                +C_ID+" integer primary key, "
                +C_NOM+" text "
                +") ";

        Log.d("SQL", sqlCategories);
        db.execSQL(sqlCategories);


        //créer la table oeuvres
        String sqlOeuvres = "create table "+TABLE_OEUVRES
                +" ("
                +O_ID+" text primary key, " //risque de ne pas fonctionner, qu'est-ce que le type correct?
                +O_TITRE+" text, "
                +O_DIMENSION+" text, "
                +O_COORD_LAT+" number, "
                +O_COORD_LONG+" number, "
                +O_URI_IMAGE+" text, "
                +O_DATE_IMAGE+" text, "
                +O_ETAT+" text, "
                +O_QUARTIER+" text, "
                +O_MATERIAU+" text, "
                +O_TECHNIQUE+" text, "
                +O_CATEGORIE+" text, "
                +O_DATE_PROD+" text, "
                +O_COMMENT+" text, "
                +O_RATING+" number, "
                +"CONSTRAINT "+O_QUARTIER+"_FK foreign key ("+O_QUARTIER+
                ") references "+TABLE_QUARTIERS+"("+Q_ID+"), "

                +"CONSTRAINT "+O_MATERIAU+"_FK foreign key ("+O_MATERIAU+
                ") references "+TABLE_MATERIAUX+"("+M_ID+"), "

                +"CONSTRAINT "+O_TECHNIQUE+"_FK foreign key ("+O_TECHNIQUE+
                ") references "+TABLE_TECHNIQUES+"("+T_ID+"), "

                +"CONSTRAINT "+O_CATEGORIE+"_FK foreign key ("+O_CATEGORIE+
                ") references "+TABLE_CATEGORIES+"("+C_ID+") "

                +") ";

        Log.d("SQL", sqlOeuvres);
        db.execSQL(sqlOeuvres);


        String sqlOeuvreArtiste ="create table "+TABLE_OEUVRE_ARTISTE
                +" ("
                +OA_ID_OEUVRE+" text, "
                +OA_ID_ARTISTE+" integer, "
                +"CONSTRAINT "+OA_ID_OEUVRE+"_FK foreign key ("+OA_ID_OEUVRE+
                ") references "+TABLE_OEUVRES+" ("+O_ID+"), "
                +"CONSTRAINT "+OA_ID_ARTISTE+"_FK foreign key ("+OA_ID_ARTISTE+
                ") references "+TABLE_ARTISTES+" ("+A_ID+"), "
                +"CONSTRAINT table_oeuvre_artiste_PK primary key ("+OA_ID_OEUVRE
                +", "+OA_ID_ARTISTE+")"
                +") ";

        Log.d("SQL", sqlOeuvreArtiste);
        db.execSQL((sqlOeuvreArtiste));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //probablement pas comme ça vu qu'il faut conserver toutes les données spécifiques de l'utilisateur
        //telles que les photos, dates des photos et état des oeuvres
        db.execSQL("drop table if exists " + TABLE_ARTISTES);
        db.execSQL("drop table if exists " + TABLE_QUARTIERS);
        db.execSQL("drop table if exists " + TABLE_MATERIAUX);
        db.execSQL("drop table if exists " + TABLE_TECHNIQUES);
        db.execSQL("drop table if exists " + TABLE_CATEGORIES);
        db.execSQL("drop table if exists " + TABLE_OEUVRE_ARTISTE);
        db.execSQL("drop table if exists " + TABLE_OEUVRES);

        onCreate(db);
    }

    //ajoute les artistes
    public void ajouteArtiste(Oeuvre[] root){
        Oeuvre oeuvre;
        Artiste artiste;
        List<Artiste> artistes;
        // ArrayList<Artiste> tousLesArtistes = new ArrayList<Artiste>();


        ContentValues cv = new ContentValues();
        for (int i = 0; i < root.length; i++){
            oeuvre = root[i];
            artistes = oeuvre.Artistes;
            for (int j = 0; j < artistes.size(); j++) {

                artiste = artistes.get(j);
                //ajouter les valeurs du json comme contenu par colonne

                cv.put(A_ID, artiste.NoInterne);
                cv.put(A_NOM, artiste.Nom);
                cv.put(A_PRENOM, artiste.Prenom);
                cv.put(A_NOM_COLLECTIF, artiste.NomCollectif);

                try {
                    db.insertOrThrow(TABLE_ARTISTES, null, cv);
                } catch (SQLException e) {};

            }

        }
        Log.d("RemplirSQL", "ajout des artistes complété");
    }

    //ajoute les quartiers
    public void ajouteQuartier(Oeuvre[] root){
        Oeuvre oeuvre;
        String quartier;
        int compte = 1;
        ArrayList<String> tousLesQuartiers = new ArrayList<String>();


        ContentValues cv = new ContentValues();
        for (int i = 0; i < root.length; i++) {
            oeuvre = root[i];
            quartier = oeuvre.Arrondissement;
           if (!tousLesQuartiers.contains(quartier)){
                cv.put(Q_ID, compte);
                cv.put(Q_NOM, quartier);
                try{
                    db.insertOrThrow(TABLE_QUARTIERS, null, cv);
                    } catch (Exception e) {};
            compte++;
            tousLesQuartiers.add(quartier);
            }
        }
        Log.d("RemplirSQL", "ajout des quartiers complété");
    }

    //ajoute les materiaux
    public void ajouteMateriau(Oeuvre[] root){
        Oeuvre oeuvre;
        String materiau;
        ArrayList<String> tousLesMateriaux = new ArrayList<String>();
        int compte = 1;


        ContentValues cv = new ContentValues();
        for (int i = 0; i < root.length; i++) {
            oeuvre = root[i];
            materiau = oeuvre.Materiaux;
            if (!tousLesMateriaux.contains(materiau)){
                cv.put(M_ID, compte);
                cv.put(M_NOM, materiau);
                try{
                    db.insertOrThrow(TABLE_MATERIAUX, null, cv);
                } catch (Exception e) {};
                compte++;
                tousLesMateriaux.add(materiau);

           }
        }
        Log.d("RemplirSQL", "ajout des matériaux complété");
    }

    //ajoute les techniques
    public void ajouteTechnique(Oeuvre[] root){
        Oeuvre oeuvre;
        String technique;
        ArrayList<String> toutesLesTechniques = new ArrayList<String>();
        int compte=1;

        ContentValues cv = new ContentValues();
        for (int i = 0; i < root.length; i++) {
            oeuvre = root[i];
            technique = oeuvre.Technique;

            if (!toutesLesTechniques.contains(technique)){
                cv.put(T_ID, compte);
                cv.put(T_NOM, technique);
                try{
                    db.insertOrThrow(TABLE_TECHNIQUES, null, cv);
                } catch (Exception e) {};
                compte++;
                toutesLesTechniques.add(technique);
            }
        }

        Log.d("RemplirSQL", "ajout des techniques complété");
    }

    //ajoute les catégories
    public void ajouteCategorie(Oeuvre[] root){
        Oeuvre oeuvre;
        String categorie;
        ArrayList<String> toutesLesCategories = new ArrayList<String>();
        int compte =1;

        ContentValues cv = new ContentValues();
        for (int i = 0; i < root.length; i++) {
            oeuvre = root[i];
            categorie = oeuvre.SousCategorieObjet;
            if (!toutesLesCategories.contains(categorie)){
                cv.put(C_ID, compte);
                cv.put(C_NOM, categorie);
                try{
                    db.insertOrThrow(TABLE_CATEGORIES, null, cv);
                } catch (Exception e) {};
                compte++;
                toutesLesCategories.add(categorie);
            }
        }
        Log.d("RemplirSQL", "ajout des catégories complété");
    }


    //cursor pour parcourir les tables créées préalablmement
    // pour trouver le foreign key correspondant
    // -1 si ...
    public int trouverID(String TABLE, String COLONNE, String valeur, String COLONNE_ID){
        Cursor c;
        c = db.rawQuery("select "+COLONNE_ID+" from "+TABLE+" where "+COLONNE+" = ? ",new String[] {valeur});
        if( c.getCount()==0 ) { return(-1); }
        c.moveToFirst();
        int key=c.getInt(0);
        c.close();
        return key;
    }

    //pour le lien artiste_oeuvre, il faut cherche avec le numéro interne de l'artiste,
    //valeur est un int et non un string
    //d'où le deuxième constructeur
    public int trouverID(String TABLE, String COLONNE, int valeur, String COLONNE_ID){
        Cursor c;
        c = db.rawQuery("select "+COLONNE_ID+" from "+TABLE+" where "+COLONNE+" = "+valeur+" ", null);
        if( c.getCount()==0 ) { return(-1); }
        c.moveToFirst();
        int key=c.getInt(0);
        c.close();
        return key;
    }

    public void ajouteOeuvres(Oeuvre[] root){
        Oeuvre oeuvre;
        List<Artiste> artistes;

        ContentValues cv = new ContentValues();

        //pour la liste oeuvre_artiste
        ContentValues cv_oa = new ContentValues();

        for (int i = 0; i < root.length; i++){
            oeuvre = root[i];
            cv.clear();
            //ajouter les valeurs du json comme contenu par colonne
            cv.put(O_ID, oeuvre.NumeroAccession);
            cv.put(O_TITRE, oeuvre.Titre);
            cv.put(O_DIMENSION, oeuvre.DimensionsGenerales);
            cv.put(O_COORD_LAT, oeuvre.CoordonneeLatitude);
            cv.put(O_COORD_LONG, oeuvre.CoordonneeLongitude);
            cv.put(O_URI_IMAGE, URI_DEF); //image par défaut
            cv.put(O_DATE_IMAGE, ""); //on n'a pas encore de données
            cv.put(O_ETAT, ETAT_NORMAL); //pour l'instant, c'est ok
            cv.put(O_DATE_PROD, oeuvre.DateFinProduction);
            cv.put(O_COMMENT, "");
            cv.put(O_RATING, RATING_DEF);


            // on entre dans les données qui sont issues de d'autres tables
            int key_quartier = trouverID(TABLE_QUARTIERS, Q_NOM, oeuvre.Arrondissement, Q_ID);
            cv.put(O_QUARTIER, key_quartier);


            int key_materiau = trouverID(TABLE_MATERIAUX, M_NOM, oeuvre.Materiaux, M_ID);
            cv.put(O_MATERIAU, key_materiau);

            int key_technique = trouverID(TABLE_TECHNIQUES, T_NOM, oeuvre.Technique, T_ID);
            cv.put(O_TECHNIQUE, key_technique);

            int key_categorie = trouverID(TABLE_CATEGORIES, C_NOM, oeuvre.SousCategorieObjet, C_ID);
            cv.put(O_CATEGORIE, key_categorie);

            try{
                db.insertOrThrow(TABLE_OEUVRES, null, cv);
            } catch (Exception e){};


            //ajouter le lien de création

            //récupère la liste des artistes pour cette oeuvre
            artistes = oeuvre.Artistes;

            //le id de l'oeuvre sera son numéro d'accession
            cv_oa.put(OA_ID_OEUVRE, oeuvre.NumeroAccession);

            //récupère les clefs de chaque artiste
            for (int j = 0; j < artistes.size(); j++ ){
                int num = oeuvre.Artistes.get(j).NoInterne;
                int key_artiste = trouverID(TABLE_ARTISTES, A_ID, num, A_ID);

                cv_oa.put(OA_ID_ARTISTE, key_artiste);
                try{
                    db.insertOrThrow(TABLE_OEUVRE_ARTISTE, null, cv_oa);
                } catch (Exception e){};
            }

        }

        Log.d("RemplirSQL", "ajout des oeuvres et des liens oeuvre-artiste complété");
    }


    //cursor pour retourner données
    public Cursor listeTable(String TABLE, String COL){
        Cursor c;

        c = db.rawQuery("select * from "+TABLE+" order by     "
                +COL+" asc", null);
        return c;
    }

    public Cursor listeTableOrd(String TABLE, String COL_RECHERCHE, String valeur, String COL_ORDRE){
        Cursor c;
        c = db.rawQuery("select * from "+TABLE+" where "+COL_RECHERCHE+" = '"+valeur+"' order by "
                +COL_ORDRE+" desc", null);
        return c;

    }


    public Cursor retourneOeuvre(String numOeuvre){
        Cursor c;

        c = db.rawQuery("select * from "+TABLE_OEUVRES+" where "+O_ID+" = '"+numOeuvre+"' ", null);
        int nombre = c.getCount();

        return c;
    }

    /*

    public Oeuvre retourneOeuvreComplete (String numOeuvre){
        Cursor c;
        Oeuvre oeuvre = new Oeuvre();
        c = db.rawQuery("select * from " + TABLE_OEUVRES + " where " + O_ID + " = '" + numOeuvre + "' ", null);
        c.moveToFirst();

        oeuvre.NumeroAccession = c.getString(c.getColumnIndex(O_ID));

        oeuvre.Titre= c.getString(c.getColumnIndex(O_TITRE));
        oeuvre.DimensionsGenerales = c.getString(c.getColumnIndex(O_DIMENSION));
        oeuvre.DateFinProduction = c.getString(c.getColumnIndex(O_DATE_PROD));

        String val_a = c.getString(c.getColumnIndex(O_QUARTIER));
        oeuvre.Arrondissement = retourneNom(TABLE_QUARTIERS, Q_ID, val_a, Q_NOM);

        String val_t = c.getString(c.getColumnIndex(O_TECHNIQUE));
        oeuvre.Technique = retourneNom(TABLE_TECHNIQUES, T_ID, val_t, T_NOM);

        String val_c = c.getString(c.getColumnIndex(O_CATEGORIE));
        oeuvre.SousCategorieObjet = retourneNom(DBHelper.TABLE_CATEGORIES, DBHelper.C_ID, val_c, DBHelper.C_NOM);

        String val_m = c.getString(c.getColumnIndex(O_MATERIAU));
        oeuvre.Materiaux= retourneNom(DBHelper.TABLE_MATERIAUX, DBHelper.M_ID, val_m, DBHelper.M_NOM);

        //il faut récupérer le string avec les noms d'artistes depuis là où on en a besoin
        // avec la méthode retourneNomsArtistes();

        //il faut récupérer les données pour les photos
        //+ commentaires et rating
        c.close();
        return oeuvre;

    }

    */


    public String retourneNom(String TABLE, String COLONNE_RECHERCHE, String valeur, String COLONNE_RESULTAT ){
        Cursor c;
        String nom;
        c = db.rawQuery("select "+COLONNE_RESULTAT+" from "+TABLE+" where "+COLONNE_RECHERCHE+" = '"+valeur+"' ", null);
        //int nombre = c.getCount();
        //if (nombre ==1 ){ pas nécessaire je pense}
        c.moveToFirst();
        nom = c.getString(c.getColumnIndex(COLONNE_RESULTAT));

        c.close();
        return nom;
    }

    public int[] retourneNumArtiste(String numOeuvre){
        Cursor c;

        c = db.rawQuery("select * from " + TABLE_OEUVRE_ARTISTE + " where " + OA_ID_OEUVRE + " = '" + numOeuvre + "' ", null);
        int nombre = c.getCount();

        int[] numbers = new int[nombre];
        int ct=0;
                //key=c.getInt(0);
        c.moveToFirst();
        for(int j=0; j<nombre; j++){
            numbers[j]=(c.getInt((c.getColumnIndex(DBHelper.OA_ID_ARTISTE))));
            c.moveToNext();
        }

        c.close();
        return numbers;
    }

    public Cursor retourneNomArtiste(Object numArtiste){
        Cursor c;
        String number = numArtiste.toString();
        c = db.rawQuery("select * from "+TABLE_ARTISTES+" where "+A_ID+" = '"+number+"' ", null);
        int nombre = c.getCount();
        return c;
    }



    public String retourneNomsArtistes(String numOeuvre){
        int[] artistes;
        int nbrArtistes=0;
        String nom_artistes="";

        artistes = retourneNumArtiste(numOeuvre);
        for (int i=0; i<artistes.length; i++){
            Cursor artist_name;

            artist_name = retourneNomArtiste(artistes[i]);
            artist_name.moveToFirst();

            if (!artist_name.getString(artist_name.getColumnIndex(DBHelper.A_NOM)).equals("")) {
                if (nbrArtistes<1) {
                    String prenom = artist_name.getString(artist_name.getColumnIndex(DBHelper.A_PRENOM));
                    String nom = artist_name.getString(artist_name.getColumnIndex(DBHelper.A_NOM));
                    nom_artistes = prenom + " " + nom;
                }
                else {
                    String prenom = artist_name.getString(artist_name.getColumnIndex(DBHelper.A_PRENOM));
                    String nom = artist_name.getString(artist_name.getColumnIndex(DBHelper.A_NOM));
                    nom_artistes = nom_artistes +", "+ prenom + " " + nom;
                }
            }
            //si c'est un collectif
            else if(artist_name.getString(artist_name.getColumnIndex(DBHelper.A_NOM_COLLECTIF)) != ""){
                String nom = artist_name.getString(artist_name.getColumnIndex(DBHelper.A_NOM_COLLECTIF));
                nom_artistes = nom_artistes +  nom + " ";
            }
            else{
                Log.d("fiche", "erreur dans le nom de l'artiste");
            }
            artist_name.close();
            nbrArtistes++;
        }

        return nom_artistes;
    }

    public void changeEtat(String numOeuvre, String nouvelEtat){
        //nouvel état est l'état vers lequel on aimerait aller =/= état actuel de l'oeuvre

        switch (nouvelEtat){
            case ETAT_NORMAL:
                ContentValues cv = new ContentValues();
                cv.put(O_ETAT, ETAT_NORMAL);

                db.update(TABLE_OEUVRES, cv, O_ID + " = '" + numOeuvre + "' ", null);

                break;
            case ETAT_FAVORIS:
                ContentValues cv2 = new ContentValues();
                cv2.put(O_ETAT, ETAT_FAVORIS);

                db.update(TABLE_OEUVRES, cv2, O_ID + " = '" + numOeuvre + "' ", null);

                break;
            case ETAT_GALERIE:
                ContentValues cv3 = new ContentValues();
                cv3.put(O_ETAT, ETAT_GALERIE);

                db.update(TABLE_OEUVRES, cv3, O_ID + " = '" + numOeuvre + "' ", null);

                break;
            default:
                Log.d("dbh", "etat oeuvre = "+nouvelEtat+"et ne convient pas");
                break;
        }

    }

    public void ajoutePhoto(String numOeuvre, String uri, String date){
        ContentValues cv = new ContentValues();

        cv.put(O_DATE_IMAGE, date);
        cv.put(O_URI_IMAGE, uri);
        cv.put(O_ETAT, ETAT_GALERIE);

        db.update(TABLE_OEUVRES, cv, O_ID + " = '" + numOeuvre + "' ", null);
    }

    public void ajouteComment (String numOeuvre, String comment){
        ContentValues cv = new ContentValues();

        cv.put(O_COMMENT, comment);

        db.update(TABLE_OEUVRES, cv, O_ID + " = '" + numOeuvre + "' ", null);
        Log.d("sql", "comment ajouté");
    }

    public void ajouteRating (String numOeuvre, int rating){
        ContentValues cv = new ContentValues();

        cv.put(O_RATING, rating);

        db.update(TABLE_OEUVRES, cv, O_ID + " = '" + numOeuvre + "' ", null);
        Log.d("sql", "rating ajouté");
    }

    public String retourneURIphoto(String numOeuvre){
        Cursor c;
        c = db.rawQuery("select "+O_URI_IMAGE+" from " + TABLE_OEUVRES + " where " + O_ID + " = '" + numOeuvre + "' ", null);
        c.moveToFirst();

        String uri_photo = c.getString(c.getColumnIndex(DBHelper.O_URI_IMAGE));

        return uri_photo;
    }

    public String retourneDatephoto(String numOeuvre){
        Cursor c;
        c = db.rawQuery("select "+O_DATE_IMAGE+" from " + TABLE_OEUVRES + " where " + O_ID + " = '" + numOeuvre + "' ", null);
        c.moveToFirst();

        String date = c.getString(c.getColumnIndex(DBHelper.O_DATE_IMAGE));
        //Log.d("date", "string is "+date);

        //ajuste le format de date à un format "humain" jour mois année

        String annee = date.substring(0, 4);
        String mois = date.substring (4, 6);
        String jour = date.substring(6, 8);

        /*
        String short_date = date.substring(6, date.length() - 7);

        long dv = Long.valueOf(short_date);//
        Date df = new java.util.Date(dv);

        String jour = new SimpleDateFormat("dd").format(df);
        String mois = new SimpleDateFormat("MM").format(df);
        String annee = new SimpleDateFormat("yyyy").format(df);
        */
        switch (mois){

            case "01":
                mois="janvier";
                break;
            case "02":
                mois="février";
                break;
            case "03":
                mois="mars";
                break;
            case "04":
                mois="avril";
                break;
            case "05":
                mois="mai";
                break;
            case "06":
                mois="juin";
                break;
            case "07":
                mois="juillet";
                break;
            case "08":
                mois="août";
                break;
            case "09":
                mois="septembre";
                break;
            case "10":
                mois="octobre";
                break;
            case "11":
                mois="novembre";
                break;
            case "12":
                mois="décembre";
                break;
        }

        String date_o = jour+" "+mois+" "+annee;

        return date_o;
    }

}

