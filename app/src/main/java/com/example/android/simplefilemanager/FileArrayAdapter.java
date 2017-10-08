package com.example.android.simplefilemanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hp on 07/10/2017.
 */

public class FileArrayAdapter extends ArrayAdapter<Item> {

    Activity context;
    private List<Item> listItem;
    private SparseBooleanArray mSelectedItemsIds;


    public FileArrayAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.listItem = items;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View listItemView = convertView;
        if (listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.file_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) listItemView.findViewById(R.id.TextView01);
            viewHolder.icon = (ImageView) listItemView.findViewById(R.id.fd_Icon1);
            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) listItemView.getTag();
        }

        Item currentItem = getItem(position);

        Picasso.with(getContext()).load(currentItem.getIcon()).into(viewHolder.icon);
        viewHolder.name.setText(currentItem.getFile());
        listItemView.setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
                : Color.TRANSPARENT);


        return listItemView;
    }

    @Override
    public void add(Item item) {
        listItem.add(item);
        notifyDataSetChanged();
        Toast.makeText(context, listItem.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void remove(Item object) {
        // super.remove(object);
        listItem.remove(object);
        notifyDataSetChanged();
    }

    public List<Item> getListItem() {
        return listItem;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public static class ViewHolder {
        ImageView icon;
        TextView name;
    }
}
