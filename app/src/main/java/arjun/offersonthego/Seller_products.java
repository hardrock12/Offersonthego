package arjun.offersonthego;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


class Seller_Products_Results_Model {
    public String product_name;
    public String category;
    public String description;
    public String price;
    public String Avail;
    public String shopid;
    public String productid;

    public static Seller_Products_Results_Model fromJson(JSONObject jsonObject) {
        Seller_Products_Results_Model model = new Seller_Products_Results_Model();
        try {
            model.product_name = jsonObject.getString("product_name");
            model.category = jsonObject.getString("catgory");
            model.productid = jsonObject.getString("product_id");
            model.shopid = jsonObject.getString("shopid");
            model.description = jsonObject.getString("description");
            model.price = jsonObject.getString("price");
            model.Avail = jsonObject.getString("availability");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return model;
    }

    public static ArrayList<Seller_Products_Results_Model> fromJson(JSONArray jsonArray) {
        ArrayList<Seller_Products_Results_Model> models = new ArrayList<Seller_Products_Results_Model>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json_of_one_model = null;

            try {
                json_of_one_model = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Seller_Products_Results_Model model = Seller_Products_Results_Model.fromJson(json_of_one_model);
            models.add(model);


        }


        return models;

    }

}


public class Seller_products extends AppCompatActivity {


    public Button search_button;
    public EditText search_text;
    Context context;
    public String term;
    public TextView sh_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search_text =(EditText)findViewById(R.id.search_key);
        search_button = (Button)findViewById(R.id.button_search_spro);
        sh_id = (TextView)findViewById(R.id.sho_iden);

        SharedPreferences shopIdentifier = getSharedPreferences("shopData", Context.MODE_PRIVATE);
        final String shop_Identifier = (shopIdentifier.getString("shopIdentifier", ""));

        sh_id.setText(shop_Identifier);

        context=this;

        ArrayList<Seller_Products_Results_Model> arraylist = new ArrayList<Seller_Products_Results_Model>();
        Seller_Items_Adapter search_itemsAdapter = new Seller_Items_Adapter(this, arraylist);
        ListView lvsresults = (ListView) findViewById(R.id.listview_seller_products);
        lvsresults.setAdapter(search_itemsAdapter);
// setting on click listener for Listview
        lvsresults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });






      search_button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {


              term=search_text.getText().toString();
              seller_products_task tasks = new seller_products_task(findViewById(android.R.id.content));
             // tasks.execute("http://offersonthego.16mb.com/API/api.products.php?searchterm=" + searchterm + "&searchcategory=" + searchcat);
              tasks.execute("http://10.0.3.2/mini/API/arjun/sellerProducts.php?shopid=" + shop_Identifier + "&search=" + term);


          }
      });




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
class seller_products_task extends AsyncTask<String, Void, String> {
    private View rv;

    seller_products_task(View rootview) {
        this.rv = rootview;
    }

    @Override
    protected void onPostExecute(String response) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ListView lvsearch = (ListView) rv.findViewById(R.id.listview_seller_products);
       Seller_Items_Adapter adapter = (Seller_Items_Adapter) lvsearch.getAdapter();
        adapter.clear();
        adapter.addAll(Seller_Products_Results_Model.fromJson(jsonArray));


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
