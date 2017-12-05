package ee382apt.thread;

/**
 * Created by caiju on 12/4/2017.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter{
    private Context mContext;
    private List<String> listLocations;
    private List<String> titles;
    public ListAdapter(Context context, List<String> titles, List<String> list){
        mContext = context;
        listLocations = list;
        this.titles = titles;
    }

    @Override
    public int getCount(){
        return listLocations.size();
    }

    @Override
    public Object getItem(int position){
        return listLocations.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = View.inflate(mContext, R.layout.list_item, null);
        TextView title = (TextView)v.findViewById(R.id.str_title);
        TextView location = (TextView)v.findViewById(R.id.str_location);
        if(titles.size() == 0){
            title.setText("No events found");
        }

        title.setText(titles.get(position));
        location.setText(listLocations.get(position));

        return v;
    }
}