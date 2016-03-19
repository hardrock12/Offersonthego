package arjun.offersonthego;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by arjun on 19/3/16.
 */
public class Search_ItemsAdapter extends ArrayAdapter<Search_Results_Model> {
    public Search_ItemsAdapter(Context context, ArrayList<Search_Results_Model> items) {

        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Search_Results_Model result_item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.searchactivity_listview, parent, false);

        }
        TextView pname = (TextView) convertView.findViewById(R.id.lv_pname);
        TextView description = (TextView) convertView.findViewById(R.id.lv_description);
        TextView cost = (TextView) convertView.findViewById(R.id.lv_price);
        pname.setText(result_item.product_name);
        description.setText(result_item.description);
        cost.setText(result_item.price);

        return convertView;
    }
}
