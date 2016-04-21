package arjun.offersonthego;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    ArrayList<settings_model> arr;
    Context mcontext;
    EditText ed;
    Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = this;
        setContentView(R.layout.activity_settings);
        arr = new ArrayList<settings_model>();
        SharedPreferences s = getSharedPreferences("settings_prefs", MODE_PRIVATE);
        arr.add(new settings_model("Nearby Search Radius", String.valueOf(s.getFloat("nearby_range", 5.0f))));
        arr.add(new settings_model("Username", String.valueOf(s.getString("name", "Anonymous"))));
        setting_listview_Adapter adapter = new setting_listview_Adapter(this, arr);
        ListView lv = (ListView) findViewById(R.id.settings_list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                d = new Dialog(mcontext);
                d.setContentView(R.layout.general_setting_dialog);
                TextView tx = (TextView) d.findViewById(R.id.setting_header);
                ed = (EditText) d.findViewById(R.id.etxt_setting_value);
                //TextView tx=(TextView)d.findViewById(R.id.setting_header);
                Button btn = (Button) d.findViewById(R.id.setting_save);
                tx.setText(arr.get(position).set_name + "?");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences s = getSharedPreferences("settings_prefs", MODE_PRIVATE);
                        if (position == 0) {
                            float val = Float.parseFloat(ed.getText().toString());
                            SharedPreferences.Editor editor = s.edit();
                            editor.putFloat("nearby_Range", val);


                        } else if (position == 1) {


                            SharedPreferences.Editor editor = s.edit();
                            editor.putString("name", ed.getText().toString());


                        }
                        d.dismiss();
                    }
                });

                d.show();

            }
        });


    }
}
