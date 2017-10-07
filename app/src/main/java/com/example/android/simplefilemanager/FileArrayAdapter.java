package com.example.android.simplefilemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hp on 07/10/2017.
 */

public class FileArrayAdapter extends ArrayAdapter<Item> {



    public FileArrayAdapter(Context context, List<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View listItemView = convertView;
        if (listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(
                 R.layout.file_list_item, parent, false);

            viewHolder =  new ViewHolder();
            viewHolder.name = (TextView) listItemView.findViewById(R.id.TextView01);
            viewHolder.icon = (ImageView) listItemView.findViewById(R.id.fd_Icon1);
            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) listItemView.getTag();
        }

        Item currentItem = getItem(position);

        Picasso.with(getContext()).load(currentItem.getIcon()).into(viewHolder.icon);
        viewHolder.name.setText(currentItem.getFile());

        return listItemView;
    }

    public static class ViewHolder {
        ImageView icon;
        TextView name;
    }
}
