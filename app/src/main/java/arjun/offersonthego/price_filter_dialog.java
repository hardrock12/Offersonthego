package arjun.offersonthego;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ARJUN on 4/15/16.
 */
public class price_filter_dialog extends DialogFragment {
    public Context mcontext;
    public View mrootview;
    public Runnable u;

    price_filter_dialog(Context context, View rootview, Runnable u) {

        mcontext = context;
        mrootview = rootview;
        this.u = u;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder nbuilder = new AlertDialog.Builder(getActivity());
        // CharSequence filters[] = {"by category", "by region", "by "};

        nbuilder.setTitle("Choose Filter:").setSingleChoiceItems(R.array.price_filter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String price_ranges[] = getResources().getStringArray(R.array.price_filter);
                EditText tv_lo, tv_up;
                TextView errr;

                double ulimit, llimit;
                ulimit = Double.parseDouble(price_ranges[which].substring(6));
                llimit = 0.0;

                ArrayList<Search_Results_Model> elements = Search_Result.stored_search_results;
                for (int h = 0; h < elements.size(); h++) {

                    if (elements.get(h).priceinrs < llimit || elements.get(h).priceinrs > ulimit) {
                        elements.remove(h);
                        h--;

                    }

                }


                ListView lv = (ListView) mrootview.findViewById(R.id.lv_search_results);


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
                dialog.dismiss();

            }
        });
        return nbuilder.create();
    }

}

