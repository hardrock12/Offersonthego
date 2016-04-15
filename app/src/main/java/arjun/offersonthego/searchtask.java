package arjun.offersonthego;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ARJUN on 4/2/16.
 */
class searchtask extends AsyncTask<String, Void, String> {
    private View rv;
public ArrayList<Search_Results_Model> stored_response=null;
    boolean response_recieved=false;
    private Context mcontext;
    public Runnable updategps;

    searchtask(View rootview, Context context,Runnable update_gps) {
        this.rv = rootview;
        this.mcontext = context;
        updategps=update_gps;
    }


    @Override
    protected void onPostExecute(String response) {
        JSONArray jsonArray = null;
        Log.i("ootg", "json response:" + response);
        try {
            jsonArray = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mcontext,"Error downloading search results",Toast.LENGTH_LONG).show();
            return;
        }


        ListView lvsearch = (ListView) rv.findViewById(R.id.lv_search_results);
        Search_ItemsAdapter adapter = (Search_ItemsAdapter) lvsearch.getAdapter();
        adapter.clear();
        Search_Result.stored_search_results=Search_Results_Model.fromJson(jsonArray);

        if(response!="")
        {

            response_recieved=true;
            Search_Result.response_Ready=true;
updategps.run();
        }
        adapter.addAll(Search_Result.stored_search_results);


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
