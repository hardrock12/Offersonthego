package arjun.offersonthego;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map_route extends FragmentActivity {
    GoogleMap map;
    ArrayList<LatLng> markerPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_route);
        // Initializing
        markerPoints = new ArrayList<LatLng>();

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
        map = fm.getMap();
        Intent fd = getIntent();
        String url = getDirectionsUrl(new LatLng(fd.getDoubleExtra("origin_lat", 0.0), fd.getDoubleExtra("origin_lon", 0.0)), new LatLng(fd.getDoubleExtra("dest_lat", 0.0), fd.getDoubleExtra("dest_lon", 0.0)));

        //DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        //downloadTask.execute(url);
        common_net_task mapapi = new common_net_task(new common_net_task_Runnnable() {
            String r;

            @Override
            public void init(String response) {
                r = response;
            }

            @Override
            public void run() {
                ParserTask parserTask = new ParserTask();
                parserTask.execute(r);
            }
        }, url);
        mapapi.execute();
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";


        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.i("ootg", "url:" + url);
        return url;
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                JsonPolylineparser parser = new JsonPolylineparser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.RED);
            }
            if (lineOptions == null) {
                return;
            }
            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
            LatLng coordinate = new LatLng(getIntent().getDoubleExtra("origin_lat", 0.0), getIntent().getDoubleExtra("origin_lon", 0.0));
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 10);
            map.animateCamera(yourLocation);
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(getIntent().getDoubleExtra("dest_lat", 0.0), getIntent().getDoubleExtra("dest_lon", 0.0)))
                    .title("Shop"));
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(getIntent().getDoubleExtra("origin_lat", 0.0), getIntent().getDoubleExtra("origin_lon", 0.0)))
                    .title("You are here"));


        }
    }
}

