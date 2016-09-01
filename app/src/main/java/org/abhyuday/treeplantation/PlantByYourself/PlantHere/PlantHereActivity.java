package org.abhyuday.treeplantation.PlantByYourself.PlantHere;

/**
 * Launched when "Plant here" button is clicked on UserProfile activity in PlantByYourself.
 * Associated configuration file - PlantHereConfig.java
 * Utilities for directory creation - AlbumStorageDirFactory.java and FroyoAlbumDirFactory
 * Layout files- activity_photo_intent
 *
 * Function - Click photos and upload to the server along with the timestamp,location and name specified in a textbox.
 *
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.abhyuday.treeplantation.R;
import org.abhyuday.treeplantation.UserProfile;
import org.abhyuday.treeplantation.loginmodule.LoginConfig;

public class PlantHereActivity extends AppCompatActivity  {

    private static final int ACTION_TAKE_PHOTO_B = 1;
    private String mCurrentPhotoPath;
    private EditText etSpecies;
    private EditText etComments;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    public String currStorageDir = "";
    public File storageDir=null;
    public int userId = 0;
    public Location currLocation;
    private Button btnUploadData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_intent);

        Button picBtn = (Button) findViewById(R.id.btnIntend);
        btnUploadData = (Button) findViewById(R.id.btnUploadData);
        etSpecies = (EditText) findViewById(R.id.etSpecies);
        etComments = (EditText) findViewById(R.id.etComments);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(LoginConfig.USER_ID_SHARED_PREF,0);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        currStorageDir = Integer.toString(userId) + "_" + timeStamp ;

        setBtnListenerOrDisable(
                picBtn,
                mTakePicOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                currLocation = location;
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) !=
                PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION },1);
        }
        currLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Toast.makeText(PlantHereActivity.this, Double.toString(currLocation.getLatitude()) + "\n" + Double.toString(currLocation.getLongitude()), Toast.LENGTH_LONG).show();

        mAlbumStorageDirFactory = new FroyoAlbumDirFactory();

        btnUploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storageDir!=null && storageDir.listFiles().length!=0){
                    String comment = etComments.getText().toString();
                    String species = etSpecies.getText().toString();
                    uploadImages(species,comment,storageDir);
                }else{
                    Toast.makeText(PlantHereActivity.this,"Please Click some photos of your plant",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void uploadImages(final String species, final String comment, File storageDir) {
        final File[] allFiles = storageDir.listFiles();
        final int numberOfPics = allFiles.length;

        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PlantHereConfig.UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        Toast.makeText(PlantHereActivity.this,"Successfully uploaded",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(PlantHereActivity.this, UserProfile.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Log.i("myTag","volleyError");
                        Toast.makeText(PlantHereActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<String, String>();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                for(int i = 0; i < allFiles.length-1; i++ ){
                    Uri imageUri = Uri.fromFile(allFiles[i]);
                    try{
                        Log.i("myTag", "trying to add bitmap-> image->params");
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        String image = getStringImage(bitmap);
                        params.put("Image"+Integer.toString(i).trim(), image);
                    }catch (Exception e){
                        Log.e("myTag","bitmapError,PhotoIntentActicity: " + e.getMessage());
                    }
                }
                    params.put(PlantHereConfig.USER_ID,Integer.toString(userId).trim());
                    params.put(PlantHereConfig.NUMBER_PIC,Integer.toString(numberOfPics).trim());
                    params.put(PlantHereConfig.SPECIES,species);
                    params.put(PlantHereConfig.COMMENT,comment);
                    params.put(PlantHereConfig.TIMESTAMP,timeStamp);
                    params.put(PlantHereConfig.LATITUDE,Double.toString(currLocation.getLatitude()));
                    params.put(PlantHereConfig.LONGITUDE,Double.toString(currLocation.getLongitude()));
                    params.put(PlantHereConfig.ACTION,Integer.toString(1));
                    params.put(PlantHereConfig.KEY_DIRECTORY, PlantHereConfig.DIRECTORY_URL);

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
            //setPic();
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
                    "cannot do");
            btn.setClickable(false);
        }
    }



}