package org.abhyuday.treeplantation.PlantByYourself.UploadNearestModule;

/**
 * Launched when "Upload" button is clicked on UserProfile activity in PlantByYourself.
 * Associated configuration file - UploadNearestConfig.java
 * Layout files- activity_nearest_tree
 *
 * Function -   Detect the nearby trees and display in a list.
 *              Click photos of the selected tree and upload to the server along with the timestamp.
 *
 */


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import org.abhyuday.treeplantation.PlantByYourself.PlantHere.AlbumStorageDirFactory;
import org.abhyuday.treeplantation.PlantByYourself.PlantHere.FroyoAlbumDirFactory;
import org.abhyuday.treeplantation.R;
import org.abhyuday.treeplantation.UserProfile;
import org.abhyuday.treeplantation.loginmodule.LoginConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UploadNearestTree extends AppCompatActivity {

    private static final int ACTION_TAKE_PHOTO_B = 1;
    private String mCurrentPhotoPath;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private Location currLocation;
    public String currStorageDir = "";
    public File storageDir=null;
    public int userId = 0;
    /*private String NEAREST_TREE_URL ="http://10.0.2.2/androidPHP/getNearestTrees.php";
    private String UPLOAD_URL ="http://10.0.2.2/androidPHP/uploadYourTreeImages.php";*/

    private Button btnUploadData;
    private ListView listView;
    private String nearestTreeIdOnClick="-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_tree);
        Button picBtn = (Button) findViewById(R.id.btnIntend);
        btnUploadData = (Button) findViewById(R.id.btnUploadData);
        listView = (ListView) findViewById(R.id.listView);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(LoginConfig.USER_ID_SHARED_PREF,0);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        currStorageDir = Integer.toString(userId) + "_" + timeStamp ;


        setBtnListenerOrDisable(
                picBtn,
                mTakePicOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) !=
                PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION },1);
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                currLocation = location;
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };
        currLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        getTreesNearby();

        mAlbumStorageDirFactory = new FroyoAlbumDirFactory();


        btnUploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storageDir!=null && storageDir.listFiles().length!=0){
                        uploadImages(storageDir);
                }else{
                    Toast.makeText(UploadNearestTree.this,"Please Click some photos of your plant",Toast.LENGTH_LONG).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*if(storageDir!=null && storageDir.listFiles().length!=0){
                    nearestTreeIdOnClick = (String) parent.getItemAtPosition(position);
                    Toast.makeText(NearestTree.this,"Selected tree Id"+ nearestTreeIdOnClick, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(NearestTree.this,"Please Click next to upload photos of plant" + nearestTreeIdOnClick, Toast.LENGTH_LONG).show();
                }*/
                nearestTreeIdOnClick = (String) parent.getItemAtPosition(position);


            }
        });
    }

    private void uploadImages( File storageDir) {
        final File[] allFiles = storageDir.listFiles();
        final int numberOfPics = allFiles.length; //One extra null pic is added unnecessarily

        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UploadNearestConfig.UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        Toast.makeText(UploadNearestTree.this,"Successfully uploaded",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UploadNearestTree.this, UserProfile.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Log.i("myTag","volleyError");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                for(int i = 0; i < allFiles.length-1; i++ ){
                    Uri imageUri = Uri.fromFile(allFiles[i]);
                    try{
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        String image = getStringImage(bitmap);
                        params.put("Image"+Integer.toString(i).trim(), image);
                    }catch (Exception e){
                        Log.e("myTag","bitmapError,PhotoIntentActicity: " + e.getMessage());
                    }
                }

                params.put(UploadNearestConfig.USER_ID,Integer.toString(userId).trim());
                params.put(UploadNearestConfig.NUMBER_PIC,Integer.toString(numberOfPics).trim());
                params.put(UploadNearestConfig.TIMESTAMP,timeStamp);
                params.put(UploadNearestConfig.LATITUDE,Double.toString(currLocation.getLatitude()));
                params.put(UploadNearestConfig.LONGITUDE,Double.toString(currLocation.getLongitude()));
                params.put(UploadNearestConfig.ACTION,Integer.toString(2));
                params.put(UploadNearestConfig.NEAREST_TREE_ID,nearestTreeIdOnClick);
                params.put(UploadNearestConfig.KEY_DIRECTORY, UploadNearestConfig.DIRECTORY_URL);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private File getAlbumDir() {
        storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(currStorageDir);

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("myTag", "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private File setUpPhotoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File f = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(actionCode == ACTION_TAKE_PHOTO_B){
            File f = null;

            try {
                f = setUpPhotoFile();
                mCurrentPhotoPath = f.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            } catch (IOException e) {
                e.printStackTrace();
                f = null;
                mCurrentPhotoPath = null;
            }
        }
        startActivityForResult(takePictureIntent, actionCode);
    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            galleryAddPic();
            mCurrentPhotoPath = null;
        }

    }

    Button.OnClickListener mTakePicOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
                }
            };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==ACTION_TAKE_PHOTO_B){
            if (resultCode == RESULT_OK) {
                handleBigCameraPhoto();
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
            }
        }
    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
     *
     * @param context The application's environment.
     * @param action The Intent action to check for availability.
     *
     * @return True if an Intent with the specified action can be sent and
     *         responded to, false otherwise.
     */

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    "cannot access camera");
            btn.setClickable(false);
        }
    }


    private void getTreesNearby() {
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UploadNearestConfig.NEAREST_TREE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        int nTrees= fillTheNearTreeList(s);
                        Toast.makeText(UploadNearestTree.this,"Number of trees near you: "+ nTrees,Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Log.i("myTag","volleyError");
                        Toast.makeText(UploadNearestTree.this,"some error in retrieving data",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<String, String>();
                params.put("Latitude",Double.toString(currLocation.getLatitude()));
                params.put("Longitude",Double.toString(currLocation.getLongitude()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private int fillTheNearTreeList(String response) {
        JSONObject jsonObject = null;
        List<String> list = new ArrayList<>();
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                int id = jo.getInt("MyPlantedTrees_Id");

                list.add(Integer.toString(id));
            }
            ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
            listView.setAdapter(listAdapter);

            return result.length();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("myTag", "exceptionJSON");
            return 0;
        }
    }
}
