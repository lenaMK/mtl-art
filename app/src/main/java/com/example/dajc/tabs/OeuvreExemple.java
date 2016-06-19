package com.example.dajc.tabs;

/**
 * Created by LenaMK on 26/04/2016.
 */
public class OeuvreExemple {

        String titre;
        String artiste_nom;
        String artiste_prenom;
        String dimension;
        String technique;
        String quartier;
        String type;
        String url;

        public OeuvreExemple (String titre, String nom, String prenom, String size, String tech, String quart, String type, String url){
            this.titre = titre;
            this.artiste_nom = nom;
            this.artiste_prenom = prenom;
            this.dimension = size;
            this.technique = tech;
            this.quartier = quart;
            this.type = type;
            this.url = url;

        }

}

