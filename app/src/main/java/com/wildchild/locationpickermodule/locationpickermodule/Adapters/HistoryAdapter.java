package com.wildchild.locationpickermodule.locationpickermodule.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.wildchild.locationpickermodule.R;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.History;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {

    private List<History> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public HistoryAdapter(Context aContext, List<History> listData) {
        this.context = aContext;
        this.listData = listData;
        //layoutInflater = LayoutInflater.from(aContext);

        this.layoutInflater = (LayoutInflater) aContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        System.out.println("IS RENDERING VIEW");
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.history_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.subtitle = (TextView) convertView.findViewById(R.id.subtitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        History history = this.listData.get(position);
        holder.title.setText(history.getPlace());
        holder.subtitle.setText("Date: " + history.getCreatedAt());

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView subtitle;
    }

}
