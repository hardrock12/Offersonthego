package arjun.offersonthego;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class product_Details extends AppCompatActivity {
    public static JSONObject staticroot;
    public static double CURRENT_LAT;
    public static double CURRENT_LONG;
    Context mcontext;
    Dialog shop_Review;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Search_Result.stop_image_download=false;
        registerGPS();
        setContentView(R.layout.activity_product__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String shop_id = getIntent().getStringExtra(Search_Result.SEARCH_SHOP_ID);
        final String product_id = getIntent().getStringExtra(Search_Result.SEARCH_PRODUCT_ID);
        Button call_text = (Button) findViewById(R.id.shop_phno);
        mcontext = this;

        common_net_task product_details_fetch = new common_net_task(new common_net_task_Runnnable() {


            String response;

            @Override


            public void init(String response) {
                this.response = response;
            }

            @Override
            public void run() {
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
                } catch (JSONException ex)

                {
                    ex.printStackTrace();
                    return;
                }


                staticroot = root;
                TextView productname = (TextView) findViewById(R.id.product_name);
                TextView productcost = (TextView) findViewById(R.id.pcost);
                TextView sellingprice = (TextView) findViewById(R.id.selling_price);
                TextView yousave = (TextView) findViewById(R.id.you_save);
                TextView Product_rating = (TextView) findViewById(R.id.Product_Rating);
                TextView desc = (TextView) findViewById(R.id.descpriton);
                TextView avai = (TextView) findViewById(R.id.Availab);
                ListView randr = (ListView) findViewById(R.id.rating_and_review);
                RatingBar rate = (RatingBar) findViewById(R.id.ratingBar);

                final TextView shop_address = (TextView) findViewById(R.id.Shop_Address);
                Button navi = (Button) findViewById(R.id.navigate_to);
                Button write_Rev = (Button) findViewById(R.id.btn_write_a_review);




                navi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent d = new Intent(mcontext, Map_route.class);
                        d.putExtra("origin_lat", CURRENT_LAT);
                        d.putExtra("origin_lon", CURRENT_LONG);
                        try {
                            d.putExtra("dest_lat", Double.parseDouble(staticroot.getJSONObject("shop_location").getString("lattitude")));
                            d.putExtra("dest_lon", Double.parseDouble(staticroot.getJSONObject("shop_location").getString("longitude")));
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                            return;
                        }
                        startActivity(d);
                    }
                });


                write_Rev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shop_Review = new Dialog(mcontext);
                        shop_Review.setCancelable(true);
                        shop_Review.setContentView(R.layout.write_review_dialog);
                        shop_Review.setTitle("Reviews:");
                        Button sbt = (Button) shop_Review.findViewById(R.id.btn_submit_review_shop);
                        sbt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                String imei = telephonyManager.getDeviceId();
                                SharedPreferences sharedPreferences = getSharedPreferences("user_details", Context.MODE_PRIVATE);
                                String uname = (sharedPreferences.getString("name", "Anonymous"));
                                //String searchcat = (sharedPreferences.getString("search_category",""));
                                //String searchregion = (sharedPreferences.getString("search_region",""));

                                common_net_task m = new common_net_task(new common_net_task_Runnnable() {
                                    @Override
                                    public void init(String response) {

                                    }

                                    @Override
                                    public void run() {

                                    }
                                }, "http://www.google.com");
                                m.execute();
                                shop_Review.dismiss();
                            }
                        });


                        shop_Review.show();

                    }
                });

                Button call_text = (Button) findViewById(R.id.call_button);

                try {

                    productname.setText(root.getString("product_name"));
                    productcost.setText("MRP:Rs " + root.getString("MRP"));
                    sellingprice.setText("Selling Price:Rs " + root.getString("price"));
                    Product_rating.setText("Rating:" + root.getJSONObject("product_rating_details").getString("avg_product_rating"));
                    ArrayList<review_model> sl = new ArrayList<review_model>();
                    JSONArray shop_ratings = root.getJSONArray("shop_ratings");
                    for (int i = 0; i < shop_ratings.length(); i++) {
                        JSONObject each = shop_ratings.getJSONObject(i);
                        //sl.add(each.getString("customer_name") + ":" + each.getString("reviews") + "(" + each.getString("rating") + ")");
                        sl.add(new review_model(each.getString("customer_name"), each.getString("reviews"), each.getString("rating")));
                    }

                    randr.setAdapter(new review_listbox_adapter(mcontext, sl));


                    if (root.getString("availability").equals("Y")) {

                        avai.setText("STATUS:AVAILABLE");
                    } else {

                        avai.setText("STATUS:OUT OF STOCK");

                    }
                    rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
/*
                            try {
                                String pid=staticroot.getString("product_id");// "http://offersonthego.16mb.com/API/update_product_rating.php?pid=" + staticroot.getString("product_id") + "&sid=" + staticroot.getString("shopid") + "&rating=" + String.valueOf(rating);
                                String sid=staticroot.getString("shopid");// "http://offersonthego.16mb.com/API/update_product_rating.php?pid=" + staticroot.getString("product_id") + "&sid=" + staticroot.getString("shopid") + "&rating=" + String.valueOf(rating);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }*/
                            common_net_task ratingsender = new common_net_task(new common_net_task_Runnnable() {
                                @Override
                                public void init(String response) {

                                }

                                @Override
                                public void run() {

                                }
                            }, "http://offersonthego.16mb.com/API/update_product_rating.php?pid=" + product_id + "&sid=" + shop_id + "&rating=" + String.valueOf(rating));
                            ratingsender.execute();
                        }
                    });

                    call_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + staticroot.getJSONObject("shop_details").getString("ph_no")));

                                startActivity(in);
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getApplicationContext(), "yourActivity is not found", Toast.LENGTH_SHORT).show();
                            } catch (JSONException ex) {

                                ex.printStackTrace();
                            }

                        }
                    });


                    try {

                        yousave.setText("You save Rs" + String.valueOf(Double.valueOf(root.getString("MRP")) - Double.valueOf(root.getString("price"))));
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();


                    }
                    desc.setText(root.getString("description"));
                    shop_address.setText(root.getJSONObject("shop_details").getString("shop_name") + "\n" + root.getJSONObject("shop_details").getString("address") + "\n" + root.getJSONObject("shop_details").getString("region"));
                    call_text.setText("Call " + root.getJSONObject("shop_details").getString("ph_no"));
                    async_image_loading product_image_download = new async_image_loading(new async_response_bitmap() {
                        @Override
                        public void Processbitmap(Bitmap b, int i) {
                            ImageView img = (ImageView) findViewById(R.id.product_image);
                            img.setImageBitmap(b);
                        }
                    }, 0, false);
                    product_image_download.execute(root.getString("product_image"));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

            }
        }, "http://offersonthego.16mb.com/API/api.single_product.php?shopid=" + shop_id + "&pid=" + product_id);
        product_details_fetch.execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
