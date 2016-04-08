package arjun.offersonthego;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class product_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String shop_id = getIntent().getStringExtra(Search_Result.SEARCH_SHOP_ID);
        String product_id = getIntent().getStringExtra(Search_Result.SEARCH_PRODUCT_ID);
        common_net_task product_details_fetch = new common_net_task(new common_net_task_Runnnable() {


            String response;
            @Override


            public void init(String response) {
                this.response = response;
            }

            @Override
            public void run() {

                TextView productname = (TextView) findViewById(R.id.product_name);
                TextView productcost = (TextView) findViewById(R.id.pcost);
                TextView sellingprice = (TextView) findViewById(R.id.selling_price);
                TextView yousave = (TextView) findViewById(R.id.you_save);
                TextView Product_rating = (TextView) findViewById(R.id.Product_Rating);


                TextView shop_address = (TextView) findViewById(R.id.Shop_Address);
                TextView shop_contact = (TextView) findViewById(R.id.txt_contactus);

                Button call_text = (Button) findViewById(R.id.call_button);
                try {
                    JSONObject root = new JSONObject(response);
                    productname.setText(root.getString("product_name"));
                    productcost.setText(root.getString("MRP"));
                    sellingprice.setText(root.getString("price"));



                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


            }
        }, "http://offersonthego.16mb.com/API/api.single_product.php?shopid=" + shop_id + "&pid=" + product_id);
        product_details_fetch.execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
