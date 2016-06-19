package com.example.dajc.tabs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

/**
 * Created by LenaMK on 20/04/2016.
 */
public class MapActivity extends Activity implements LocationListener {

    DBHelper dbh;
    MapView map;
    IMapController mapController;
    ResourceProxy mResourceProxy;
    GeoPoint startPoint;
    String numOeuvre;
    Cursor resul_sql;
    double lati;
    double longi;

    String titre;
    String etat;
    String noOeuvre;
    double o_lati;
    double o_longi;
    int marker_personne;


    public MapActivity(){
       this.dbh = FirstActivity.getDBH();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marker_personne = 0;
        setContentView(R.layout.map_layout);

        Intent intent = getIntent();
        numOeuvre = intent.getStringExtra("numOeuvre");

        resul_sql = dbh.retourneOeuvre(numOeuvre);
        resul_sql.moveToFirst();
        titre = resul_sql.getString(resul_sql.getColumnIndex(DBHelper.O_TITRE));
        etat = resul_sql.getString(resul_sql.getColumnIndex(DBHelper.O_ETAT));
        noOeuvre = resul_sql.getString(resul_sql.getColumnIndex(DBHelper.O_ID));
        o_lati = resul_sql.getDouble(resul_sql.getColumnIndex(DBHelper.O_COORD_LAT));
        o_longi = resul_sql.getDouble(resul_sql.getColumnIndex(DBHelper.O_COORD_LONG));

        //set map view
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //set default zoom buttons and ability to zoom with fingers
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //set map default view point
        mapController = map.getController();
        mapController.setZoom(15);

        // localisation
        LocationManager locm = (LocationManager)this. getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Pas de permission", Toast.LENGTH_LONG).show();
        }else {
            locm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }


        startPoint = new GeoPoint(o_lati, o_longi);
        mapController.setCenter(startPoint);

        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

        OverlayItem myOverlayItem;

        myOverlayItem = new OverlayItem(titre, etat, new GeoPoint(o_lati, o_longi));
        items.add(myOverlayItem);

        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> iOverlay = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            //ItemizedIconOverlay iOverlay = new ItemizedIconOverlay(overlay, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                //Toast the name of the work

                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                //shows the name of the artwork
                //should work on getting the fiche

                Toast.makeText(getApplicationContext(), item.getSnippet(), Toast.LENGTH_SHORT).show();

                return false;
            }

        };

        mResourceProxy = new DefaultResourceProxyImpl(this);

        //the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items, iOverlay
                , mResourceProxy);
        mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);

        Toast.makeText(this, titre, Toast.LENGTH_SHORT);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location!= null) {
            Log.d("gps", "location changed " + location.getLatitude() + "," + location.getLongitude());
            lati = location.getLatitude();
            longi = location.getLongitude();
            startPoint = new GeoPoint(lati, longi);

            setMarker(); //set an own marker
            map.postInvalidate();
        }
        else{
            Log.d("gps","location is null");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("gps","status changed");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("gps","provider enabled : "+provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("gps","provider disabled : "+provider);
    }

    void setMarker() {

        Drawable personMarker = getResources().getDrawable(R.mipmap.ic_map_person);
        OverlayItem item;
        ItemizedIconOverlay<OverlayItem> overlay= new ItemizedIconOverlay<OverlayItem>(
                new ArrayList<OverlayItem>(), personMarker, null,
                new DefaultResourceProxyImpl(this));;

        if (marker_personne == 0) {
            // gc: last GeoPoint
            item  = new OverlayItem(null, null, new GeoPoint(lati, longi) {
            });
            overlay.addItem(item);
            map.getOverlays().add(overlay);
            marker_personne = 1;
        } else{
            overlay.removeAllItems();
            item = new OverlayItem(null, null, new GeoPoint(lati, longi) {
            });
            overlay.addItem(item);
            map.getOverlays().add(overlay);
        }
        Log.d("gps", "my location added");
    }
}
