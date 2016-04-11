package arjun.offersonthego;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class product_Details extends AppCompatActivity {
    public static JSONObject staticroot;

    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String shop_id = getIntent().getStringExtra(Search_Result.SEARCH_SHOP_ID);
        final String product_id = getIntent().getStringExtra(Search_Result.SEARCH_PRODUCT_ID);
        Button call_text = (Button) findViewById(R.id.shop_phno);
        mcontext = this;

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
                TextView desc = (TextView) findViewById(R.id.descpriton);
                TextView avai = (TextView) findViewById(R.id.Availab);
                ListView randr = (ListView) findViewById(R.id.rating_and_review);
                RatingBar rate = (RatingBar) findViewById(R.id.ratingBar);

                TextView shop_address = (TextView) findViewById(R.id.Shop_Address);


                Button call_text = (Button) findViewById(R.id.call_button);

                try {
                    JSONObject root = new JSONObject(response);
                    staticroot = root;
                    productname.setText(root.getString("product_name"));
                    productcost.setText("MRP:Rs " + root.getString("MRP"));
                    sellingprice.setText("Selling Price:Rs " + root.getString("price"));
                    Product_rating.setText("Rating:" + root.getJSONObject("product_rating_details").getString("avg_product_rating"));
                    List<String> sl = new ArrayList<String>();
                    JSONArray shop_ratings = root.getJSONArray("shop_ratings");
                    for (int i = 0; i < shop_ratings.length(); i++) {
                        JSONObject each = shop_ratings.getJSONObject(i);
                        sl.add(each.getString("customer_name") + ":" + each.getString("reviews") + "(" + each.getString("rating") + ")");

                    }

                    randr.setAdapter(new ArrayAdapter<String>(mcontext, R.layout.rating_and_review_listview, sl));
                    if (root.getString("availability").equals("Y")) {

                        avai.setText("STATUS:AVAILABLE");
                    } else {

                        avai.setText("STATUS:OUT OF STOCK");

                    }
                    rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
/*
                            try {
                                String pid=staticroot.getString("product_id");// "http://offersonthego.16mb.com/API/update_product_rating.php?pid=" + staticroot.getString("product_id") + "&sid=" + staticroot.getString("shopid") + "&rating=" + String.valueOf(rating);
                                String sid=staticroot.getString("shopid");// "http://offersonthego.16mb.com/API/update_product_rating.php?pid=" + staticroot.getString("product_id") + "&sid=" + staticroot.getString("shopid") + "&rating=" + String.valueOf(rating);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }*/
                            common_net_task ratingsender = new common_net_task(new common_net_task_Runnnable() {
                                @Override
                                public void init(String response) {

                                }

                                @Override
                                public void run() {

                                }
                            }, "http://offersonthego.16mb.com/API/update_product_rating.php?pid=" + product_id + "&sid=" + shop_id + "&rating=" + String.valueOf(rating));
                            ratingsender.execute();
                        }
                    });

                    call_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + staticroot.getJSONObject("shop_details").getString("ph_no")));

                                startActivity(in);
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getApplicationContext(), "yourActivity is not found", Toast.LENGTH_SHORT).show();
                            } catch (JSONException ex) {

                                ex.printStackTrace();
                            }

                        }
                    });


                    try {

                        yousave.setText("You save Rs" + String.valueOf(Double.valueOf(root.getString("MRP")) - Double.valueOf(root.getString("price"))));
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();


                    }
                    desc.setText(root.getString("description"));
                    shop_address.setText(root.getJSONObject("shop_details").getString("shop_name") + "\n" + root.getJSONObject("shop_details").getString("address") + "\n" + root.getJSONObject("shop_details").getString("region"));
                    call_text.setText("Call " + root.getJSONObject("shop_details").getString("ph_no"));
                    async_image_loading product_image_download = new async_image_loading(new async_response_bitmap() {
                        @Override
                        public void Processbitmap(Bitmap b, int i) {
                            ImageView img = (ImageView) findViewById(R.id.product_image);
                            img.setImageBitmap(b);
                        }
                    }, 0);
                    product_image_download.execute(root.getString("product_image"));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

            }
        }, "http://offersonthego.16mb.com/API/api.single_product.php?shopid=" + shop_id + "&pid=" + product_id);
        product_details_fetch.execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
