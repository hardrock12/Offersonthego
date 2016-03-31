package arjun.offersonthego;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by arjun on 23/3/16.
 */
public class category_list_dialog extends DialogFragment {
    public Context mcontext;
    public View mrootview;


    category_list_dialog(Context context, View rootview) {
        mcontext = context;
        mrootview = rootview;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder nbuilder = new AlertDialog.Builder(getActivity());
        // CharSequence filters[] = {"by category", "by region", "by "};
        nbuilder.setTitle("Choose Category Filter:").setSingleChoiceItems(R.array.categoryofproducts, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categories[] = getResources().getStringArray(R.array.categoryofproducts);
                searchtask phpquery = new searchtask(mrootview, mcontext);
                phpquery.execute(Search_Result.SEARCH_PHP_SCRIPT + "searchterm=" + Search_Result.SEARCH_TERM + "&searchcategory=" + categories[which] + "&lat=" + Search_Result.CURRENT_LAT + "&long=" + Search_Result.CURRENT_LONG);

                dialog.dismiss();

            }
        });
        return nbuilder.create();
    }

}


