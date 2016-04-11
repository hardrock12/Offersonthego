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
public class Seller_Items_Adapter extends ArrayAdapter<Seller_Products_Results_Model> {
    public Seller_Items_Adapter(Context context, ArrayList<Seller_Products_Results_Model> items) {

        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Seller_Products_Results_Model result_item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.seller_prodcuts_listview, parent, false);

        }
        TextView product_id = (TextView) convertView.findViewById(R.id.list_pro_id);
        TextView pname = (TextView) convertView.findViewById(R.id.list_pro_name);
       // TextView description = (TextView) convertView.findViewById(R.id.lv_description);
        TextView cost = (TextView) convertView.findViewById(R.id.list_pro_price);
       // TextView distancetoshop = (TextView) convertView.findViewById(R.id.navigation_distance);
       // TextView tvshopname=(TextView)convertView.findViewById(R.id.tv_shop_name);
        //ImageView imgthum = (ImageView) convertView.findViewById(R.id.thumbnail);
        pname.setText(result_item.product_name);
      //  description.setText(result_item.description);
        cost.setText("\u20B9 " + result_item.price);
      /*  if (result_item.distance != null) {
            distancetoshop.setText(result_item.distance + " m");
        }*/
        product_id.setText(result_item.productid);

        return convertView;
    }
}
