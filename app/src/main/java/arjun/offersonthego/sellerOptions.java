package arjun.offersonthego;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

public class sellerOptions extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Context context;
   // public static String LOGIN_PHP_SCRIPT = "http://offersonthego.16mb.com/API/loginapi.php?";
    public static String LOGIN_NAMES = "";
    public static String LOGIN_PASSWDS = "";
    public static String OPERATION = "arjun.selleroptions.operation";
    SharedPreferences shopIdentifier;

    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        Intent intent = getIntent();

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String log1 = (sharedPreferences.getString("user", ""));
        String pwd1 = (sharedPreferences.getString("passwd",""));

        String loginname = intent.getStringExtra(login.LOGIN_NAME);
        String loginpasswd = intent.getStringExtra(login.LOGIN_PASSWD);
        LOGIN_NAMES = loginname;
        LOGIN_PASSWDS = loginpasswd;
        Log.i("ootg", loginname + ":" + loginpasswd);


        LoginSellerTask tasks = new LoginSellerTask(findViewById(android.R.id.content),this);
       // Log.i("ootg","http://offersonthego.16mb.com/API/sellerHomeApi.php?usr=" + loginname + "&pwd=" + loginpasswd);
        //tasks.execute("http://offersonthego.16mb.com/API/sellerHomeApi.php?usr=" + loginname + "&pwd=" + loginpasswd);

        Log.i("ootg", "http://offersonthego.16mb.com/API/sellerHomeApi.php?usr=" + log1 + "&pwd=" + pwd1);
        tasks.execute("http://offersonthego.16mb.com/API/sellerHomeApi.php?usr=" + log1 + "&pwd=" + pwd1);




        shopIdentifier = getSharedPreferences("shopData", Context.MODE_PRIVATE);



          final TextView shopi=(TextView)findViewById(R.id.shop_id);
        shopi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                SharedPreferences.Editor value = shopIdentifier.edit();

                value.putString("shopIdentifier", shopi.getText().toString());
                value.commit();

            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seller_options, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_product) {

            Intent intent=new Intent(context,addProduct.class);
            context.startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.update_product) {

            Intent intent=new Intent(context,Seller_products.class);
            intent.putExtra(OPERATION, "update");
            context.startActivity(intent);

        } else if (id == R.id.change_availability) {

        } else if (id == R.id.remove_product) {

        } else if (id == R.id.update_location) {

        } else if (id == R.id.feature_product) {

        }
        else if (id == R.id.logout) {

            Intent intent=new Intent(context,login.class);
            context.startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
class LoginSellerTask extends AsyncTask<String, Void, String> {
    private View rv;

    private Context context;


    LoginSellerTask(View rootview, Context context) {
        this.rv = rootview;
        this.context = context;
    }


    @Override
    protected void onPostExecute(String response) {
        JSONArray jsonArray = null;
        Log.i("ootg", "json response:" + response);
        try {
            jsonArray = new JSONArray(response);
            JSONObject object0 = jsonArray.getJSONObject(0);
            if(object0.has("error"))
            {
                //return to login
                Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context,login.class);
                context.startActivity(intent);
                return;
            }




            JSONObject object1 = jsonArray.getJSONObject(1);
            JSONObject object2 = jsonArray.getJSONObject(2);
            JSONObject object3 = jsonArray.getJSONObject(3);
            TextView sh_id=(TextView)rv.findViewById(R.id.shop_id);
            TextView sh_name=(TextView)rv.findViewById(R.id.shop_name);
            TextView sh_addr=(TextView)rv.findViewById(R.id.shop_address);
            TextView sh_noi=(TextView)rv.findViewById(R.id.shop_noi);
            TextView sh_noo=(TextView)rv.findViewById(R.id.shop_noo);
            TextView sh_nor=(TextView)rv.findViewById(R.id.shop_nor);
            TextView sh_phno=(TextView)rv.findViewById(R.id.shop_phno);
            TextView sh_region=(TextView)rv.findViewById(R.id.shop_region);
            TextView sh_seller=(TextView)rv.findViewById(R.id.shop_sellerName);
            TextView sh_type=(TextView)rv.findViewById(R.id.shop_type);
            TextView sh_rate=(TextView)rv.findViewById(R.id.shop_rating);

            sh_nor.setText(object0.getString("count_all"));
            sh_noi.setText(object1.getString("count_avail"));
            sh_noo.setText(object2.getString("count_out_of_stock"));
            sh_id.setText(object3.getString("shopid"));
            sh_name.setText(object3.getString("shop_name"));
            sh_addr.setText(object3.getString("address"));
            sh_phno.setText(object3.getString("ph_no"));
            sh_region.setText(object3.getString("region"));
            sh_seller.setText(object3.getString("name"));
            sh_type.setText(object3.getString("shop_type"));

            //shop_iden = sh_id.getText().toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }


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


