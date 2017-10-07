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

    private Context c;
    private int id;
    private List<Item> items;

    public FileArrayAdapter(Context context, int textViewResourceId,
                            List<Item> objects) {
        super(context, textViewResourceId, objects);
        c = context;
        id = textViewResourceId;
        items = objects;
    }

    public FileArrayAdapter(Context context, List<Item> items) {
        super(context, 0, items);
    }


   // public Item getItem(int i)
    //{return items.get(i);}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View v = convertView;
        if (v == null) {
       //    v = LayoutInflater.from(getContext()).inflate(
         //          R.layout.file_list_item, parent, false);
         v = LayoutInflater.from(getContext()).inflate(
                 R.layout.file_list_item, parent, false);

            viewHolder =  new ViewHolder();
            viewHolder.name = (TextView) v.findViewById(R.id.TextView01);
            viewHolder.icon = (ImageView) v.findViewById(R.id.fd_Icon1);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        Item currentItem = getItem(position);

        Picasso.with(getContext()).load(currentItem.getIcon()).into(viewHolder.icon);
        viewHolder.name.setText(currentItem.getFile());

      /*  final Item o = items.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.TextView01);
            t1.setText(o.getFile());

            ImageView imageView = (ImageView) v.findViewById(R.id.fd_Icon1);
            Picasso.with(getContext()).load(o.getIcon()).into(imageView);

        }*/
        return v;
    }

    public static class ViewHolder {
        ImageView icon;
        TextView name;
    }
}
