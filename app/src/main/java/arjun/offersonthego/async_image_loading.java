package arjun.offersonthego;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by ARJUN on 4/7/16.
 */
public class async_image_loading extends AsyncTask<String, Void, Bitmap> {
    Bitmap bmImage;
    async_response_bitmap call;

    int i;

    public async_image_loading(async_response_bitmap resp, int index) {
        call = resp;
        i = index;

    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {

        call.Processbitmap(result, i);
    }
}