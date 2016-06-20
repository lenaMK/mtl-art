package com.example.dajc.tabs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
 * Created by LenaMK on 18/04/2016.
 */
public class MapFragment extends Fragment implements LocationListener, View.OnClickListener {

    Location mLastLocation;
    GeoPoint startPoint;
    MapView map;

    GeoPoint myLocation;
    ResourceProxy mResourceProxy;
    IMapController mapController;


    //attention, ces coordonnées existent donc si c'est le cas il y aura un bug
    public double lati = 0;
    public double longi = 0;

    double mtl_lati;
    double mtl_longi;

    ImageButton listViewButton;
    ImageButton locationButton;

    View v;

    DBHelper dbh;

    public MapFragment (){
        this.dbh = FirstActivity.getDBH();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.map_frag_layout, container, false);
        listViewButton = (ImageButton) v.findViewById(R.id.button_listView);
        listViewButton.setOnClickListener(this);

        locationButton = (ImageButton) v.findViewById(R.id.button_location);
        locationButton.setOnClickListener(this);

        //set map view
        map = (MapView) v.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //set default zoom buttons and ability to zoom with fingers
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //set map default view point
        mapController = map.getController();
        mapController.setZoom(15);

        // localisation
        LocationManager locm = (LocationManager)getActivity(). getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(),"Pas de permission",Toast.LENGTH_LONG).show();
        }else {
            locm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }



        //set the start point to user last known location or to mtl
        mtl_lati = 45.508567;
        mtl_longi = -73.566455;
        if (lati != 0 && longi != 0) {
            startPoint = new GeoPoint(lati, longi);
            Log.d("map", "start set to phone location");
        } else {
            startPoint = new GeoPoint(mtl_lati, mtl_longi);
            Log.d("map", "start set to default location");
        }
        mapController.setCenter(startPoint);



        //adding specific pins: create array + add as in example
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
       // ItemizedIconOverlay<OverlayItem> items = new ItemizedIconOverlay<OverlayItem>(
        //         new ArrayList<OverlayItem>(), artMarker, null,
         //       new DefaultResourceProxyImpl(getActivity()));


        // example items.add(new OverlayItem("Title", "Description", new GeoPoint(0.0d,0.0d))); // Lat/Lon decimal degrees
        String title;
        String id;
        double art_lati;
        double art_longi;
        int item_nb = 0;
        OverlayItem myOverlayItem;

        //dbh = new DBHelper(Activity.this);

        Cursor c = dbh.listeTable(DBHelper.TABLE_OEUVRES, DBHelper.O_TITRE);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            title = c.getString(c.getColumnIndex(DBHelper.O_TITRE));
            id = c.getString(c.getColumnIndex(DBHelper.O_ID));

            art_lati = c.getDouble(c.getColumnIndex(DBHelper.O_COORD_LAT));
            art_longi = c.getDouble(c.getColumnIndex(DBHelper.O_COORD_LONG));

            myOverlayItem = new OverlayItem(title, id, new GeoPoint(art_lati, art_longi));
            items.add(myOverlayItem);

            item_nb++;
            c.moveToNext();

        }

        Log.d("map", "Items added = " + item_nb);

        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> iOverlay = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
       //ItemizedIconOverlay iOverlay = new ItemizedIconOverlay(overlay, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                //show the state
                Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                //shows the name of the artwork
                //should work on getting the fiche


                Intent intent = new Intent(getActivity(), FicheActivity.class);
                intent.putExtra("numOeuvre", item.getSnippet());
                startActivity(intent);

                return false;
            }

        };

        mResourceProxy = new DefaultResourceProxyImpl(getContext());

        //the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items, iOverlay
                , mResourceProxy);
        mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);


/*
        Drawable artMarker = getResources().getDrawable(R.drawable.ic_map_person);
        ItemizedIconOverlay<OverlayItem> overlay = new ItemizedIconOverlay<OverlayItem>(
                items, artMarker, null,
                new DefaultResourceProxyImpl(getActivity()));

        map.getOverlays().add(overlay);
*/
        Log.d("map", "Overlay added");

        return v;

    }

    // fonctions de localisation

    @Override
    public void onLocationChanged(Location location) {
        Log.d("gps","location changed "+location.getLatitude()+","+location.getLongitude());
        lati = location.getLatitude();
        longi = location.getLongitude();

        /* do not change startpoint(center of map)
        startPoint = new GeoPoint(lati,longi);
        mapController.setCenter(startPoint);
        */


        if (this.getView() != null) {
            this.setMarker(); //set an own marker, attention, this creates a new marker every time!
        }

        map.postInvalidate();


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
        //case marker already exists, should be removed and replaced by new one
        Drawable personMarker = getResources().getDrawable(R.mipmap.ic_map_person);
        ItemizedIconOverlay<OverlayItem> overlay = new ItemizedIconOverlay<OverlayItem>(
                new ArrayList<OverlayItem>(), personMarker, null,
                new DefaultResourceProxyImpl(getActivity()));
        // gc: last GeoPoint

        //remove last Marker
        overlay.removeAllItems();
        //map.updateViewLayout(v);


        myLocation = new GeoPoint(lati, longi);
        OverlayItem item = new OverlayItem(null, null, myLocation);

        overlay.addItem(item);
        map.getOverlays().add(overlay);

        Log.d("gps", "my location added");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_listView:
                Intent intent = new Intent(getActivity(), ListViewActivity.class);
                String longi_s;
                String lati_s;
                if (myLocation != null) {
                    longi_s = "" + myLocation.getLongitude();
                    lati_s = "" + myLocation.getLatitude();
                } else {
                    longi_s = "";
                    lati_s = "";
                }
                intent.putExtra("Geo_longi", longi_s);
                intent.putExtra("Geo_lati", lati_s);
                startActivity(intent);
                break;
            case R.id.button_location:
                startPoint = new GeoPoint(lati, longi);
                mapController.setCenter(startPoint);
                break;
        }
    }
}
