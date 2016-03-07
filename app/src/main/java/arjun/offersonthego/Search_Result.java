package arjun.offersonthego;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class Search_Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        Toast.makeText(this,intent.getStringExtra(MainActivity.SEARCH_TERM),Toast.LENGTH_LONG).show();
        Toast.makeText(this,intent.getStringExtra(MainActivity.SEARCH_CATEGORY),Toast.LENGTH_LONG).show();


                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
