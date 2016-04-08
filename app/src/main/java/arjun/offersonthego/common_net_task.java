package arjun.offersonthego;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class common_net_task extends AsyncTask<Void, Void, String> {

    common_net_task_Runnnable interface_to_run;
    String url_to_get;


    common_net_task(common_net_task_Runnnable r, String url) {
        url_to_get = url;
        interface_to_run = r;


    }

    @Override
    protected void onPostExecute(String response) {
        interface_to_run.init(response);
        interface_to_run.run();
    }

    //Error:(100, 1) error: searchtask is not abstract and does not override abstract method doInBackground(String...) in AsyncTask
    @Override
    protected String doInBackground(Void... url) {
        String response = "";


        try {
            response = get_results(url_to_get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("ootg", "commonnettask:fetched ==>" + response);
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


