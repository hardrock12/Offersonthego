package arjun.offersonthego;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

class featured_productts {

    public Bitmap image;
    public String product_name;
    public String mrp;
    public String price;
    public String shopid;
    public String pid;

}

public class MainActivity extends AppCompatActivity {
    public final static String SEARCH_TERM = "arjun.mainactivity.SEARCHTERM";
    public final static String SEARCH_CATEGORY = "arjun.mainactivity.SEARCHCATEGEORY";
    public final static String SEARCH_REGION = "arjun.mainactivity.SEARCHREGION";
    public Runnable r;
    public android.os.Handler mhandler;
    public Runnable r2;
    public android.os.Handler mhandler2;
    public featured_productts fproducts[];
    public MenuItem sign_in;
    public double CURRENT_LAT;
    public double CURRENT_LONG;
    int fno1 = -1, fno2 = -1;
    SharedPreferences sharedpreferences;
    // public Menu menu;
    Context context;
    boolean first_gps_data_Recieved = false;
    String msearch_category = "";
    String msearch_term = "";
    String mregion_term = "Nearby";
    EditText txt_search;
    EditText region_search;
    ListView lv;
    Button s;
    Dialog region_dialog;
    ArrayAdapter<String> adp;
    ImageView fimag1;//=(ImageView)findViewById(R.id.fimage1);
    ImageView fimag2;//=(ImageView)findViewById(R.id.fimage2);
    TextView txtname1;//=(TextView)findViewById(R.id.fname1);
    TextView txtname2;//=(TextView)findViewById(R.id.fname2);
    TextView txtprice1;//=(TextView)findViewById(R.id.fprice1);
    TextView txtprice2;//=(TextView)findViewById(R.id.fprice2);
    TextView txtmrp1;//=(TextView)findViewById(R.id.fmrp1);
    TextView txtmrp2;//=(TextView)findViewById(R.id.fmrp2);

    public void registerGPS() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the GPS location provider.
                CURRENT_LAT = location.getLatitude();
                CURRENT_LONG = location.getLongitude();
                if (first_gps_data_Recieved == false) {
                    first_gps_data_Recieved = true;
                    get_featured();


                }

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

