package arjun.offersonthego;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Search_Result extends AppCompatActivity {
    Context context;
    public static String SEARCH_PHP_SCRIPT = "http://offersonthego.16mb.com/API/api.products.php?";
    public static String GOOGLE_DIRECTION_MATRIX = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    public static String SEARCH_TERM = "";
    public static String SEARCH_CATEGORY = "";
    public static boolean response_Ready = false;
    public ProgressDialog mprogressDialoggps;
    public ArrayList<Search_Results_Model> response_result_model_to_adapters;
    public searchtask tasks;
    public static ArrayList<Search_Results_Model> stored_search_results;
    public ArrayList<Search_Results_Model> arraylist;
    public double CURRENT_LAT;
    public double CURRENT_LONG;

    public void update_Locations() {


        //building url
        String url;
        url = GOOGLE_DIRECTION_MATRIX + "origins=" + CURRENT_LAT + "," + CURRENT_LONG + "&destinations=";
        for (int i = 0; i < stored_search_results.size(); i++) {
            if (i != 0) {
                url = url + "|";

            }

            url = url + stored_search_results.get(i).shop_lat + "," + stored_search_results.get(i).shop_lngi;


        }
        url = url + "&key=AIzaSyBRJy24wykvkl_uwBVen2uLdvBfbx97gLI";
        Log.i("ootg", url);

        common_net_task common_net_task = new common_net_task(new common_net_task_Runnnable() {

            String response;

            @Override
            public void run() {
                ListView lv_serach_Res = (ListView) findViewById(R.id.lv_search_results);
                Search_ItemsAdapter adap = (Search_ItemsAdapter) lv_serach_Res.getAdapter();

                //  stored_search_results --> response_Result_model_to_adapters-->arraylist(adapter)-->adapter
//copy of task_stored response_response
                response_result_model_to_adapters = stored_search_results;

                try {
                    JSONArray distance_elements = new JSONObject(response).getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
                    for (int i = 0; i < stored_search_results.size(); i++) {
                        Search_Results_Model s = response_result_model_to_adapters.get(i);
                        JSONObject jobj = distance_elements.getJSONObject(i);
                        Log.i("ootg", jobj.toString());
                        if (jobj.getString("status") != "ZERO_RESULTS" && jobj.has("distance")) {
                            s.Distance = jobj.getJSONObject("distance").getString("text");
                            s.valid_distance=true;
                            // s.Distance = jobj.getString("distance");
s.distanceinm=jobj.getJSONObject("distance").getDouble("value");
                        } else {
                            s.valid_distance=true;
                            s.Distance = "";
                            s.distanceinm=9999999;
                        }

                        response_result_model_to_adapters.set(i, s);

                    }
                    arraylist = response_result_model_to_adapters;
                    adap.clear();

                    adap.addAll(arraylist);
                    adap.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();


                }

            }

            @Override
            public void init(String str) {
                response = str;
                Log.i("ootg", str);
            }
        }, url);
        common_net_task.execute();


    }

    public void registerGPS() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the GPS location provider.
                CURRENT_LAT = location.getLatitude();
                CURRENT_LONG = location.getLongitude();


                if (!Search_Result.response_Ready) {
                    return;
                }
                update_Locations();

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 5, locationListener);
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
        arraylist = new ArrayList<Search_Results_Model>();
        Search_ItemsAdapter search_itemsAdapter = new Search_ItemsAdapter(this, arraylist);
        ListView lvsresults = (ListView) findViewById(R.id.lv_search_results);
        lvsresults.setAdapter(search_itemsAdapter);
// setting on click listener for Listview
        lvsresults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });


        //new thread for async network task
        tasks = new searchtask(findViewById(android.R.id.content), context, new Runnable() {
            @Override
            public void run() {
                update_Locations();
            }
        });
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
                Filter_list_dialog filter_list_dialog = new Filter_list_dialog(context, findViewById(android.R.id.content), new Runnable() {
                    @Override
                    public void run() {
                        update_Locations();
                    }
                });
                filter_list_dialog.show(getFragmentManager(), "FILTER");

        }

        return super.onOptionsItemSelected(item);
    }

}

