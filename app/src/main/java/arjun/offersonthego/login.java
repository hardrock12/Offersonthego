package arjun.offersonthego;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class login extends AppCompatActivity {

    public final  static  String LOGIN_NAME="arjun.login.NAME";
    public final  static  String LOGIN_PASSWD="arjun.login.PASSWD";

    Context context;
    public String mlogin_name="";
    public String mlogin_passwd="";
    public  EditText username;
    public EditText password;
    public TextView t;
   // public Button log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        username=(EditText) findViewById(R.id.uid);
        password=(EditText) findViewById(R.id.passwd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //log = (Button) findViewById(R.id.logInButton);
        //msearch_term=txt_search.getText().toString();
        mlogin_name = username.getText().toString();
        mlogin_passwd = password.getText().toString();
        setSupportActionBar(toolbar);
        /*
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,sellerHome.class);
                intent.putExtra(LOGIN_NAME,mlogin_name);
                intent.putExtra(LOGIN_PASSWD, mlogin_passwd);
                Log.i("ootg", mlogin_name+ ":" + mlogin_passwd);
                startActivity(intent);
            }
        });
       */
        TextView logButton=(TextView)findViewById(R.id.logInButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mlogin_name = username.getText().toString();
                mlogin_passwd = password.getText().toString();
                //t = (TextView)findViewById(R.id.testview);
               // t.setText(mlogin_name);
                Intent intent = new Intent(context, sellerOptions.class);
                intent.putExtra(LOGIN_NAME, mlogin_name);
                intent.putExtra(LOGIN_PASSWD, mlogin_passwd);
                Log.i("ootg", mlogin_name + ":" + mlogin_passwd);
                startActivity(intent);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
