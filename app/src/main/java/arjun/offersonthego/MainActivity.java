package arjun.offersonthego;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

public final  static  String SEARCH_TERM="arjun.mainactivity.SEARCHTERM";
public final  static  String SEARCH_CATEGORY="arjun.mainactivity.SEARCHCATEGEORY";

    Context context;
    String msearch_category="";

    // this is githtb test
    
    String msearch_term="";
    EditText txt_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        txt_search=(EditText) findViewById(R.id.txt_search);
// setup spin
       Spinner spin=(Spinner)findViewById(R.id.category_list);
      /*  ArrayAdapter<CharSequence> adapter=new ArrayAdapter<CharSequence>(this,R.array.categoryofproducts,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
 */       spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) parent.getSelectedView();
                msearch_category = tv.getText().toString();

                Toast.makeText(context, tv.getText(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
     //-==================
// setup appbar
        Toolbar my_toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);
        //setup_txtfield_change
        EditText search_term=(EditText)findViewById(R.id.txt_search);
        search_term.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
msearch_term=txt_search.getText().toString();
            }
        });
//setup button

        TextView search_txt_view=(TextView)findViewById(R.id.btn_Search);
        search_txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Search_Result.class);
                intent.putExtra(SEARCH_TERM,msearch_term);
                intent.putExtra(SEARCH_CATEGORY,msearch_category);
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
        }

        return super.onOptionsItemSelected(item);
    }
}