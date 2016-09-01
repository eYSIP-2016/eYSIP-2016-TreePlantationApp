package org.abhyuday.treeplantation.PlantByOrg.MyTreesModule;

/**
 * Launched when "My trees" button is clicked on UserProfile activity
 * Layout file - activity_my_tree_list
 * Config file - MyTreeListConfig
 * Adapter file - CustomAdapterMyTreeList
 *
 *
 * Function -   Display the list of all trees that were planted by the user in PlantByOrg section
 *              On selecting a tree MySingleTree activity of MySingleTreeModule Package is opened.
 *
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.abhyuday.treeplantation.PlantByOrg.MySingleTreeModule.MySingleTree;
import org.abhyuday.treeplantation.PlantByOrg.MySingleTreeModule.MySingleTreeConfig;
import org.abhyuday.treeplantation.R;
import org.abhyuday.treeplantation.loginmodule.LoginConfig;
import org.abhyuday.treeplantation.PlantByOrg.models.PlantedTreeModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyTreeList extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public int userId;
    private ArrayList<PlantedTreeModel> list = new ArrayList<>();
    private CustomAdapterMyTreeList adapterMyTreeList;
    private ListView myPlantListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tree_list);


        list = new ArrayList<>();
        adapterMyTreeList = new CustomAdapterMyTreeList(this, list);
        myPlantListView = (ListView) findViewById(R.id.yourTreesListView);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(LoginConfig.USER_ID_SHARED_PREF,0);

        myPlantListView.setOnItemClickListener(this);



    }

    //JSON converted to list and passed to adapterTreeList
    private void parseResponse(String response){
        JSONObject jsonObject = null;


        try {
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
                list.add(newPlantedTree);
            }

            myPlantListView.setAdapter(adapterMyTreeList);
//            adapterMyTreeList.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("myTag","exceptionJSON");
        }



    }

    //volley request for a string response.. which in turn calls parseResponse to convert to JSON.
    private void getResponse(String url){
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
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
    protected void onResume() {
        super.onResume();
        String url = MyTreeListConfig.URL_MY_TREES + Integer.toString(userId);
        getResponse(url);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId()==R.id.yourTreesListView){
            PlantedTreeModel myTree = (PlantedTreeModel) parent.getItemAtPosition(position);
            Intent iFetchData = new Intent(MyTreeList.this, MySingleTree.class);
            iFetchData.putExtra(MySingleTreeConfig.PLANTED_TREE_ID_INTENT, Integer.toString(myTree.getPT_id()));
            iFetchData.putExtra(MySingleTreeConfig.TREE_ID_INTENT, Integer.toString(myTree.getPT_TreeId()));
            startActivity(iFetchData);
        }
    }
}
