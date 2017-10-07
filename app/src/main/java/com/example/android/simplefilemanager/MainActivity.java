package com.example.android.simplefilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private File currentDir;
    private FileArrayAdapter adapter;

    public static final String LOG_TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentDir = new File("/sdcard/");
        fill(currentDir);


/*        try {
            path.mkdirs();
        } catch (SecurityException e) {
            Log.e(LOG_TAG, "unable to write on the sd card ");
        }


Log.e(LOG_TAG, path.toString());

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                File sel = new File(dir, filename);
                return (sel.isFile() || sel.isDirectory()) && !sel.isHidden();
            }
        };

        String[] fList = path.list(filter);
        fileList = new Item[fList.length];
        for (int i = 0; i < fList.length; i++) {
            fileList[i] = new Item(fList[i], R.drawable.ic_file_grey600_36dp);

            //convert into file path
            File sel = new File(path, fList[i]);

            // Set drawables
            if (sel.isDirectory()) {
                fileList[i].icon = R.drawable.ic_folder_grey600_36dp;
                Log.d("DIRECTORY", fileList[i].file);
            } else {
                Log.d("FILE", fileList[i].file);
            }
        }

        adapter = new ArrayAdapter<Item>(this, android.R.layout.select_dialog_item,
                android.R.id.text1, fileList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // creates view
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view
                        .findViewById(android.R.id.text1);

                // put the image on the text view
                textView.setCompoundDrawablesWithIntrinsicBounds(
                        fileList[position].icon, 0, 0, 0);

                return view;
            }
        };*/
    }

    private void fill(File f) {
        File [] dirs = f.listFiles();
        this.setTitle("Current directory: " + f.getName());
        List<Item> dir = new ArrayList<Item>();
        List<Item> fls = new ArrayList<Item>();
        try{
            for (File ff: dirs) {
                if (ff.isDirectory()) {
                    File[] fBuf = ff.listFiles();
                    int buf = 0;
                    if (fBuf != null) {
                        buf = fBuf.length;
                    } else {
                        buf = 0;
                    }
                    String numItem = String.valueOf(buf);
                    if (buf == 0) numItem =  numItem + " item";
                    else numItem = numItem + " items";

                    dir.add(new Item(ff.getName(), R.drawable.ic_folder_grey600_36dp));
                } else {
                    fls.add(new Item(ff.getName(), R.drawable.ic_file_grey600_36dp));
                }
            }
        } catch (Exception e) {

        }

        dir.addAll(fls);

       // if(!f.getName().equalsIgnoreCase("sdcard"))
         //   dir.add(0,new Item("..","Parent Directory","",f.getParent(),"directory_up"));
     //   adapter = new FileArrayAdapter(MainActivity.this,R.layout.activity_main,dir);
       // this.setListAdapter(adapter);
        adapter = new FileArrayAdapter(this, dir);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }
    public List<File> folderSearchBT(File src, String folder) throws FileNotFoundException {


        List<File> result = new ArrayList<File>();

        File[] filesAndDirs = src.listFiles();
        List<File> filesDirs = Arrays.asList(filesAndDirs);
        for (File file : filesDirs) {
            result.add(file); // always add, even if directory
            if (!file.isFile()) {
                List<File> deeperList = folderSearchBT(file, folder);
                result.addAll(deeperList);
            }
        }
        return result;
    }

    public String searchForBluetoothFolder() {

        String splitchar = "/";
        File root = Environment.getExternalStorageDirectory();
        List<File> btFolder = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String bt = sharedPreferences.getString(
                getString(R.string.settings_folder_key),
                getString(R.string.settings_folder_default));
       // String bt = "bluetooth";
        try {
            btFolder = folderSearchBT(root, bt);
        } catch (FileNotFoundException e) {
            Log.e("FILE: ", e.getMessage());
        }

        for (int i = 0; i < btFolder.size(); i++) {

            String g = btFolder.get(i).toString();

            String[] subf = g.split(splitchar);

            String s = subf[subf.length - 1].toUpperCase();

            boolean equals = s.equalsIgnoreCase(bt);

            if (equals)
                return g;
        }
        return null; // not found
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_folder, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                // TO DO
                return true;
            case R.id.action_change_default_folder:
              /*  Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("");
                startActivity(intent);*/
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

/*    @Override
    public Loader<List<File>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String defaultFolder = sharedPreferences.getString(
                getString(R.string.settings_folder_key),
                getString(R.string.settings_folder_default));

Log.e("MainActivity", defaultFolder);


        return new ;
    }



    @Override
    public void onLoadFinished(Loader<List<File>> loader, List<File> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<File>> loader) {

    }*/
}
