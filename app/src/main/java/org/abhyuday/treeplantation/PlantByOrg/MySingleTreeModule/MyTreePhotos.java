package org.abhyuday.treeplantation.PlantByOrg.MySingleTreeModule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.abhyuday.treeplantation.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyTreePhotos extends AppCompatActivity {

    String plantedTree_Id;
    ImageView ivThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tree_photos);

        ivThumbnail = (ImageView) findViewById(R.id.ivThumbnail);

        Intent i = getIntent();
        plantedTree_Id = i.getStringExtra(MySingleTreeConfig.PLANTED_TREE_ID_INTENT);

        getMyTreeData(plantedTree_Id);

    }

    private void getMyTreeData(final String pt_Id){
        String url = MySingleTreeConfig.URL_GET_IMAGES.trim() + pt_Id.trim();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i("myTag",response);
                        if(!response.trim().equals("failure")){
                            showJSON(response);
                        }else{
                            Log.i("myTag","error");
                            Toast.makeText(MyTreePhotos.this,"There are no photos for this tree/Some error in getting data",Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        Log.i("myTag","volleyError");
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        String imageurl = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(MySingleTreeConfig.JSON_ARRAY);
            JSONObject MyTreeData = result.getJSONObject(0);
            imageurl = MyTreeData.getString(MySingleTreeConfig.KEY_IMAGE_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Picasso.with(MyTreePhotos.this).load(imageurl).into(ivThumbnail);
    }
}
