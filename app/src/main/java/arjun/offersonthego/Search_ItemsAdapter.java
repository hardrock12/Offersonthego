package arjun.offersonthego;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        TextView distancetoshop = (TextView) convertView.findViewById(R.id.navigation_distance);
        TextView tvshopname=(TextView)convertView.findViewById(R.id.tv_shop_name);
        ImageView imgthum = (ImageView) convertView.findViewById(R.id.thumbnail);
        pname.setText(result_item.product_name);
        description.setText(result_item.description);
        cost.setText("\u20B9 " + result_item.price);
      /*  if (result_item.distance != null) {
            distancetoshop.setText(result_item.distance + " m");
        }*/
        if(result_item.Distance==null||result_item.valid_distance==false) {
            distancetoshop.setText("calculating..");
        }
        else{
            distancetoshop.setText(result_item.Distance);

        }
        tvshopname.setText(result_item.shop_name);
        if (result_item.thumb != null) {
            imgthum.setImageBitmap(result_item.thumb);
        }

        return convertView;
    }
}
