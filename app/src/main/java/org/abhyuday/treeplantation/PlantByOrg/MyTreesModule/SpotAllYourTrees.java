package org.abhyuday.treeplantation.PlantByOrg.MyTreesModule;

/**
 * Launched when a spot all button is clicked in UserProfile activity is clicked
 * Layout file - fragment_spot_all_your_trees
 * Map fragment - spotAllMap
 *
 *
 * Function -   Display all the places where the user has planted trees using the PlantByOrg
 *
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.abhyuday.treeplantation.R;
import org.abhyuday.treeplantation.loginmodule.LoginConfig;
import org.abhyuday.treeplantation.PlantByOrg.models.PlantedTreeModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpotAllYourTrees extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<PlantedTreeModel> allPlantedTrees;
    private int userId;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_spot_all_your_trees);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.spotAllMap);
        //mapFragment.getMapAsync(SpotAllYourTrees.this);
        allPlantedTrees = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(LoginConfig.USER_ID_SHARED_PREF,0);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for(int i=0; i< allPlantedTrees.size(); i++){
            PlantedTreeModel currPlantedTree = allPlantedTrees.get(i);
            LatLng latLng = new LatLng(currPlantedTree.getPT_Latitude(),currPlantedTree.getPT_Longitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(currPlantedTree.getPT_Name()));
        }
    }

    private void getAllLatLng(String response){
        JSONObject jsonObject = null;


        try {
            allPlantedTrees.clear();
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(MyTreeListConfig.JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                int PT_ID= jo.getInt(MyTreeListConfig.KEY_PT_ID);
                double PT_LAT= jo.getDouble(MyTreeListConfig.KEY_PT_LAT);
                double PT_LON= jo.getDouble(MyTreeListConfig.KEY_PT_LON);
                String PT_NAME= jo.getString(MyTreeListConfig.KEY_PT_NAME);
                int PT_TREE_ID= jo.getInt(MyTreeListConfig.KEY_PT_TREE_ID);
                int PT_USER_ID= jo.getInt(MyTreeListConfig.KEY_PT_USER_ID);
                String T_GENUS= jo.getString(MyTreeListConfig.KEY_T_GENUS);
                String T_IMAGE_URL= jo.getString(MyTreeListConfig.KEY_T_IMAGE_URL);
                String T_SEED_SAPLING= jo.getString(MyTreeListConfig.KEY_T_SEED_SAPLING);
                String T_SOURCE= jo.getString(MyTreeListConfig.KEY_T_SOURCE);
                String T_NAME= jo.getString(MyTreeListConfig.KEY_T_NAME);

                PlantedTreeModel newPlantedTree = new PlantedTreeModel(PT_ID,PT_LAT,T_NAME,PT_LON,PT_NAME,PT_TREE_ID,PT_USER_ID,T_GENUS,T_IMAGE_URL,T_SEED_SAPLING,T_SOURCE );
                allPlantedTrees.add(newPlantedTree);
            }

            mapFragment.getMapAsync(SpotAllYourTrees.this);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("myTag","exceptionJSON");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = MyTreeListConfig.URL_MY_TREES + Integer.toString(userId);

        getResponse(url);

    }

    private void getResponse(String url){
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getAllLatLng(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag","volleyError");
                  }});

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
