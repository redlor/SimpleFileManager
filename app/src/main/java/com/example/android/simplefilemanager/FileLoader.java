package com.example.android.simplefilemanager;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hp on 07/10/2017.
 */

public class FileLoader extends AsyncTaskLoader<List<Item>>{

    public FileLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Item> loadInBackground() {
        List<Item> list = new ArrayList<Item>();

        String name = "";
        int icon = 0;
        String path = "";
        Item currentItem = new Item(name, icon, path);
        currentItem.getFile();
        currentItem.getIcon();


        list.add(currentItem);
        return list;
    }
}
