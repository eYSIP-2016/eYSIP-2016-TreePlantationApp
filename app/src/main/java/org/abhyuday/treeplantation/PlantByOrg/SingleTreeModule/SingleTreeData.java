package org.abhyuday.treeplantation.PlantByOrg.SingleTreeModule;

/**
 * Launched when a plant is selected from the listview in PlantList.
 * Associated config file - TreeListDataConfig.java
 * Layout files- activity_single_tree_data
 *
 * Function -   Display the data of a particular tree.
 *              On clicking "plant this tree" button, PlantHereMap activity of PlantingTreeModule package is opened.
 *
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.abhyuday.treeplantation.PlantByOrg.PlantingTreeModule.PlantHereMap;
import org.abhyuday.treeplantation.R;
import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

public class SingleTreeData extends AppCompatActivity {


    TextView textViewTreeData;
    ProgressDialog loading;
    Intent intent;
    String treeId;
    String treeImageUrl;
    Button btnPlantThisTree;
    ImageView imageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_tree_data);

        textViewTreeData = (TextView) findViewById(R.id.textViewTreeData);
        btnPlantThisTree = (Button) findViewById(R.id.btnPlantThisTree);
        imageView2= (ImageView) findViewById(R.id.imageView2);

        intent = getIntent();
        treeId = intent.getStringExtra(TreeListDataConfig.TREE_ID_INTENT);

        getTreeData(treeId);

        btnPlantThisTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleTreeData.this, PlantHereMap.class);
                intent.putExtra(TreeListDataConfig.TREE_ID_INTENT, treeId);
                startActivity(intent);
            }
        });
    }


    //method used to get the data of tree using volley request as a string response
    private void getTreeData(String id) {


        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);
        String url = TreeListDataConfig.DATA_URL+id.trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SingleTreeData.this,"StringRequest Error",Toast.LENGTH_LONG).show();
                    }});

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //method used to convert string response to JSON format and displaying in the textview
    private void showJSON(String response){
        String name="";
        String source = "";
        String seedSapling = "";
        String genus = "";
        String imageurl = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(TreeListDataConfig.JSON_ARRAY);
            JSONObject TreeData = result.getJSONObject(0);
            name = TreeData.getString(TreeListDataConfig.KEY_NAME);
            source = TreeData.getString(TreeListDataConfig.KEY_SOURCE);
            seedSapling = TreeData.getString(TreeListDataConfig.KEY_SEEDSAPLING);
            genus = TreeData.getString(TreeListDataConfig.KEY_GENUS);
            imageurl = TreeData.getString(TreeListDataConfig.KEY_IMAGEURL);
            treeImageUrl = imageurl;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        textViewTreeData.setText("Name:\t"+name+
                "\nsource:\t"+ source+
                "\nseedsapling:\t"+ seedSapling+
                "\ngenus:\t"+ genus+
                "\nimageurl:" + imageurl
                );

        Picasso.with(SingleTreeData.this).load(imageurl).into(imageView2);
    }

}
