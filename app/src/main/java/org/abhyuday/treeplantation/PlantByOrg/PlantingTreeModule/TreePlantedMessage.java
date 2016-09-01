package org.abhyuday.treeplantation.PlantByOrg.PlantingTreeModule;

/**
 * Launched when "plant here" button on the toolbar is clicked on PlantHereMap activity
 * Layout files- activity_tree_planted_message
 * Configuration files used to upload to the server - PlantingTreeConfig
 *
 * Function -   Give a name to your plant and upload it to the server via a Volley request.
 *
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.abhyuday.treeplantation.R;
import org.abhyuday.treeplantation.PlantByOrg.SingleTreeModule.TreeListDataConfig;
import org.abhyuday.treeplantation.UserProfile;
import org.abhyuday.treeplantation.loginmodule.LoginConfig;

import java.util.HashMap;
import java.util.Map;

public class TreePlantedMessage extends AppCompatActivity {

    TextView Longitude;
    TextView Latitude;
    Button btnProfile;
    Button btnPlantNow;
    EditText etTreeName;

    public int treeId;
    public int userId;
    public double latitude;
    public double longitude;
    public String treeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_planted_message);

        Longitude = (TextView) findViewById(R.id.Longitude);
        Latitude = (TextView) findViewById(R.id.Latitude);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        btnPlantNow = (Button) findViewById(R.id.btnPlantNow);
        etTreeName = (EditText) findViewById(R.id.etTreeName);

        //fetching treeId from the intent
        Intent currentIntent = getIntent();
        latitude = currentIntent.getDoubleExtra(PlantHereMap.KEY_LATITUDE,0.0);
        longitude = currentIntent.getDoubleExtra(PlantHereMap.KEY_LONGITUDE, 0.0);
        treeId = Integer.parseInt(currentIntent.getStringExtra(TreeListDataConfig.TREE_ID_INTENT));

        //fetching userId from the shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(LoginConfig.USER_ID_SHARED_PREF,0);

        Latitude.setText(String.format("%s %.15f","Latitude: ", latitude));
        Longitude.setText(String.format("%s %.15f","Longitude: ", longitude));

        btnPlantNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                treeName = etTreeName.getText().toString();
                if (!treeName.equals("")) {
                    plantNow();
                }
                else{
                    Toast.makeText(TreePlantedMessage.this,"Please enter a name for your tree", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(TreePlantedMessage.this, UserProfile.class);
                startActivity(profileIntent);

            }
        });

    }

    public void plantNow(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PlantingTreeConfig.INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.trim().equals(PlantingTreeConfig.MESSAGE_SUCCESS)){
                            Toast.makeText(TreePlantedMessage.this, "Planted Successfully", Toast.LENGTH_SHORT).show();
                            Intent profileIntent = new Intent(TreePlantedMessage.this, UserProfile.class);
                            startActivity(profileIntent);
                        }else{
                            Toast.makeText(TreePlantedMessage.this,  response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        Toast.makeText(TreePlantedMessage.this,  "volleyError", Toast.LENGTH_SHORT).show();
                        Log.i("myTag","volleyError");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(PlantingTreeConfig.KEY_TREE_ID, Integer.toString(treeId));
                params.put(PlantingTreeConfig.KEY_USER_ID, Integer.toString(userId));
                params.put(PlantingTreeConfig.KEY_TREE_NAME, treeName);
                params.put(PlantingTreeConfig.KEY_LATITUDE, Double.toString(latitude));
                params.put(PlantingTreeConfig.KEY_LONGITUDE, Double.toString(longitude));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
