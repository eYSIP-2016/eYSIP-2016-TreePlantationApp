package org.abhyuday.treeplantation;

/**
 * This is the landing page after a user is successfully logged in. It has options to go to the two different parts of the project.
 * UserProfile activity has a navigation drawer and the following layout xml files -
 * activity_user_profile
 * content_user_profile
 * nav_header_user_profile  (navbar)
 * user_profile_appbar_menu (menu)
 *
 *
 *
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.abhyuday.treeplantation.PlantByOrg.MyTreesModule.MyTreeList;
import org.abhyuday.treeplantation.PlantByOrg.MyTreesModule.SpotAllYourTrees;
import org.abhyuday.treeplantation.PlantByYourself.GalleryModule.MyGallery;
import org.abhyuday.treeplantation.PlantByYourself.PlantHere.PlantHereActivity;
import org.abhyuday.treeplantation.PlantByYourself.SpotAllOnMapModule.SpotYourTrees;
import org.abhyuday.treeplantation.PlantByYourself.UploadNearestModule.UploadNearestTree;
import org.abhyuday.treeplantation.loginmodule.LoginConfig;
import org.abhyuday.treeplantation.PlantByOrg.TreeListModule.*;

public class UserProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button listbutton;
    Button myTreesButton;
    Button spotAllBtn;

    Button plantHereButton;
    Button adoptTreeButton;
    Button nearestTreeButton;
    Button galleryButton;
    Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listbutton = (Button) findViewById(R.id.listbutton);
        myTreesButton = (Button) findViewById(R.id.myTreesButton);
        spotAllBtn = (Button) findViewById(R.id.spotAllBtn);
        plantHereButton = (Button) findViewById(R.id.plantHereButton);
        adoptTreeButton = (Button) findViewById(R.id.adoptTreeButton);
        nearestTreeButton = (Button) findViewById(R.id.nearestTreeButton);
        galleryButton = (Button) findViewById(R.id.galleryButton);
        mapButton = (Button) findViewById(R.id.mapButton);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(LoginConfig.EMAIL_SHARED_PREF,"Not Available");
        int userId = sharedPreferences.getInt(LoginConfig.USER_ID_SHARED_PREF,0);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/LemonMilk.otf");
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setTypeface(type);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(type);



        //displaying username and id in navigation bar
        View header = navigationView.getHeaderView(0);
        TextView textId = (TextView) header.findViewById(R.id.navId);
        TextView textUsername = (TextView) header.findViewById(R.id.navUserName);
        textUsername.setText(email );
        textId.setText(Integer.toString(userId));


        listbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfile.this, PlantList.class);
                startActivity(i);
            }
        });

        myTreesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfile.this, MyTreeList.class);
                startActivity(i);
            }
        });

        spotAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfile.this, SpotAllYourTrees.class);
                startActivity(i);
            }
        });

        plantHereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfile.this, PlantHereActivity.class);
                startActivity(i);
            }
        });

        adoptTreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        nearestTreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfile.this, UploadNearestTree.class);
                startActivity(i);
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfile.this, MyGallery.class);
                startActivity(i);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfile.this, SpotYourTrees.class);
            }
        });
    }


    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        SharedPreferences preferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(LoginConfig.LOGGEDIN_SHARED_PREF, false);
                        editor.putString(LoginConfig.EMAIL_SHARED_PREF, "");
                        editor.putInt(LoginConfig.USER_ID_SHARED_PREF,0);
                        editor.apply();
                        Intent intent = new Intent(UserProfile.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_profile_appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(UserProfile.this,"settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_logout) {
            //calling logout method when the logout button is clicked
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
