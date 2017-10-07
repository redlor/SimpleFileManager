package com.example.android.simplefilemanager;

/**
 * Created by Hp on 07/10/2017.
 */

public class Item {
    public String file;
    private String path;
    public int icon;

    public Item(String file, Integer icon, String path) {
        this.file = file;
        this.icon = icon;
        this.path = path;
    }

    public String getFile() {
        return file;
    }

    public int getIcon() {
        return icon;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return file;
    }
}
