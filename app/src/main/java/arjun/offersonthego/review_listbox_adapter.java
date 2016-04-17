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
 * Created by ARJUN on 4/16/16.
 */
public class review_listbox_adapter extends ArrayAdapter<review_model> {
    review_listbox_adapter(Context mc, ArrayList<review_model> arraylist) {


        super(mc, 0, arraylist);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        review_model result_item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rating_and_review_listview, parent, false);

        }
        TextView username = (TextView) convertView.findViewById(R.id.rating_username);
        TextView rev = (TextView) convertView.findViewById(R.id.rating_review);
        TextView rat = (TextView) convertView.findViewById(R.id.rating_rating);
        username.setText(result_item.username);
        rev.setText(result_item.review);
        rat.setText(result_item.rating);

        return convertView;
    }
}
