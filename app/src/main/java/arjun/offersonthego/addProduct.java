package arjun.offersonthego;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;



import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class addProduct extends AppCompatActivity {

    public Button send;
    public EditText pid,pname,pdesc,pprice,ptype,pmrp,text;
    public String cat;
    public Spinner pcat,pavail;
    Context context;
    public String category_product="default";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String category;


        SharedPreferences shopIdentifier = getSharedPreferences("shopData", Context.MODE_PRIVATE);
        final String shop_Identifier = (shopIdentifier.getString("shopIdentifier", ""));
        TextView sh_identifier = (TextView)findViewById(R.id.identifier);

        sh_identifier.setText(shop_Identifier);

      /*  spinner here*/

        send = (Button)findViewById(R.id.sendVal);
       // text =(EditText)findViewById(R.id.P_id);

        pid =(EditText)findViewById(R.id.P_id);
        pname =(EditText)findViewById(R.id.P_name);
        pdesc =(EditText)findViewById(R.id.P_desc);
        pprice =(EditText)findViewById(R.id.P_price);
        pmrp = (EditText)findViewById(R.id.P_mrp);
        ptype =(EditText)findViewById(R.id.P_type);
        pcat = (Spinner)findViewById(R.id.P_category);
        pavail =(Spinner)findViewById(R.id.P_avail);

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



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String  msg = text.getText().toString();
                final String pro_id = pid.getText().toString();
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
                            nameValuePairs.add(new BasicNameValuePair("sh_id",shop_Identifier ));
                            nameValuePairs.add(new BasicNameValuePair("p_id", pro_id));
                            nameValuePairs.add(new BasicNameValuePair("p_name", pro_name));
                            nameValuePairs.add(new BasicNameValuePair("p_desc", pro_desc));
                            nameValuePairs.add(new BasicNameValuePair("p_price", pro_price));
                            nameValuePairs.add(new BasicNameValuePair("p_mrp", pro_mrp));
                            nameValuePairs.add(new BasicNameValuePair("p_type", pro_type));
                            nameValuePairs.add(new BasicNameValuePair("p_cat", pro_cat));
                            nameValuePairs.add(new BasicNameValuePair("p_avail", available));
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://offersonthego.16mb.com/API/add_product.php");
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
                        Toast.makeText(getBaseContext(), "Sent", Toast.LENGTH_SHORT).show();
                        pid.setText("");
                        pname.setText("");
                        pdesc.setText("");
                        pprice.setText("");
                        pmrp.setText("");
                        ptype.setText("");
                    }
                }.execute();

                    /*///////////////////////////////
        Spinner spin=(Spinner)findViewById(R.id.P_category);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch(position)
                {
                    case 0: category_product = "Electronics";
                          break;
                    case 1: category_product = "Groceries";
                        break;
                    case 2: category_product = "Home and Furniture";
                        break;
                    case 3: category_product = "Clothing";
                        break;
                    case 4: category_product = "Home and Furniture";
                        break;
                    case 5: category_product = "Sports";
                        break;
                    case 6: category_product = "Food";
                        break;
                    case 7: category_product = "Electrical";
                        break;
                    case 8: category_product = "Books and Media";
                        break;
                    case 9: category_product = "Pharmacy";
                        break;
                    case 10: category_product = "Other";
                        break;



                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        */

                //  Toast.makeText(context, cat, Toast.LENGTH_LONG).show();

                // Toast.makeText(context, "cat:"+category_product, Toast.LENGTH_LONG).show();

                // Log.i("ootg", "cat:"+category_product);


           /////////////////////////////////////////////









                /*

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String msg = msg1;

                        if (msg.length() > 0) {
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://10.0.3.2/mini/API/addTest.php");

                            try {
                                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                                nameValuePairs.add(new BasicNameValuePair("id", "01"));
                                nameValuePairs.add(new BasicNameValuePair("message", msg));
                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                httpclient.execute(httppost);
                                //text.setText(""); //reset the message text field
                                //Toast.makeText(getBaseContext(), "Sent", Toast.LENGTH_SHORT).show();
                            } catch (ClientProtocolException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        catch(Exception e)
                            {
                               e.printStackTrace();
                            }

                        }

                    }
                });


                t.start();

                text.setText("*");
              */

                    //display message if text field is empty
                    //Toast.makeText(getBaseContext(),"All fields are required",Toast.LENGTH_SHORT).show();

            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