    public void get_featured() {
        common_net_task region = new common_net_task(new common_net_task_Runnnable() {
            String response = "";
            String address;
            String urlfeatured;

            @Override
            public void init(String res) {
                this.response = res;
            }

            @Override
            public void run() {
                try {
                    JSONArray addresseslist = new JSONObject(response).getJSONArray("results");
                    //for (int i = 0; i < addresseslist.length(); i++) {


                    address = addresseslist.getJSONObject(0).getString("formatted_address");
                    String urlen = "";
                    Log.i("ootg", address);
                } catch (JSONException ex)

                {
                    ex.printStackTrace();
                }
                //urlen = "http://offersonthego.16mb.com/API/update_shop_location.php?" + URLEncoder.encode("sid=" + shop_Identifier + "&ln=" + CURRENT_LAT + "&lg=" + CURRENT_LONG + "&formataddr=" + address, "UTF-8");
                try {
                    urlfeatured = "http://offersonthego.16mb.com/API/retrieve_featured.php?region=" + URLEncoder.encode(address, "UTF-8");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                common_net_task n = new common_net_task(new common_net_task_Runnnable() {
                    String respo = "";


                    @Override
                    public void init(String r) {
                        respo = r;
                    }

                    @Override
                    public void run() {

                        try {
                            JSONArray ja = new JSONArray(respo);
                            fproducts = new featured_productts[ja.length()];
                            for (int k = 0; k < ja.length(); k++) {
                                fproducts[k] = new featured_productts();
                            }
                            for (int i = 0; i < ja.length(); i++) {
                                async_image_loading image_loading = new async_image_loading(new async_response_bitmap() {
                                    @Override
                                    public void Processbitmap(Bitmap b, int index) {
                                        fproducts[index].image = b;

                                    }
                                }, i, false);
                                JSONObject jo = ja.getJSONObject(i);
                                image_loading.execute(jo.getString("product_image"));
                                fproducts[i].product_name = jo.getString("product_name");
                                fproducts[i].price = jo.getString("price");
                                fproducts[i].mrp = jo.getString("MRP");
                                fproducts[i].shopid = jo.getString("shopid");
                                fproducts[i].pid = jo.getString("product_id");


                            }

                            mhandler.postDelayed(r, 300);
                            mhandler2.postDelayed(r2, 500);
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                    }
                }, urlfeatured);
                n.execute();
            }
        }, "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + CURRENT_LAT + "," + CURRENT_LONG + "&key=AIzaSyBLP5q5-qduRWzkP0Tqh4_Unlt2TTbuL0Y");

        region.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //getting featured images

        fimag1 = (ImageView) findViewById(R.id.fimage1);
        fimag2 = (ImageView) findViewById(R.id.fimage2);
        txtname1 = (TextView) findViewById(R.id.fname1);
        txtname2 = (TextView) findViewById(R.id.fname2);
        txtprice1 = (TextView) findViewById(R.id.fprice1);
        txtprice2 = (TextView) findViewById(R.id.fprice2);
        txtmrp1 = (TextView) findViewById(R.id.fmrp1);
        txtmrp2 = (TextView) findViewById(R.id.fmrp2);
// striking
// create runnable fearuted
        mhandler = new android.os.Handler();

        r = new Runnable() {
            @Override
            public void run() {
                Random rand = new Random();
                int h = fno1;
                h = rand.nextInt(fproducts.length);
                if (h == fno1) {
                    h = (h + 1) % fproducts.length;
                }
                fno1 = h;

                if (fproducts[fno1].image != null) {
                    fimag1.setImageBitmap(fproducts[fno1].image);
                    fimag1.setAlpha(1.0f);
                    Animation anim;
                    anim = AnimationUtils.loadAnimation(context, R.anim.image_fade);
                    fimag1.startAnimation(anim);
                    txtname1.setText(fproducts[fno1].product_name);
                    txtprice1.setText("Rs." + fproducts[fno1].price);
                    txtmrp1.setText("Rs." + fproducts[fno1].mrp);
                    txtmrp1.setPaintFlags(txtmrp1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    txtname1.startAnimation(anim);
                    txtprice1.startAnimation(anim);
                    txtmrp1.startAnimation(anim);
                }
                mhandler.postDelayed(r, 5000);
            }
        };

// create runnable fearuted2
        mhandler2 = new android.os.Handler();

        r2 = new Runnable() {
            @Override
            public void run() {
                Random rand = new Random();
                int h = fno2;
                h = rand.nextInt(fproducts.length);
                if (h == fno2) {
                    h = (h + 1) % fproducts.length;
                }
                fno2 = h;
                if (fproducts[fno2].image != null) {
                    fimag2.setImageBitmap(fproducts[fno2].image);
                    fimag2.setAlpha(1.0f);
                    Animation anim;
                    anim = AnimationUtils.loadAnimation(context, R.anim.image_fade);
                    fimag2.startAnimation(anim);
                    txtname2.setText(fproducts[fno2].product_name);
                    txtprice2.setText("Rs." + fproducts[fno2].price);
                    txtmrp2.setText("Rs." + fproducts[fno2].mrp);

                    txtname2.startAnimation(anim);
                    txtprice2.startAnimation(anim);
                    txtmrp2.startAnimation(anim);
                    txtmrp2.setPaintFlags(txtmrp2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                mhandler2.postDelayed(r2, 5000);
            }
        };


        //create onclick listner

        View.OnClickListener clickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fno1 == -1) {
                    return;
                }
                Intent inte = new Intent(context, product_Details.class);
                inte.putExtra(Search_Result.SEARCH_SHOP_ID, fproducts[fno1].shopid);
                inte.putExtra(Search_Result.SEARCH_PRODUCT_ID, fproducts[fno1].pid);
                startActivity(inte);

            }
        };

        fimag1.setOnClickListener(clickListener1);
        txtname1.setOnClickListener(clickListener1);
        txtprice1.setOnClickListener(clickListener1);
        txtmrp1.setOnClickListener(clickListener1);


        // for featured 2

        View.OnClickListener clickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fno1 == -1) {
                    return;
                }
                Intent inte = new Intent(context, product_Details.class);
                inte.putExtra(Search_Result.SEARCH_SHOP_ID, fproducts[fno2].shopid);
                inte.putExtra(Search_Result.SEARCH_PRODUCT_ID, fproducts[fno2].pid);
                startActivity(inte);

            }
        };

        fimag2.setOnClickListener(clickListener2);
        txtname2.setOnClickListener(clickListener2);
        txtprice2.setOnClickListener(clickListener2);
        txtmrp2.setOnClickListener(clickListener2);


        // menu = (Menu) findViewById(R.id.sigin_as_seller);
        sign_in = (MenuItem) findViewById(R.id.sigin_as_seller);
        txt_search = (EditText) findViewById(R.id.txt_search);


        //register gps
        registerGPS();

        //get featured products and display
        //    get_featured();

// setup spin
        Spinner spin = (Spinner) findViewById(R.id.category_list);
      /*  ArrayAdapter<CharSequence> adapter=new ArrayAdapter<CharSequence>(this,R.array.categoryofproducts,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
 */
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mregion_term = "All";
                        break;
                    case 1:

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
                                    Log.i("ootg", address);
                                    //  arrayList.add(address);

                                    //}
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mregion_term = address;
                            }

                        }, "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + CURRENT_LAT + "," + CURRENT_LONG + "&key=AIzaSyBLP5q5-qduRWzkP0Tqh4_Unlt2TTbuL0Y");
                        address_ret.execute();
                        break;
                    case 2:

                        region_dialog = new Dialog(context);
                        region_dialog.setCancelable(false);
                        region_dialog.setContentView(R.layout.region_select_dialog);
                        region_dialog.setTitle("Select Region:");
                        lv = (ListView) region_dialog.findViewById(R.id.lv_suggest);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView s = (TextView) view;
                                mregion_term = s.getText().toString();
                                region_dialog.dismiss();
                            }
                        });
                        adp = new ArrayAdapter<String>(context, R.layout.suggestion_listview_layout);
                        lv.setAdapter(adp);


                        region_search = (EditText) region_dialog.findViewById(R.id.dialog_region);

                        s = (Button) region_dialog.findViewById(R.id.btn_show_suggest);
                        s.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String str = region_search.getText().toString();
                                Log.i("ootg", str + " recieved");

                                common_net_task address_ret = new common_net_task(new common_net_task_Runnnable() {
                                    String response = "";

                                    public void init(String s) {
                                        response = s;

                                    }

                                    @Override
                                    public void run() {
                                        ArrayList<String> arrayList = new ArrayList<String>();
                               /* Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                try {
                                    List<Address> addressList = geocoder.getFromLocationName(str, 10);
                                    Log.i("ootg",addressList.toString());
                                    for (int i = 0; i < addressList.size(); i++) {
                                        String address = "";
                                        int len = addressList.get(i).getMaxAddressLineIndex();
                                        for (int j = 0; j < len; j++) {
                                            address = address + "," + addressList.get(i).getAddressLine(j);


                                        }
                                      address = address + addressList.get(i).getLocality();*/
                                        String address = "";
                                        Log.i("ootg", response);
                                        try {
                                            JSONArray addresseslist = new JSONObject(response).getJSONArray("results");
                                            for (int i = 0; i < addresseslist.length(); i++) {


                                                address = addresseslist.getJSONObject(i).getString("formatted_address");
                                                Log.i("ootg", address);
                                                arrayList.add(address);

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        adp.clear();
                                        adp.addAll(arrayList);
                                        adp.notifyDataSetChanged();
                                    }

                                }, "https://maps.googleapis.com/maps/api/geocode/json?address=" + str + "&key=AIzaSyBLP5q5-qduRWzkP0Tqh4_Unlt2TTbuL0Y");
                                address_ret.execute();


                            }
                        });

                        region_search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String str = s.toString();


                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                        region_dialog.show();
                        break;
                }

                //   Toast.makeText(context, tv.getText(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);

            }
        });
        //==================
// setup appbar
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);
        //setup_txtfield_change
        EditText search_term = (EditText) findViewById(R.id.txt_search);
        search_term.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                msearch_term = txt_search.getText().toString();
            }
        });
//setup button

        TextView search_txt_view = (TextView) findViewById(R.id.btn_Search);
        search_txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Search_Result.class);
                intent.putExtra(SEARCH_TERM, msearch_term);
                intent.putExtra(SEARCH_CATEGORY, msearch_category);
                intent.putExtra(SEARCH_REGION, mregion_term);

                sharedpreferences = getSharedPreferences("searchValue", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("search_word", msearch_term);
                editor.putString("search_category", msearch_category);
                editor.putString("search_region", mregion_term);

                editor.commit();


                startActivity(intent);


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


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
            startActivity(new Intent(context, SettingsActivity.class));
            return true;
        } else if (id == R.id.sigin_as_seller) {
            Intent intent = new Intent(context, login.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


}