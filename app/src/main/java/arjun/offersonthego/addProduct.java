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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class addProduct extends AppCompatActivity {

    public Button send;
    public EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences shopIdentifier = getSharedPreferences("shopData", Context.MODE_PRIVATE);
        String shop_Identifier = (shopIdentifier.getString("shopIdentifier", ""));
        TextView sh_identifier = (TextView)findViewById(R.id.identifier);

        sh_identifier.setText(shop_Identifier);


        send = (Button)findViewById(R.id.sendVal);
        text =(EditText)findViewById(R.id.value);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  msg = text.getText().toString();


                //check whether the msg empty or not
                if(msg.length()>0) {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://10.0.2.2/mini/API/addTest.php");

                    try {
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("id", "01"));
                        nameValuePairs.add(new BasicNameValuePair("data", msg));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        httpclient.execute(httppost);
                        text.setText(""); //reset the message text field
                        Toast.makeText(getBaseContext(),"Sent",Toast.LENGTH_SHORT).show();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //display message if text field is empty
                    Toast.makeText(getBaseContext(),"All fields are required",Toast.LENGTH_SHORT).show();
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
