package pikahayang.pikahayang.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pikahayang.pikahayang.Model.SizeModel;
import pikahayang.pikahayang.R;


public class SizeAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<SizeModel> item;

    public SizeAdapter(Activity activity, List<SizeModel> item) {
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
            convertView = inflater.inflate(R.layout.list_size, null);

        TextView size = (TextView) convertView.findViewById(R.id.tvSize);

        SizeModel data;
        data = item.get(position);

        size.setText(data.getSize());

        return convertView;
    }
}
