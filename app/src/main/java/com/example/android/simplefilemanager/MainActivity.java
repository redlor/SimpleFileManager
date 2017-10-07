package com.example.android.simplefilemanager;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Item>> {

    private File currentDir;
    private FileArrayAdapter adapter;
    private static final int FILE_LOADER_ID = 1;
    LoaderManager loaderManager;

    public static final String LOG_TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(FILE_LOADER_ID, null, this);

    }

    private void fill(File file) {
        File [] directories = file.listFiles();
        this.setTitle("Current directory: " + file.getName());
        List<Item> dirList = new ArrayList<Item>();
        List<Item> filesList = new ArrayList<Item>();
        try{
            for (File fileCounter: directories) {
                if (fileCounter.isDirectory()) {
                    dirList.add(new Item(fileCounter.getName(), R.drawable.ic_folder_grey600_36dp));
                } else {
                    filesList.add(new Item(fileCounter.getName(), R.drawable.ic_file_grey600_36dp));
                }
            }
        } catch (Exception e) {

        }

        dirList.addAll(filesList);

        adapter = new FileArrayAdapter(this, dirList);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
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

    @Override
    public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
        return new FileLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String defaultFolder = sharedPreferences.getString(
                getString(R.string.settings_folder_key),
                getString(R.string.settings_folder_default));

     //   String categoryString = category.toString();
        switch (defaultFolder) {
            case "Documents": currentDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC);
                break;
            case "Downloads": currentDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
                break;
            case "Pictures": currentDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
                break;
            default : currentDir = new File("/sdcard/");
                break;
        }

        // currentDir = new File("/sdcard/");
        // currentDir = new File(defaultFolder);
        fill(currentDir);
    }

    @Override
    public void onLoaderReset(Loader<List<Item>> loader) {
        adapter.clear();
    }


}
