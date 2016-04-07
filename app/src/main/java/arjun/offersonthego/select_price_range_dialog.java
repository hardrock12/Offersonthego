package arjun.offersonthego;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by ARJUN on 4/3/16.
 */
public class select_price_range_dialog extends Dialog implements
        android.view.View.OnClickListener {
    Context context;
    View mroot;

    select_price_range_dialog(Context ctx, View mrootview) {
        super(ctx);
        context = ctx;
        mroot = mrootview;


    }


    public Button filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.price_range);
        filter = (Button) findViewById(R.id.btn_yes);
        filter.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                EditText tv_lo, tv_up;
                TextView errr;
                tv_lo = (EditText) findViewById(R.id.from_price);
                tv_up = (EditText) findViewById(R.id.toprice);
                errr = (TextView) findViewById(R.id.tverrr);
                if (tv_lo.getText().toString() == "" || tv_up.getText().toString() == "") {
                    errr.setText("Please fill the boxes!");

                    return;

                }
                double ulimit, llimit;
                ulimit = Double.parseDouble(tv_up.getText().toString());
                llimit = Double.parseDouble(tv_lo.getText().toString());

                ArrayList<Search_Results_Model> elements = Search_Result.stored_search_results;
                for (int h = 0; h < elements.size(); h++) {

                    if (elements.get(h).priceinrs < llimit || elements.get(h).priceinrs > ulimit) {
                        elements.remove(h);
                        h--;

                    }

                }


                ListView lv = (ListView) mroot.findViewById(R.id.lv_search_results);


                ArrayAdapter<Search_Results_Model> adapter1 = (Search_ItemsAdapter) lv.getAdapter();


                //sort according to price


                for (int i = 0; i < elements.size() - 1; i++) {
                    int min = i;
                    Search_Results_Model temp;
                    for (int j = i + 1; j < elements.size(); j++) {
                        if (elements.get(min).priceinrs > elements.get(j).priceinrs) {
                            min = j;

                        }

                    }

                    temp = elements.get(i);
                    elements.set(i, elements.get(min));
                    elements.set(min, temp);
                }

                    adapter1.clear();
                    adapter1.addAll(elements);
                    adapter1.notifyDataSetChanged();


                dismiss();
        }
    }
}