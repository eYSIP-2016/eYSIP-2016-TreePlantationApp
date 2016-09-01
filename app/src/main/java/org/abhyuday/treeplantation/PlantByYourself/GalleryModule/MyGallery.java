package org.abhyuday.treeplantation.PlantByYourself.GalleryModule;

/**
 * Launched when "Gallery" button is clicked on UserProfile activity in PlantByYourself.
 * Layout files- activity_my_gallery
 *
 * Function -   Display the list of all plant that are planted/adopted by the user.
 *              On selecting any one of the plants, MyFolder activity is opened that display all the pictures of that
 *              particular plant directory.
 *
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.abhyuday.treeplantation.R;
import org.abhyuday.treeplantation.loginmodule.LoginConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class MyGallery extends AppCompatActivity{
    private ListView myPlantsListView;
    public final String GET_MY_TREES_URL = "http://10.129.139.139:9898/plantYourself/plantYourself_getMyTreeList.php";

    public int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gallery);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(LoginConfig.USER_ID_SHARED_PREF,0);

        myPlantsListView = (ListView) findViewById(R.id.listView2);
        getMyTrees();

        myPlantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String MyPlantedTreeImages_Id = (String) parent.getItemAtPosition(position);
                Intent i = new Intent(MyGallery.this,MyFolder.class);
                i.putExtra("MyPlantedTreeImages_Id",MyPlantedTreeImages_Id);
                startActivity(i);
            }
        });
    }


    private void getMyTrees() {
        final ProgressDialog loading = ProgressDialog.show(this,"Downloading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_MY_TREES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        Log.i("myTag", "response is :" + s);
                        fillMyTreesList(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Log.i("myTag","volleyError");
                        Toast.makeText(MyGallery.this,"some error in retrieving data",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();
                params.put("Id",Integer.toString(userId));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private int fillMyTreesList(String response) {
        JSONObject jsonObject;// = null;
        List<String> list = new ArrayList<>();
        Log.i("myTag",response);
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                int MyPlantedTreeImages_Id = jo.getInt("MyPlantedTreeImages_Id");

                list.add(Integer.toString(MyPlantedTreeImages_Id));
            }
            ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
            myPlantsListView.setAdapter(listAdapter);




            return result.length();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("myTag", "exceptionJSON");
            return 0;
        }
    }

}