package arjun.offersonthego;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;

import android.widget.ListView;

import android.location.*;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class Search_Results_Model {
    public String product_name;
    public String category;
    public String description;
    public String price;
    public String Avail;
    public String shopid;
    public String productid;
    public String distance;

    public static Search_Results_Model fromJson(JSONObject jsonObject) {
        Search_Results_Model model = new Search_Results_Model();
        try {
            model.product_name = jsonObject.getString("Product_name");
            model.category = jsonObject.getString("catgory");
            model.productid = jsonObject.getString("product_id");
            model.shopid = jsonObject.getString("shopid");
            model.description = jsonObject.getString("description");
            model.price = jsonObject.getString("price");
            model.Avail = jsonObject.getString("availability");
            model.distance = jsonObject.getString("distance");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return model;
    }

    public static ArrayList<Search_Results_Model> fromJson(JSONArray jsonArray) {
        ArrayList<Search_Results_Model> models = new ArrayList<Search_Results_Model>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json_of_one_model = null;

            try {
                json_of_one_model = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Search_Results_Model model = Search_Results_Model.fromJson(json_of_one_model);
            models.add(model);


        }


        return models;

    }

}


public class Search_Result extends AppCompatActivity {
    Context context;
    public static String SEARCH_PHP_SCRIPT = "http://offersonthego.16mb.com/API/api.products.php?";
    public static String SEARCH_TERM = "";
    public static String SEARCH_CATEGORY = "";
    public static String CURRENT_LAT = "";
    public static String CURRENT_LONG = "";
    public ProgressDialog mprogressDialoggps;

    private double[] getGPS() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

/* Loop over the array backwards, and if you get an accurate location, then break                 out the loop*/
        Location l = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        } else {
            gps[0] = gps[1] = -1;


        }
        Log.i("ootg", String.valueOf(gps[0]) + String.valueOf(gps[1]));
        Toast.makeText(this, String.valueOf(gps[0] + String.valueOf(gps[1])), Toast.LENGTH_SHORT).show();
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

class searchtask extends AsyncTask<String, Void, String> {
    private View rv;

    private Context mcontext;

    searchtask(View rootview, Context context) {
        this.rv = rootview;
        this.mcontext = context;
    }


    @Override
    protected void onPostExecute(String response) {
        JSONArray jsonArray = null;
        Log.i("ootg", "json response:" + response);
        try {
            jsonArray = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ListView lvsearch = (ListView) rv.findViewById(R.id.lv_search_results);
        Search_ItemsAdapter adapter = (Search_ItemsAdapter) lvsearch.getAdapter();
        adapter.clear();
        adapter.addAll(Search_Results_Model.fromJson(jsonArray));


    }

    //Error:(100, 1) error: searchtask is not abstract and does not override abstract method doInBackground(String...) in AsyncTask
    @Override
    protected String doInBackground(String... url) {
        String response = "";


        try {
            response = get_results(url[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    public String get_results(String url_toget) throws IOException {
        StringBuilder response = new StringBuilder();
        URL url = new URL(url_toget);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String strline;
            while ((strline = input.readLine()) != null) {
                response.append(strline);
            }
            input.close();
        }

        return response.toString();


    }

}

