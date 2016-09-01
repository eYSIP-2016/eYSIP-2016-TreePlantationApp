package org.abhyuday.treeplantation.PlantByOrg.MySingleTreeModule;

/**
 * Launched when a tree is selected from the list in MyTreeList
 * Layout file - activity_my_single_tree
 * Config file - MySingleTreeConfig
 *
 *
 * Function -   Display the information regarding the tree
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
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.abhyuday.treeplantation.R;
import org.abhyuday.treeplantation.loginmodule.LoginConfig;

public class MySingleTree extends AppCompatActivity {

    TextView tvTreeInfo;
    Button btnViewPhotos;
    Button btnUploadPhoto;
    String plantedTreeId;
    String TreeId;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_single_tree);
        tvTreeInfo = (TextView) findViewById(R.id.tvTreeInfo);
        btnViewPhotos = (Button) findViewById(R.id.btnPhotos);
        btnUploadPhoto = (Button) findViewById(R.id.btnUploadPhoto);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(LoginConfig.USER_ID_SHARED_PREF,0);

        Intent currentIntent = getIntent();
        plantedTreeId = currentIntent.getStringExtra(MySingleTreeConfig.PLANTED_TREE_ID_INTENT);
        TreeId = currentIntent.getStringExtra(MySingleTreeConfig.TREE_ID_INTENT);


        String url = MySingleTreeConfig.URL_PLANTED_TREE_INFO+ (plantedTreeId);
        getResponse(url);

        btnViewPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myTreePhotoIntent = new Intent(MySingleTree.this, MyTreePhotos.class);
                myTreePhotoIntent.putExtra(MySingleTreeConfig.PLANTED_TREE_ID_INTENT,plantedTreeId);
                startActivity(myTreePhotoIntent);
            }
        });

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadPhotoIntent = new Intent(MySingleTree.this, UploadMyTreePhoto.class);
                uploadPhotoIntent.putExtra(MySingleTreeConfig.PLANTED_TREE_ID_INTENT,plantedTreeId);
                uploadPhotoIntent.putExtra(MySingleTreeConfig.TREE_ID_INTENT,TreeId);
                startActivity(uploadPhotoIntent);
            }
        });


    }

    private void getResponse(String url){
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parseResponse(response);
                tvTreeInfo.setText(response);
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
