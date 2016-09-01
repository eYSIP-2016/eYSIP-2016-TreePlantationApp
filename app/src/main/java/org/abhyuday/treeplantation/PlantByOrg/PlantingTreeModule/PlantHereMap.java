package org.abhyuday.treeplantation.PlantByOrg.PlantingTreeModule;

/**
 * Launched when "Plant this tree" button is clicked on SingleTreeData activity
 * Layout files- fragment_plant_here_map
 * fragment id - plantHereMap
 *
 *
 * Function -   Display the map of world.
 *              Long press on a location to mark and click "plant here" button on the toolbar to proceed.
 *              TreePlantedMessage activity is opened next.
 *
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.abhyuday.treeplantation.R;
import org.abhyuday.treeplantation.PlantByOrg.SingleTreeModule.TreeListDataConfig;

public class PlantHereMap extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener
   {

    private GoogleMap mMap;
    public double lastSelectedLatitude; // for now,planted only on the last marked marker
    public double lastSelectedLongitude;
    public static final String KEY_LATITUDE = "lastSelectedLatitude" ;
    public static final String KEY_LONGITUDE = "lastSelectedLongitude" ;
    public String treeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_plant_here_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.plantHereMap);
        mapFragment.getMapAsync(this);

        Intent currIntent = getIntent();
        treeId = currIntent.getStringExtra(TreeListDataConfig.TREE_ID_INTENT);

        lastSelectedLatitude = 19.137153;
        lastSelectedLongitude = 72.913211;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Mumbai and move the camera
        LatLng mumbai = new LatLng(19.137153,72.913211);
        //mMap.addMarker(new MarkerOptions().position(mumbai).title("Marker in Mumbai"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mumbai));
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plant_here_map_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnplantHere: {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Plant your tree here?\nLatitude: " + lastSelectedLatitude + " and Longitude: " + lastSelectedLongitude);

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(PlantHereMap.this, TreePlantedMessage.class);
                        intent.putExtra(KEY_LATITUDE,lastSelectedLatitude);
                        intent.putExtra(KEY_LONGITUDE,lastSelectedLongitude);
                        intent.putExtra(TreeListDataConfig.TREE_ID_INTENT,treeId);
                        startActivity(intent);
                    }
                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


                return true;
            }
            case R.id.maptypeHYBRID:
                if(mMap != null){
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    return true;
                }
            case R.id.maptypeNONE:
                if(mMap != null){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                    return true;
                }
            case R.id.maptypeNORMAL:
                if(mMap != null){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    return true;
                }
            case R.id.maptypeSATELLITE:
                if(mMap != null){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    return true;
                }
            case R.id.maptypeTERRAIN:
                if(mMap != null){
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapClick(LatLng latLng) {


    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(PlantHereMap.this,
                latLng.latitude + " : " + latLng.longitude,
                Toast.LENGTH_LONG).show();

        lastSelectedLatitude = latLng.latitude;
        lastSelectedLongitude= latLng.longitude;


        //Add marker on LongClick position
        MarkerOptions markerOptions =
                new MarkerOptions().position(latLng).title(latLng.toString());
        mMap.addMarker(markerOptions);



    }



}
