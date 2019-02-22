package tech.ducletran.travelgallery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import tech.ducletran.travelgallery.R;

import java.util.ArrayList;

public class CityCountryItemAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private Context context;

    public CityCountryItemAdapter(Context context, ArrayList<String> list) {
        this.list = list;
        this.context = context;
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_city_country,parent,false);

        TextView itemName = convertView.findViewById(R.id.city_country_item_text_view);
        itemName.setText(list.get(position));

        return convertView;
    }

}
