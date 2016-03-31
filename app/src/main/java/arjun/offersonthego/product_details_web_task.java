package arjun.offersonthego;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by arjun on 29/3/16.
 */
public class product_details_web_task extends AsyncTask<String, Void, String> {


    private View rv;

    private Context mcontext;

    product_details_web_task(View rootview, Context context) {
        this.rv = rootview;
        this.mcontext = context;
    }


    @Override
    protected void onPostExecute(String response) {
        JSONArray jsonArray = null;
        Log.i("ootg", "json response for products_details:" + response);
        try {
            jsonArray = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ImageView product_image = (ImageView) rv.findViewById(R.id.product_image);
        TextView product_name = (TextView) rv.findViewById(R.id.product_name);
        TextView sellingprice = (TextView) rv.findViewById(R.id.selling_price);
        TextView yousave = (TextView) rv.findViewById(R.id.you_save);
        TextView productrating = (TextView) rv.findViewById(R.id.Product_Rating);
        TextView ShopAddress = (TextView) rv.findViewById(R.id.Shop_Address);


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


