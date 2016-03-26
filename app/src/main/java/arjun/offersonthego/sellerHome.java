package arjun.offersonthego;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

class Login_Model {
    public String sid;
    public String shopname;
    public String address;
    public String rating;
    public String region;

    public String shoptype;
    public String phno;


    /*
    public String product_name;
    public String category;
    public String description;
    public String price;
    public String Avail;
    public String shopid;
    public String productid;
    */
    public static Login_Model fromJson(JSONObject jsonObject) {
        Login_Model model = new Login_Model();
        try {
            model.sid = jsonObject.getString("shopid");
            model.shopname = jsonObject.getString("shop_name");
            model.address = jsonObject.getString("address");
            model.rating = jsonObject.getString("rating");
            model.region = jsonObject.getString("region");
            model.shoptype = jsonObject.getString("shop_type");
            model.phno = jsonObject.getString("ph_no");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return model;
    }

    public static ArrayList<Login_Model> fromJson(JSONArray jsonArray) {
      ArrayList<Login_Model> models = new ArrayList<Login_Model>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json_of_one_model = null;

            try {
                json_of_one_model = jsonArray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Login_Model model = Login_Model.fromJson(json_of_one_model);
            models.add(model);


        }
        return models;
    }

}


public class sellerHome extends AppCompatActivity {
    Context context;
    public static String LOGIN_PHP_SCRIPT = "http://10.0.2.2/mini/API/loginapi.php?";
    public static String LOGIN_NAMES = "";
    public static String LOGIN_PASSWDS = "";
   // public TextView v;
    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        Intent intent = getIntent();

        String loginname = intent.getStringExtra(login.LOGIN_NAME);
        String loginpasswd = intent.getStringExtra(login.LOGIN_PASSWD);
        LOGIN_NAMES = loginname;
        LOGIN_PASSWDS = loginpasswd;
Log.i("ootg",loginname+":"+loginpasswd);

       // v = (TextView)findViewById(R.id.sh_id);
        //v.setText(loginname);
     //  v.setText("HAI");
       // ArrayList<Login_Model> arraylist = new ArrayList<Login_Model>();

        //code remaining
        ///////////////////////////////////////////////continue from here
        ArrayList<Login_Model> arraylist = new ArrayList<Login_Model>();


        /*
        Search_ItemsAdapter search_itemsAdapter = new Search_ItemsAdapter(this, arraylist);
        ListView lvsresults = (ListView) findViewById(R.id.lv_search_results);
        lvsresults.setAdapter(search_itemsAdapter);

        */
// setting on click listener for Listview

        /*
        lvsresults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
      */

        //new thread for async network task
        LoginTask tasks = new LoginTask(findViewById(android.R.id.content),this);
        Log.i("ootg","http://10.0.2.2/mini/API/loginapi.php?usr=" + loginname + "&pwd=" + loginpasswd);
       tasks.execute("http://10.0.2.2/mini/API/loginapi.php?usr=" + loginname + "&pwd=" + loginpasswd);
        //tasks.execute("http://10.0.2.2/mini/API/loginapi.php?usr=arjun123&pwd=1234");
       // tasks.execute("http://10.0.2.2/mini/API/loginapi.php?usr="+"arjun123"+"&pwd="+"1234");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "seller Home Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
class LoginTask extends AsyncTask<String, Void, String> {
    private View rv;
    private Context context;

    LoginTask(View rootview,Context context) {
        this.rv = rootview;
        this.context=context;
    }

    @Override
    protected void onPostExecute(String response) {
        Log.i("ootg",response);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            if(jsonObject.has("error"))
            {
                //return to login
                Toast.makeText(context,"Incorrect username or password",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context,login.class);
                context.startActivity(intent);
return;
            }


            TextView sh_id=(TextView)rv.findViewById(R.id.sh_id);
            TextView sh_name=(TextView)rv.findViewById(R.id.sh_name);
            TextView sh_address=(TextView)rv.findViewById(R.id.sh_address);
            TextView sh_region=(TextView)rv.findViewById(R.id.sh_region);
            TextView sh_type=(TextView)rv.findViewById(R.id.sh_type);
            TextView sh_pno=(TextView)rv.findViewById(R.id.sh_pno);
            sh_id.setText(jsonObject.getString("shopid"));
            sh_name.setText(jsonObject.getString("shop_name"));
            sh_address.setText(jsonObject.getString("address"));
            sh_region.setText(jsonObject.getString("region"));
            sh_type.setText(jsonObject.getString("shop_type"));
            sh_pno.setText(jsonObject.getString("ph_no"));
            Log.i("ootg",sh_name.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
/*
        ListView lvsearch = (ListView) rv.findViewById(R.id.lv_search_results);
        Search_ItemsAdapter adapter = (Search_ItemsAdapter) lvsearch.getAdapter();
        adapter.clear();
        adapter.addAll(Search_Results_Model.fromJson(jsonArray));
*/


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