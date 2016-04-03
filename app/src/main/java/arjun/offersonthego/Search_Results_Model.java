package arjun.offersonthego;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ARJUN on 4/2/16.
 */
class Search_Results_Model {
    public String product_name;
    public String category;
    public String description;
    public String price;
    public String Avail;
    public String shopid;
    public String productid;
    public String shop_lat;
    public String shop_lngi;
    public String shop_name;
    public String Distance;
public boolean valid_distance=false;
    public double distanceinm=0;
    public double priceinrs=0;
    public static Search_Results_Model fromJson(JSONObject jsonObject) {
        Search_Results_Model model = new Search_Results_Model();
        try {

            model.product_name = jsonObject.getString("Product_name");
            model.category = jsonObject.getString("catgory");
            model.productid = jsonObject.getString("product_id");
            model.shopid = jsonObject.getString("shopid");
            model.description = jsonObject.getString("description");
            model.price = jsonObject.getString("price");
            model.priceinrs = Double.parseDouble(jsonObject.getString("price"));
            model.Avail = jsonObject.getString("availability");
            if (jsonObject.has("shop_lat")) {
                model.shop_lat = jsonObject.getString("shop_lat");
            } else {
                model.shop_lat = "";
            }
            if (jsonObject.has("shop_lon")) {
                model.shop_lngi = jsonObject.getString("shop_lon");
            } else {
                model.shop_lat = "";
            }
            model.shop_name = jsonObject.getString("shop_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return model;
    }

    public static ArrayList<Search_Results_Model> fromJson(JSONArray jsonArray) {
        ArrayList<Search_Results_Model> models = new ArrayList<Search_Results_Model>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json_of_one_model = null;

            try {
                json_of_one_model = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Search_Results_Model model = Search_Results_Model.fromJson(json_of_one_model);
            models.add(model);


        }


        return models;

    }

}
