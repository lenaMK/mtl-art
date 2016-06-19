package com.example.dajc.tabs.WebAPI;

import com.example.dajc.tabs.DBHelper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by DAJC on 2016-04-17.
 */
public class WebAPI {
    public String url;
    public Oeuvre[] root;
    DBHelper dbh;

    public WebAPI(DBHelper dbh){
        url = "http://donnees.ville.montreal.qc.ca/dataset/2980db3a-9eb4-4c0e-b7c6-a6584cb769c9/resource/18705524-c8a6-49a0-bca7-92f493e6d329/download/oeuvresdonneesouvertes.json";
        this.dbh = dbh;
    }

    public Oeuvre[] run () throws IOException {

        //recupere contenu html a partir de url
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();

        //parser le contenu json
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Oeuvre[]> jsonAdapter;
        jsonAdapter = moshi.adapter(Oeuvre[].class);
        Oeuvre[] root = jsonAdapter.fromJson(json);
        //recuperer seulement une partie...pas necessaire??
        //Oeuvre oeuvres;

        repareTextesNuls(root);

        if (root.length != 0){
            //ajout des artistes
            dbh.ajouteArtiste(root);

            //ajout des quartiers
            dbh.ajouteQuartier(root);

            //ajoute les matériaux
            dbh.ajouteMateriau(root);

            //ajoute les techniques
            dbh.ajouteTechnique(root);

            //ajoute les catégories
            dbh.ajouteCategorie(root);

            //ajoute les oeuvre et les liens oeuvre-artiste
            dbh.ajouteOeuvres(root);
        }



        return root;

    }

    public void repareTextesNuls(Oeuvre[] root) {
        for (int i = 0; i < root.length; i++) {
            if (root[i].Technique == null) root[i].Technique = "(inconnue)";
            if (root[i].Arrondissement == null) root[i].Arrondissement = "(inconnu)";
            if (root[i].Materiaux == null) root[i].Materiaux = "(inconnu)";
            if (root[i].SousCategorieObjet == null) root[i].SousCategorieObjet = "(inconnue)";
            if(root[i].DimensionsGenerales == null) root[i].DimensionsGenerales = "(inconnues)";

            //attention: pourrait donner autre info?
            for (int j = 0; j < root[i].Artistes.size(); j++) {
                if (root[i].Artistes.get(j).Nom == null) {
                    root[i].Artistes.get(j).Nom = "";
                }
                if (root[i].Artistes.get(j).Prenom == null) {
                    root[i].Artistes.get(j).Prenom = "";
                }
                if (root[i].Artistes.get(j).NomCollectif == null) {
                    root[i].Artistes.get(j).NomCollectif = "";
                }
            }


            if (root[i].DateFinProduction == null){
                root[i].DateFinProduction = "(inconnue)";

            }else {
                String date_prod = root[i].DateFinProduction;

                String short_date = date_prod.substring(6, date_prod.length() - 7);

                long dv = Long.valueOf(short_date);//
                Date df = new java.util.Date(dv);

                String jour = new SimpleDateFormat("dd").format(df);
                String mois = new SimpleDateFormat("MM").format(df);
                String annee = new SimpleDateFormat("yyyy").format(df);

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

                root[i].DateFinProduction = date_o ;
            }


        }
    }
}
