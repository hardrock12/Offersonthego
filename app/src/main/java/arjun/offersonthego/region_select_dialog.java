package arjun.offersonthego;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ARJUN on 4/3/16.
 */
public class region_select_dialog extends DialogFragment {
    EditText region_search;
    ListView lv;
public Button s;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.region_select_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.region_select_dialog, null));
        AlertDialog d= builder.create();
        lv = (ListView) d.findViewById(R.id.lv_suggest);
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), R.layout.suggestion_listview_layout);
        lv.setAdapter(adp);


        region_search = (EditText) d.findViewById(R.id.dialog_region);
        s = (Button) d.findViewById(R.id.btn_show_suggest);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = region_search.getText().toString();
                Log.i("ootg", str + " recieved");
                ArrayList<String> arrayList = new ArrayList<String>();
                Geocoder geocoder = new Geocoder(getActivity());
                try {
                    List<Address> addressList = geocoder.getFromLocationName(str, 10);
                    for (int i = 0; i < addressList.size(); i++) {
                        String address = "";
                        int len = addressList.get(i).getMaxAddressLineIndex();
                        for (int j = 0; j < len; j++) {
                            address = address + "," + addressList.get(i).getAddressLine(j);


                        }
                        address = address + addressList.get(i).getLocality();
                        Log.i("ootg", address);
                        arrayList.add(address);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                adp.clear();
                adp.addAll(arrayList);
                adp.notifyDataSetChanged();


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
return d;
    }
}
