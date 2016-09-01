package org.abhyuday.treeplantation.PlantByOrg.TreeListModule;

/**
 * Launched when "Plant" button is clicked on UserProfile activity in PlantByOrg.
 * Associated Adapter file - CustomAdapterTreeLists
 * Associated config file - TreeListDataConfig.java
 * Layout files- activity_plant_list
 *
 * Function -  Display a list of plants/seeds availabe with the organisation
 *             Lets you search dynamically
 *             On selecting a plant, SingleTreeModule is opened.
 *
 */


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.abhyuday.treeplantation.R;
import org.abhyuday.treeplantation.PlantByOrg.SingleTreeModule.TreeListDataConfig;
import org.abhyuday.treeplantation.PlantByOrg.SingleTreeModule.SingleTreeData;
import org.abhyuday.treeplantation.PlantByOrg.models.TreeModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class  PlantList extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private ArrayList<TreeModel> list = new ArrayList<>();
    private CustomAdapterTreeLists adapterTreeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        ListView plantListView = (ListView) findViewById(R.id.plantListView);
        EditText etSearch = (EditText) findViewById(R.id.etSearch);

        //search bar
        if (etSearch != null) {
            //filter for the listview is called in this method
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapterTreeList.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        //volley request for a string response
        requestForData();

        adapterTreeList = new CustomAdapterTreeLists(this, list);

        plantListView.setAdapter(adapterTreeList);

        plantListView.setOnItemClickListener(this);

    }


    //JSON converted to list and passed to adapterTreeList
    private void parseResponse(String response) {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(TreeListDataConfig.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                int id = jo.getInt(TreeListDataConfig.KEY_ID);
                String name = jo.getString(TreeListDataConfig.KEY_NAME);
                String genus = jo.getString(TreeListDataConfig.KEY_GENUS);
                String source = jo.getString(TreeListDataConfig.KEY_SOURCE);
                String seedsapling = jo.getString(TreeListDataConfig.KEY_SEEDSAPLING);
                String imageUrl = jo.getString(TreeListDataConfig.KEY_IMAGEURL);
                TreeModel newTree = new TreeModel(genus, id, imageUrl, name, seedsapling, source);
                list.add(newTree);
            }
            adapterTreeList.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("myTag", "exceptionJSON");
        }
    }


    //volley request for a string response.. which in turn calls parseResponse to convert to JSON.
    private void requestForData(){
        StringRequest stringRequest = new StringRequest(TreeListDataConfig.VIEW_ALL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId()==R.id.plantListView){
            TreeModel tree = (TreeModel) parent.getItemAtPosition(position);
            Intent iFetchData = new Intent(PlantList.this, SingleTreeData.class);
            iFetchData.putExtra(TreeListDataConfig.TREE_ID_INTENT, Integer.toString(tree.getId()));
            startActivity(iFetchData);
        }
    }
}