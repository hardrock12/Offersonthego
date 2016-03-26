package arjun.offersonthego;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

/**
 * Created by arjun on 23/3/16.
 */
public class Filter_list_dialog extends DialogFragment {
    public Context mcontext;
    public View mrootview;

    Filter_list_dialog(Context context, View root) {
        mcontext = context;
        mrootview = root;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder nbuilder = new AlertDialog.Builder(getActivity());
        CharSequence filters[] = {"by category", "by region", "Nearby"};
        nbuilder.setTitle("Choose Filter:").setItems(filters, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        category_list_dialog categoryListDialog = new category_list_dialog(mcontext, mrootview);
                        categoryListDialog.show(getFragmentManager(), "CATEGORY");
                        break;

                    case 1:

                    case 2:
                }
            }
        });
        return nbuilder.create();
    }

}



