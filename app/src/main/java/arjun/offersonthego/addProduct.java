package arjun.offersonthego;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class addProduct extends AppCompatActivity {

    public Button send, btnpimage;
    public ImageView p_image_preview;
    public EditText pid, pname, pdesc, pprice, ptype, pmrp, text;
    public String cat;
    public Spinner pcat, pavail;
    public String category_product = "default";
    Context context;
    String image_path_berowsed = "";
    String Server_full_path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String category;


        SharedPreferences shopIdentifier = getSharedPreferences("shopData", Context.MODE_PRIVATE);
        final String shop_Identifier = (shopIdentifier.getString("shopIdentifier", ""));
        TextView sh_identifier = (TextView) findViewById(R.id.identifier);

        sh_identifier.setText(shop_Identifier);

      /*  spinner here*/

        send = (Button) findViewById(R.id.sendVal);
        // text =(EditText)findViewById(R.id.P_id);

        pid = (EditText) findViewById(R.id.P_id);
        pname = (EditText) findViewById(R.id.P_name);
        pdesc = (EditText) findViewById(R.id.P_desc);
        pprice = (EditText) findViewById(R.id.P_price);
        pmrp = (EditText) findViewById(R.id.P_mrp);
        ptype = (EditText) findViewById(R.id.P_type);
        pcat = (Spinner) findViewById(R.id.P_category);
        pavail = (Spinner) findViewById(R.id.P_avail);
        btnpimage = (Button) findViewById(R.id.btn_browse);
        p_image_preview = (ImageView) findViewById(R.id.preview_image_view);

        String avail = pavail.getSelectedItem().toString();
        final String available;
        if (avail.equals("Available")) {
            available = "Y";
        } else {
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

                    public int uploadFile(String sourceFileUri) {
                        String upLoadServerUri = "http://offersonthego.16mb.com/API/file_upload.php";
                        int serverResponseCode = 0;
                        String uploaded_file = "";
                        String fileName = sourceFileUri;

                        HttpURLConnection conn = null;
                        DataOutputStream dos = null;
                        String lineEnd = "\r\n";
                        String twoHyphens = "--";
                        String boundary = "*****";
                        int bytesRead, bytesAvailable, bufferSize;
                        byte[] buffer;
                        int maxBufferSize = 1 * 1024 * 1024;
                        File sourceFile = new File(sourceFileUri);

                        if (!sourceFile.isFile()) {


                            return 0;

                        } else {
                            try {

                                // open a URL connection to the Servlet
                                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                                URL url = new URL(upLoadServerUri);

                                // Open a HTTP  connection to  the URL
                                conn = (HttpURLConnection) url.openConnection();
                                conn.setDoInput(true); // Allow Inputs
                                conn.setDoOutput(true); // Allow Outputs
                                conn.setUseCaches(false); // Don't use a Cached Copy
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("Connection", "Keep-Alive");
                                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                                conn.setRequestProperty("uploaded_file", fileName);

                                dos = new DataOutputStream(conn.getOutputStream());

                                dos.writeBytes(twoHyphens + boundary + lineEnd);
                                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                        + fileName + "\"" + lineEnd);

                                dos.writeBytes(lineEnd);

                                // create a buffer of  maximum size
                                bytesAvailable = fileInputStream.available();

                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                buffer = new byte[bufferSize];

                                // read file and write it into form...
                                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                                while (bytesRead > 0) {

                                    dos.write(buffer, 0, bufferSize);
                                    bytesAvailable = fileInputStream.available();
                                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                                }

                                // send multipart form data necesssary after file data...
                                dos.writeBytes(lineEnd);
                                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                                // Responses from the server (code and message)
                                serverResponseCode = conn.getResponseCode();
                                String serverResponseMessage = conn.getResponseMessage();

                                Log.i("ootg", "HTTP Response is : "
                                        + serverResponseMessage + ": " + serverResponseCode);

                                if (serverResponseCode == 200) {
                                    StringBuilder response = new StringBuilder();
                                    BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    String strline;
                                    while ((strline = input.readLine()) != null) {
                                        response.append(strline);
                                    }
                                    input.close();

                                    Log.i("ootg", response.toString());
                                    Server_full_path = response.toString();
                                }

                                //close the streams //
                                fileInputStream.close();
                                dos.flush();
                                dos.close();

                            } catch (MalformedURLException ex) {


                                ex.printStackTrace();


                                Log.e("ootg", "error: " + ex.getMessage(), ex);
                            } catch (Exception e) {


                                e.printStackTrace();


                                Log.e("ootg", "Exception : "
                                        + e.getMessage(), e);
                            }

                            return serverResponseCode;

                        } // End else block
                    }

                    @Override
                    protected Void doInBackground(Void... params) {

                        if (image_path_berowsed != "") {

                            uploadFile(image_path_berowsed);


                        }


                        try {
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(9);
                            nameValuePairs.add(new BasicNameValuePair("sh_id", shop_Identifier));
                            nameValuePairs.add(new BasicNameValuePair("p_id", pro_id));
                            nameValuePairs.add(new BasicNameValuePair("p_name", pro_name));
                            nameValuePairs.add(new BasicNameValuePair("p_desc", pro_desc));
                            nameValuePairs.add(new BasicNameValuePair("p_price", pro_price));
                            nameValuePairs.add(new BasicNameValuePair("p_mrp", pro_mrp));
                            nameValuePairs.add(new BasicNameValuePair("p_type", pro_type));
                            nameValuePairs.add(new BasicNameValuePair("p_cat", pro_cat));
                            nameValuePairs.add(new BasicNameValuePair("p_avail", available));
                            nameValuePairs.add(new BasicNameValuePair("p_image", Server_full_path));
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://offersonthego.16mb.com/API/add_product.php");
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            HttpResponse s = httpclient.execute(httppost);
                            //         StatusLine d=s.getStatusLine();


                        } catch (IOException e) {
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
        btnpimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            //InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
            Log.i("ootg", data.getData().toString());
            image_path_berowsed = data.getData().toString().substring(7);
            Uri targetUri = data.getData();
            //textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                p_image_preview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }

}
