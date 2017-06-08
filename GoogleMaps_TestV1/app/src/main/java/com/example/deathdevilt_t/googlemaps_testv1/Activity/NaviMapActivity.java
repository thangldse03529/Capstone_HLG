package com.example.deathdevilt_t.googlemaps_testv1.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.deathdevilt_t.googlemaps_testv1.ObjectClass.MarkerObject;
import com.example.deathdevilt_t.googlemaps_testv1.R;
import com.example.deathdevilt_t.googlemaps_testv1.Retrofit.RetrofitNode.Communicator_Node;
import com.example.deathdevilt_t.googlemaps_testv1.SqliteDatabase.UserInformation.DBHelper;
import com.example.deathdevilt_t.googlemaps_testv1.fragment.FragmentDialogHistory;
import com.example.deathdevilt_t.googlemaps_testv1.fragment.FragmentDialogInformation;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NaviMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SupportMapFragment supportMapFragment;
    private GoogleMap mMap;
    private LatLng near1, near2, specialPoint, nearTest;
    private ArrayList<MarkerObject> markerName;
    private MarkerObject markerObject, markerObject1;
    private PopupWindow popupWindow;
    private FrameLayout mFrameLayout;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    TranslateAnimation animation1;
    TranslateAnimation animation2;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    public static String email;
    private Context context;
    private Bitmap bitmap;
    private String UriImage;
    private Communicator_Node communicator;
    private DBHelper dbHelper;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //usePost("1");

        init();



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void init() {
        dbHelper = new DBHelper(context);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.frame_layout, new MapFragment());
        transaction.commit();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView) hView.findViewById(R.id.imageView_Icon);
        ImageView ImgView_Weather = (ImageView) hView.findViewById(R.id.ImgView_Weather);
        TextView tView_Name = (TextView) hView.findViewById(R.id.tView_Name);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            UriImage = bundle.getString("UriImage");
            String Name = bundle.getString("Name");
            String EmailAddress = bundle.getString("EmailAddress");
            TextView tView_emailAddress = (TextView) hView.findViewById(R.id.tView_emailAddress);
            Picasso.with(this).load(UriImage).placeholder(R.drawable.icon_avatar_default).resize(150, 150).error(R.drawable.icon_avatar_default).into(imageView);
            tView_Name.setText(Name);
            tView_emailAddress.setText(EmailAddress);

            Bitmap imageAva=((BitmapDrawable)imageView.getDrawable()).getBitmap();
            imageDownload(this,UriImage);
            try {

                Log.d("fuck",Name);
                Log.d("fuck",EmailAddress);
                Log.d("fuck1234",UriImage);
             //   dbHelper.insertContact(Name,EmailAddress,UriImage);

            }catch (Exception e){
                e.printStackTrace();
            }



        } else if (bundle == null) {

        }


    }
    public static void imageDownload(Context ctx, String url){
        Log.d("fuck1234",url);
        Picasso.with(ctx)
                .load("http://blog.concretesolutions.com.br/wp-content/uploads/2015/04/Android1.png")
                .into(getTarget(url));
    }

    //target to save
    private static Target getTarget(final String url){
        Log.d("fuck1234",url);
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + url);
                        try {
                            Log.d("fuckImg",bitmap.toString());
                            Log.d("fuckImg",Environment.getExternalStorageDirectory().getAbsolutePath());
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
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
        getMenuInflater().inflate(R.menu.navi_map, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            // Handle the camera action
            showHistoryDialog();
        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_infor) {
            showInformationDialog();
        } else if (id == R.id.nav_history) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showInformationDialog() {

        new FragmentDialogInformation().show(getFragmentManager(), "Fagment information");

    }

    private void showHistoryDialog() {

        new FragmentDialogHistory().show(getFragmentManager(), "Fragment history");

    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("NaviMap Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    // Send post request



}
