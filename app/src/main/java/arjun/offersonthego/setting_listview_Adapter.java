package arjun.offersonthego;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ARJUN on 4/21/16.
 */
public class setting_listview_Adapter extends ArrayAdapter<settings_model> {
    public setting_listview_Adapter(Context context, ArrayList<settings_model> items) {

        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        settings_model result_item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.setting_listview, parent, false);

        }
        TextView setname = (TextView) convertView.findViewById(R.id.setting_name);
        TextView setvalue = (TextView) convertView.findViewById(R.id.setting_value);
        setname.setText(result_item.set_name);
        setvalue.setText(result_item.set_value);


        return convertView;
    }
}
