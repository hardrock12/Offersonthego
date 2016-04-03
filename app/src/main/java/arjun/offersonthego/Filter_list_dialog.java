package arjun.offersonthego;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by arjun on 23/3/16.
 */
public class Filter_list_dialog extends DialogFragment {
    public Context mcontext;
    public View mrootview;
    public Runnable u;

    Filter_list_dialog(Context context, View root, Runnable u) {
        super();
        mcontext = context;
        mrootview = root;
        this.u = u;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder nbuilder = new AlertDialog.Builder(getActivity());
        CharSequence filters[] = {"by category", "by  Price"};
        nbuilder.setTitle("Choose Filter:").setItems(filters, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        category_list_dialog categoryListDialog = new category_list_dialog(mcontext, mrootview, u);
                        categoryListDialog.show(getFragmentManager(), "CATEGORY");

                        break;



                    case 1:
                       select_price_range_dialog price_range_dialog=new select_price_range_dialog(mcontext,mrootview);
                        price_range_dialog.show();

break;
                }
            }
        });
        return nbuilder.create();
    }

}




