package com.example.android.simplefilemanager;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hp on 07/10/2017.
 */

public class FileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Item>>{

    private File currentDir;
    private FileArrayAdapter adapter;
    private static final int FILE_LOADER_ID = 1;
    LoaderManager loaderManager;

    public static final String LOG_TAG = FileActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(FILE_LOADER_ID, null, this);

    }

    private void fill(File file) {
        File[] directories = file.listFiles();
        this.setTitle("Current directory: " + file.getName());
        List<Item> dirList = new ArrayList<Item>();
        List<Item> filesList = new ArrayList<Item>();
        try {
            for (File fileCounter : directories) {
                if (fileCounter.isDirectory()) {
                    dirList.add(new Item(fileCounter.getName(), R.drawable.ic_folder_grey600_36dp, fileCounter.getAbsolutePath()));
                } else {
                    filesList.add(new Item(fileCounter.getName(), R.drawable.ic_file_grey600_36dp, fileCounter.getAbsolutePath()));
                }
            }
        } catch (Exception e) {

        }

        dirList.addAll(filesList);
        if (!file.getName().equalsIgnoreCase("sdcard")) {
            dirList.add(0, new Item("..", R.drawable.ic_refresh, file.getParent()));
        }
        adapter = new FileArrayAdapter(this, dirList);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item currentItem = adapter.getItem(position);
                if (currentItem.getIcon() == R.drawable.ic_folder_grey600_36dp || currentItem.getIcon() == R.drawable.ic_refresh) {
                    currentDir = new File(currentItem.getPath());
                    fill(currentDir);
                } else {
                    File file = new File(currentItem.getPath());
                    MimeTypeMap map = MimeTypeMap.getSingleton();
                    String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
                    String type = map.getMimeTypeFromExtension(ext);
                    if (type == null)
                        type = "*/*";

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.fromFile(file);
                    intent.setDataAndType(data, type);
                    //intent.putExtra("GetPath",currentDir.toString());
                   // intent.putExtra("GetFileName",currentItem.getFile());
                    //setResult(RESULT_OK, intent);
                    startActivity(intent);
            }
            }
        });

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
