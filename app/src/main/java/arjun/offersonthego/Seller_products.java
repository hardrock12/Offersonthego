package arjun.offersonthego;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


class Seller_Products_Results_Model {
    public String product_name;
    public String category;
    public String description;
    public String price;
    public String Avail;
    public String shopid;
    public String productid;
    public String feature;

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
            model.feature= jsonObject.getString("feature");

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
    public static String OPERATION = "";
    Context context;
    public String term;
    public TextView sh_id;
    public static String PRODUCT_ID="arjun.offersonthego.updateproduct.productid";
    public AlertDialog.Builder dialogBuilder;

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
        Intent intent = getIntent();
        final String operation = intent.getStringExtra(sellerOptions.OPERATION);
        TextView oper = (TextView)findViewById(R.id.id_operation);
        oper.setText(operation);

        final ArrayList<Seller_Products_Results_Model> arraylist = new ArrayList<Seller_Products_Results_Model>();
        Seller_Items_Adapter search_itemsAdapter = new Seller_Items_Adapter(this, arraylist);
        ListView lvsresults = (ListView) findViewById(R.id.listview_seller_products);
        lvsresults.setAdapter(search_itemsAdapter);
// setting on click listener for Listview
        lvsresults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Log.i("ootg", "Operation" + operation);
                if (operation.equals("update")) {
                    Intent update_pro = new Intent(context, Update_product.class);
                    Log.i("ootg", "PRODUCT ID:" + PRODUCT_ID);
                    update_pro.putExtra(PRODUCT_ID, arraylist.get(position).productid);
                    startActivity(update_pro);

                } else if (operation.equals("remove")) {
                    showMessage(shop_Identifier, arraylist.get(position).productid);
                } else if (operation.equals("availability")) {

                    AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {


                        @Override
                        protected Void doInBackground(Void... params) {

                            try {
                                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                                Log.i("log:", "shop id:" + shop_Identifier + "pid:" + arraylist.get(position).productid);
                                nameValuePairs.add(new BasicNameValuePair("shopid", shop_Identifier));
                                nameValuePairs.add(new BasicNameValuePair("pid", arraylist.get(position).productid));
                                HttpClient httpclient = new DefaultHttpClient();
                                HttpPost httppost = new HttpPost("http://offersonthego.16mb.com/API/availabilityapi.php");
                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                httpclient.execute(httppost);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            // Notifies UI when the task is done
                            // textView.setText("Insert finished!");
                            Toast.makeText(getBaseContext(), "availability changed", Toast.LENGTH_SHORT).show();
                            term = search_text.getText().toString();
                            seller_products_task tasks = new seller_products_task(findViewById(android.R.id.content));
                            // tasks.execute("http://offersonthego.16mb.com/API/api.products.php?searchterm=" + searchterm + "&searchcategory=" + searchcat);
                            tasks.execute("http://offersonthego.16mb.com/API/sellerProducts.php?shopid=" + shop_Identifier + "&search=" + term);
                        }
                    }.execute();

                } else if (operation.equals("feature")) {

                    AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {


                        @Override
                        protected Void doInBackground(Void... params) {

                            try {
                                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                                Log.i("log:", "shop id:" + shop_Identifier + "pid:" + arraylist.get(position).productid);
                                nameValuePairs.add(new BasicNameValuePair("shopid", shop_Identifier));
                                nameValuePairs.add(new BasicNameValuePair("pid", arraylist.get(position).productid));
                                HttpClient httpclient = new DefaultHttpClient();
                                HttpPost httppost = new HttpPost("http://offersonthego.16mb.com/API/featuredapi.php");
                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                httpclient.execute(httppost);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            // Notifies UI when the task is done
                            // textView.setText("Insert finished!");

                            String fstatus = arraylist.get(position).feature;
                            if (fstatus.equals("F"))
                            {
                                Toast.makeText(getBaseContext(), "product unfeatured", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(), "product featured", Toast.LENGTH_SHORT).show();
                            }

                        term=search_text.getText().toString();

                        seller_products_task tasks = new seller_products_task(findViewById(android.R.id.content));
                        tasks.execute("http://offersonthego.16mb.com/API/sellerProducts.php?shopid="+shop_Identifier+"&search="+term);
                        // tasks.execute("http://10.0.3.2/mini/API/arjun/sellerProducts.php?shopid=" + shop_Identifier + "&search=" + term);
                    }
                }.execute();


            }
        }
    });

      search_button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {


              term=search_text.getText().toString();
              seller_products_task tasks = new seller_products_task(findViewById(android.R.id.content));
              tasks.execute("http://offersonthego.16mb.com/API/sellerProducts.php?shopid=" + shop_Identifier + "&search=" + term);
              //tasks.execute("http://10.0.3.2/mini/API/arjun/sellerProducts.php?shopid=" + shop_Identifier + "&search=" + term);

          }
      });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void showMessage(final String shop_identi, final String product_identifier)
    {

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Delete");
        dialogBuilder.setMessage("Are you sure you want to delete?");
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {


                    @Override
                    protected Void doInBackground(Void... params) {

                        try {
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                            Log.i("log:","shop id:" + shop_identi + "pid:" + product_identifier);
                            nameValuePairs.add(new BasicNameValuePair("shopid",shop_identi));
                            nameValuePairs.add(new BasicNameValuePair("pid", product_identifier));
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://offersonthego.16mb.com/API/deleteapi.php");
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            httpclient.execute(httppost);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        // Notifies UI when the task is done
                        // textView.setText("Insert finished!");
                        Toast.makeText(getBaseContext(), "Product Deleted", Toast.LENGTH_SHORT).show();
                        term=search_text.getText().toString();
                        seller_products_task tasks = new seller_products_task(findViewById(android.R.id.content));
                        // tasks.execute("http://offersonthego.16mb.com/API/api.products.php?searchterm=" + searchterm + "&searchcategory=" + searchcat);
                        tasks.execute("http://offersonthego.16mb.com/API/sellerProducts.php?shopid=" + shop_identi + "&search=" + term);
                    }
                }.execute();





            }
        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getBaseContext(), "Product not removed", Toast.LENGTH_SHORT).show();


            }
        });

        AlertDialog deleteDialog = dialogBuilder.create();
        deleteDialog.show();
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
