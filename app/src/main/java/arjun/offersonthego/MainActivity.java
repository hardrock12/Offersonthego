package arjun.offersonthego;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final static String SEARCH_TERM = "arjun.mainactivity.SEARCHTERM";
    public final static String SEARCH_CATEGORY = "arjun.mainactivity.SEARCHCATEGEORY";
    public final static String SEARCH_REGION = "arjun.mainactivity.SEARCHREGION";
    public MenuItem sign_in;
    // public Menu menu;
    Context context;
    String msearch_category = "";
    String msearch_term = "";
    String mregion_term = "Nearby";
    EditText txt_search;
    EditText region_search;
    ListView lv;
    Button s;
    Dialog region_dialog;

    ArrayAdapter<String> adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        // menu = (Menu) findViewById(R.id.sigin_as_seller);
        sign_in = (MenuItem) findViewById(R.id.sigin_as_seller);
        txt_search = (EditText) findViewById(R.id.txt_search);


// setup spin
        Spinner spin = (Spinner) findViewById(R.id.category_list);
      /*  ArrayAdapter<CharSequence> adapter=new ArrayAdapter<CharSequence>(this,R.array.categoryofproducts,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
 */
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mregion_term = "Nearby";
                        break;
                    case 1:

                        region_dialog = new Dialog(context);
                        region_dialog.setCancelable(false);
                        region_dialog.setContentView(R.layout.region_select_dialog);
                        region_dialog.setTitle("Select Region:");
                        lv = (ListView) region_dialog.findViewById(R.id.lv_suggest);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView s = (TextView) parent.getSelectedView();
                                mregion_term = s.getText().toString();
region_dialog.dismiss();
                            }
                        });
                        adp = new ArrayAdapter<String>(context, R.layout.suggestion_listview_layout);
                        lv.setAdapter(adp);


                        region_search = (EditText) region_dialog.findViewById(R.id.dialog_region);

                        s = (Button) region_dialog.findViewById(R.id.btn_show_suggest);
                        s.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String str = region_search.getText().toString();
                                Log.i("ootg", str + " recieved");

                                common_net_task address_ret = new common_net_task(new common_net_task_Runnnable() {
                                    String response = "";

                                    public void init(String s) {
                                        response = s;

                                    }

                                    @Override
                                    public void run() {
                                        ArrayList<String> arrayList = new ArrayList<String>();
                               /* Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                try {
                                    List<Address> addressList = geocoder.getFromLocationName(str, 10);
                                    Log.i("ootg",addressList.toString());
                                    for (int i = 0; i < addressList.size(); i++) {
                                        String address = "";
                                        int len = addressList.get(i).getMaxAddressLineIndex();
                                        for (int j = 0; j < len; j++) {
                                            address = address + "," + addressList.get(i).getAddressLine(j);


                                        }
                                      address = address + addressList.get(i).getLocality();*/
                                        String address = "";
                                        Log.i("ootg", response);
                                        try {
                                            JSONArray addresseslist = new JSONObject(response).getJSONArray("results");
                                            for (int i = 0; i < addresseslist.length(); i++) {


                                                address = addresseslist.getJSONObject(i).getString("formatted_address");
                                                Log.i("ootg", address);
                                                arrayList.add(address);

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        adp.clear();
                                        adp.addAll(arrayList);
                                        adp.notifyDataSetChanged();
                                    }

                                }, "https://maps.googleapis.com/maps/api/geocode/json?address=" + str + "&key=AIzaSyBLP5q5-qduRWzkP0Tqh4_Unlt2TTbuL0Y");
                                address_ret.execute();


                            }
                        });

                        region_search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String str = s.toString();


                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                        region_dialog.show();
                        break;
                }

                //   Toast.makeText(context, tv.getText(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);

            }
        });
        //==================
// setup appbar
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);
        //setup_txtfield_change
        EditText search_term = (EditText) findViewById(R.id.txt_search);
        search_term.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                msearch_term = txt_search.getText().toString();
            }
        });
//setup button

        TextView search_txt_view = (TextView) findViewById(R.id.btn_Search);
        search_txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Search_Result.class);
                intent.putExtra(SEARCH_TERM, msearch_term);
                intent.putExtra(SEARCH_CATEGORY, msearch_category);
                intent.putExtra(SEARCH_REGION,mregion_term);
                startActivity(intent);


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        } else if (id == R.id.sigin_as_seller) {
            Intent intent = new Intent(context, login.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


}