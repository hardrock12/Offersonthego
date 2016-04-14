package arjun.offersonthego;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Update_product extends AppCompatActivity {

    public Button update_button;
    public EditText pid,pname,pdesc,pprice,ptype,pmrp,text;
    public Spinner pcat,pavail;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_update_product);
        setContentView(R.layout.activity_update_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String product_id = getIntent().getStringExtra(Seller_products.PRODUCT_ID);
        Log.i("ootg", "product id:" + product_id);
        TextView pro_id = (TextView)findViewById(R.id.uP_id);
        pro_id.setText(product_id);
        SharedPreferences shopIdentifier = getSharedPreferences("shopData", Context.MODE_PRIVATE);
        final String shop_Identifier = (shopIdentifier.getString("shopIdentifier", ""));
        TextView shop_iden = (TextView)findViewById(R.id.uidentifier);
        shop_iden.setText(shop_Identifier);


        update_button = (Button)findViewById(R.id.usendVal);

       // pid =(EditText)findViewById(R.id.uP_id);
        pname =(EditText)findViewById(R.id.uP_name);
        pdesc =(EditText)findViewById(R.id.uP_desc);
        pprice =(EditText)findViewById(R.id.uP_price);
        pmrp = (EditText)findViewById(R.id.uP_mrp);
        ptype =(EditText)findViewById(R.id.uP_type);
        pcat = (Spinner)findViewById(R.id.uP_category);
        pavail =(Spinner)findViewById(R.id.uP_avail);

        UpdateSellerProductTask tasks = new UpdateSellerProductTask(findViewById(android.R.id.content),this);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Log.i("ootg", "http://offersonthego.16mb.com/API/sellerHomeApi.php?usr=" + log1 + "&pwd=" + pwd1);
        //tasks.execute("http://offersonthego.16mb.com/API/sellerHomeApi.php?usr=" + log1 + "&pwd=" + pwd1);

        Log.i("ootg", "http://offersonthego.16mb.com/API/updateRetrive.php?shopid=" + shop_Identifier + "&pid=" + product_id);
        tasks.execute("http://offersonthego.16mb.com/API/updateRetrive.php?shopid=" + shop_Identifier + "&pid=" + product_id);


        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String avail = pavail.getSelectedItem().toString();
                final String available;
                if(avail.equals("Available"))
                {
                    available = "Y";
                }
                else
                {
                    available = "N";
                }

                final String pro_name = pname.getText().toString();
                final String pro_desc = pdesc.getText().toString();
                final String pro_price = pprice.getText().toString();
                final String pro_mrp = pmrp.getText().toString();
                final String pro_type = ptype.getText().toString();
                final String pro_cat = pcat.getSelectedItem().toString();


                AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {


                    @Override
                    protected Void doInBackground(Void... params) {

                        try {

                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(9);
                            nameValuePairs.add(new BasicNameValuePair("sh_id", shop_Identifier));
                            nameValuePairs.add(new BasicNameValuePair("p_id", product_id));
                            nameValuePairs.add(new BasicNameValuePair("p_name", pro_name));
                            nameValuePairs.add(new BasicNameValuePair("p_desc", pro_desc));
                            nameValuePairs.add(new BasicNameValuePair("p_price", pro_price));
                            nameValuePairs.add(new BasicNameValuePair("p_mrp", pro_mrp));
                            nameValuePairs.add(new BasicNameValuePair("p_type", pro_type));
                            nameValuePairs.add(new BasicNameValuePair("p_cat", pro_cat));
                            nameValuePairs.add(new BasicNameValuePair("p_avail", available));
                            HttpClient httpclient = new DefaultHttpClient();
                            //HttpPost httppost = new HttpPost("http://offersonthego.16mb.com/API/add_product.php");
                            HttpPost httppost = new HttpPost("http://offersonthego.16mb.com/API/update_product.php");
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
                        Toast.makeText(getBaseContext(), "Updated", Toast.LENGTH_SHORT).show();
                        //pid.setText("");
                        // Intent goBack = new Intent(context,Seller_products.class);
                        // startActivity(goBack);
                        // pname.setText("");
                        //pdesc.setText("");
                        //pprice.setText("");
                        //pmrp.setText("");
                        //ptype.setText("");
                    }
                }.execute();
            }
        });



    }

}
class UpdateSellerProductTask extends AsyncTask<String, Void, String> {
    private View rv;
    private Context context;

    UpdateSellerProductTask(View rootview,Context context) {
        this.rv = rootview;
        this.context=context;
    }

    @Override
    protected void onPostExecute(String response) {
        Log.i("ootg",response);
        JSONObject jsonObject = null;
        try {
              jsonObject = new JSONObject(response);
           /* if(jsonObject.has("error"))
            {
                //return to login
                Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context,login.class);
                context.startActivity(intent);
                return;
            }
          */

            EditText pname = (EditText)rv.findViewById(R.id.uP_name);
            EditText pdesc = (EditText)rv.findViewById(R.id.uP_desc);
            EditText pprice = (EditText)rv.findViewById(R.id.uP_price);
            EditText pmrp = (EditText)rv.findViewById(R.id.uP_mrp);
            Spinner pcate = (Spinner)rv.findViewById(R.id.uP_category);
            Spinner avail = (Spinner)rv.findViewById(R.id.uP_avail);
            EditText ptype = (EditText)rv.findViewById(R.id.uP_type);

            pname.setText(jsonObject.getString("product_name"));
            pdesc.setText(jsonObject.getString("description"));
            pprice.setText(jsonObject.getString("price"));
            pmrp.setText(jsonObject.getString("MRP"));
            ptype.setText(jsonObject.getString("type"));
            Log.i("ootg", pname.getText().toString());


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
