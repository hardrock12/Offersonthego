package arjun.offersonthego;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class seller_shop_location extends AppCompatActivity {
    public static double CURRENT_LAT = -1;
    public static double CURRENT_LONG = -1;
    public Button b;
    public Context mcontext;
    public String shop_Identifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_shop_location);
        registerGPS();
        mcontext = this;
        b = (Button) findViewById(R.id.location_seller);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CURRENT_LAT == -1 || CURRENT_LONG == -1) {

                    Toast.makeText(mcontext, "Unable to update location", Toast.LENGTH_LONG).show();

                    return;
                }
                SharedPreferences shopIdentifier = getSharedPreferences("shopData", Context.MODE_PRIVATE);
                shop_Identifier = (shopIdentifier.getString("shopIdentifier", ""));

                common_net_task address_ret = new common_net_task(new common_net_task_Runnnable() {
                    String response = "";

                    public void init(String s) {
                        response = s;

                    }

                    @Override
                    public void run() {
                        String address = "";
                        Log.i("ootg", response);
                        try {
                            JSONArray addresseslist = new JSONObject(response).getJSONArray("results");
                            //for (int i = 0; i < addresseslist.length(); i++) {


                            address = addresseslist.getJSONObject(0).getString("formatted_address");
                            String urlen = "";
                            Log.i("ootg", address);

                            //urlen = "http://offersonthego.16mb.com/API/update_shop_location.php?" + URLEncoder.encode("sid=" + shop_Identifier + "&ln=" + CURRENT_LAT + "&lg=" + CURRENT_LONG + "&formataddr=" + address, "UTF-8");
                            try {
                                urlen = "http://offersonthego.16mb.com/API/update_shop_location.php?ln=" + CURRENT_LAT + "&lg=" + CURRENT_LONG + "&formataddr=" + URLEncoder.encode(address, "UTF-8") + "&sid=" + shop_Identifier;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            Log.i("ootg", urlen);

                            common_net_task net_task = new common_net_task(new common_net_task_Runnnable() {
                                @Override
                                public void init(String response) {
                                    Log.i("ootg", response);
                                }

                                @Override
                                public void run() {
                                    Toast.makeText(mcontext, "Location And Address Updated", Toast.LENGTH_LONG).show();
                                }
                            }, urlen);

                            net_task.execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + CURRENT_LAT + "," + CURRENT_LONG + "&key=AIzaSyBLP5q5-qduRWzkP0Tqh4_Unlt2TTbuL0Y");
                address_ret.execute();

            }

        });
    }

    public void registerGPS() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the GPS location provider.
                CURRENT_LAT = location.getLatitude();
                CURRENT_LONG = location.getLongitude();


            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        //  if(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 5, locationListener);
    }
}
