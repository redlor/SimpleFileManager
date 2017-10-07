package com.example.android.simplefilemanager;

/**
 * Created by Hp on 07/10/2017.
 */

public class Item {
    public String file;
    public int icon;

    public Item(String file, Integer icon) {
        this.file = file;
        this.icon = icon;
    }

    public String getFile() {
        return file;
    }

    public int getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return file;
    }
}
