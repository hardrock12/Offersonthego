package arjun.offersonthego;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Search_Result extends AppCompatActivity {
    Context context;
    public static String SEARCH_PHP_SCRIPT = "http://offersonthego.16mb.com/API/api.products.php?";
    public static String SEARCH_TERM = "";
    public static String SEARCH_CATEGORY = "";
    public static String CURRENT_LAT = "";
    public static String CURRENT_LONG = "";
    public ProgressDialog mprogressDialoggps;

    public void registerGPS() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Toast.makeText(context, String.valueOf(location.getLatitude()), Toast.LENGTH_LONG).show();

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private double[] getGPS() {

// Acquire a reference to the system Location Manager


// Define a lis// Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates


// Register the listener with the Location Manager to receive location updates
        double gps[] = {-1, -1};


        return gps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerGPS();
        String searchterm = intent.getStringExtra(MainActivity.SEARCH_TERM);
        String searchcat = intent.getStringExtra(MainActivity.SEARCH_CATEGORY);
        SEARCH_TERM = searchterm;
        SEARCH_CATEGORY = searchcat;
// connecting to listview by adapter
        ArrayList<Search_Results_Model> arraylist = new ArrayList<Search_Results_Model>();
        Search_ItemsAdapter search_itemsAdapter = new Search_ItemsAdapter(this, arraylist);
        ListView lvsresults = (ListView) findViewById(R.id.lv_search_results);
        lvsresults.setAdapter(search_itemsAdapter);
// setting on click listener for Listview
        lvsresults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
        //getting gps

        mprogressDialoggps = ProgressDialog.show(context, "Please Wait", "Locating the device");

// retrieving gps
        double locs[] = getGPS();
        if (locs[0] != -1) {
            Search_Result.CURRENT_LAT = String.valueOf(locs[0]);
            Search_Result.CURRENT_LONG = String.valueOf(locs[1]);
        } else {
            Search_Result.CURRENT_LAT = "-1";
            Search_Result.CURRENT_LONG = "-1";
        }

        //Thread.sleep(2000);
        mprogressDialoggps.dismiss();
        //new thread for async network task
        searchtask tasks = new searchtask(findViewById(android.R.id.content), context);
        tasks.execute("http://offersonthego.16mb.com/API/api.products.php?searchterm=" + searchterm + "&searchcategory=" + searchcat + "&lat=" + CURRENT_LAT + "&long=" + CURRENT_LONG);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.filter_icon:
                Filter_list_dialog filter_list_dialog = new Filter_list_dialog(context, findViewById(android.R.id.content));
                filter_list_dialog.show(getFragmentManager(), "FILTER");

        }

        return super.onOptionsItemSelected(item);
    }

}

