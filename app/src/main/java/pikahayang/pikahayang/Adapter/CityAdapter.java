package pikahayang.pikahayang.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pikahayang.pikahayang.Model.CityModel;
import pikahayang.pikahayang.Model.SizeModel;
import pikahayang.pikahayang.R;


public class CityAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<CityModel> item;

    public CityAdapter(Activity activity, List<CityModel> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_city, null);

        TextView city = (TextView) convertView.findViewById(R.id.tvCity);

        CityModel data;
        data = item.get(position);

        city.setText(data.getCity_name());
        String city_id = data.getCity_id();
        String city_name = data.getCity_name();

        Intent intent = new Intent("broadcast");
        intent.putExtra("city_id", String.valueOf(city_id));
        intent.putExtra("city_name", String.valueOf(city_name));
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);


        return convertView;
    }
}
