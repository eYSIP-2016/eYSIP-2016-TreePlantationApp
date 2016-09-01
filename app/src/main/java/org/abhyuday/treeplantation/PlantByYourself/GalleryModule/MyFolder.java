package org.abhyuday.treeplantation.PlantByYourself.GalleryModule;

/**
 * Launched when an item is selected from the list in MyGallery activity
 * Associated adapter file for displaying list - AdapterFolder.java
 * Layout files- activity_my_folder
 *
 * Function -   Display all the images of the plant that were posted by the user
 *
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class MyFolder extends AppCompatActivity {

    public String MyPlantedTreeImages_Id;
    private ArrayList<String> list = new ArrayList<>();
    public final String GET_MY_FOLDER_URL = "http://10.129.139.139:9898/plantYourself/plantYourself_getMyTreeFolder.php";
    private ListView myFolderListView;

    private AdapterFolder adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_folder);
        myFolderListView = (ListView) findViewById(R.id.myFolderListView);
        list = new ArrayList<>();
        adapter = new AdapterFolder(this, list);

        Intent i = getIntent();
        MyPlantedTreeImages_Id= i.getStringExtra("MyPlantedTreeImages_Id");
        Log.i("myTag", "MyPlantedTreeImages_Id : " + MyPlantedTreeImages_Id);
        getImages();

    }

    private void getImages(){
        final ProgressDialog loading = ProgressDialog.show(this,"Downloading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_MY_FOLDER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        Log.i("myTag", "response is :" + s);
                        parseResponse(s);
                        //Toast.makeText(MyFolder.this,s,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Log.i("myTag","volleyError");
                        Toast.makeText(MyFolder.this,"some error in retrieving data",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();
                params.put("MyPlantedTreeImages_Id",MyPlantedTreeImages_Id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseResponse(String response){
        JSONObject jsonObject;// = null;


        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject data = result.getJSONObject(0);
            int nImages = data.getInt("MyPlantedTreeImages_nImages");
            String imageDir = data.getString("MyPlantedTreeImages_ImageDir");
            for(int i = 0; i<nImages; i++){
                String imageUrl = imageDir + "/" + i + ".jpg";
                Log.i("myTag",imageUrl);
                list.add(imageUrl);
            }

            myFolderListView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("myTag","exceptionJSON");
            Toast.makeText(MyFolder.this,"exceptionJSON", Toast.LENGTH_LONG ).show();
        }
    }

}
