package com.example.android.simplefilemanager;

import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hp on 07/10/2017.
 */

public class FileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Item>> {

    public static final String LOG_TAG = FileActivity.class.getName();
    private static final int FILE_LOADER_ID = 1;
    private static final String STATE_TASK_RUNNING = "taskRunning";
    FileArrayAdapter adapter;
    LoaderManager loaderManager;
    boolean testDelete = false;
    ActionMode mActionMode;
    private File currentDir;
    private Context mContext;
    boolean loaderReset =  true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
if (loaderReset) {

    loaderManager = getLoaderManager();
    loaderManager.initLoader(FILE_LOADER_ID, null, this);

}
    }


    private void fill(final File file) {
        File[] directories = file.listFiles();
        this.setTitle("Current directory: " + file.getName());
        List<Item> dirList = new ArrayList<Item>();
        List<Item> filesList = new ArrayList<Item>();
        try {
            for (File fileCounter : directories) {
                if (fileCounter.isDirectory()) {
                    dirList.add(new Item(fileCounter.getName(), R.drawable.ic_folder_close, fileCounter.getAbsolutePath()));
                } else {
                    filesList.add(new Item(fileCounter.getName(), R.drawable.ic_file, fileCounter.getAbsolutePath()));
                }
            }
        } catch (Exception e) {

        }

        dirList.addAll(filesList);
        if (!file.getName().equalsIgnoreCase("sdcard")) {
            dirList.add(0, new Item("..", R.drawable.ic_folder_open, file.getParent()));
        }
        adapter = new FileArrayAdapter(this, dirList);
        final ListView listView = (ListView) findViewById(R.id.list);
        GridView gridView = (GridView) findViewById(R.id.grid);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(4000);
                    view.startAnimation(animation1);
                    Item currentItem = adapter.getItem(position);
                    if (currentItem.getIcon() == R.drawable.ic_folder_close || currentItem.getIcon() == R.drawable.ic_folder_open) {
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
                        startActivity(intent);
                    }
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    onListItemSelect(position);
                    return true;
                }
            });

        } else {
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(4000);
                    view.startAnimation(animation1);
                    Item currentItem = adapter.getItem(position);
                    if (currentItem.getIcon() == R.drawable.ic_folder_close || currentItem.getIcon() == R.drawable.ic_folder_open) {
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
                        startActivity(intent);
                    }
                }
            });
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    onListItemSelect(position);
                    return true;
                }
            });
        }

    }

    private void onListItemSelect(int position) {
        adapter.toggleSelection(position);
        boolean hasCheckedItems = adapter.getSelectedCount() > 0;

        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode = startActionMode(new ActionBarCallBack());
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null)
            mActionMode.setTitle(String.valueOf(adapter
                    .getSelectedCount()) + " selected");
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
                fill(currentDir);
                return true;
            case R.id.action_change_default_folder:
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

        switch (defaultFolder) {
            case "Root":
                currentDir = new File("/sdcard/");
                break;
            case "Music":
                currentDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MUSIC);
                break;
            case "Downloads":
                currentDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);
                break;
            case "Pictures":
                currentDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                break;
            case "Movies":
                currentDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MOVIES);
                break;
            default:
                currentDir = new File("/sdcard/");
                break;
        }

        // currentDir = new File("/sdcard/");
        // currentDir = new File(defaultFolder);
        fill(currentDir);
        loaderReset = false;
    }

    @Override
    public void onLoaderReset(Loader<List<Item>> loader) {
        adapter.clear();
    }

    public boolean dialogMessage(final Context context) {
        this.mContext = context;
        final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Delete file");
        alert.setMessage("Are you sure you want to delete this file?");
        alert.setCancelable(false);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                testDelete = true;
                Log.e(LOG_TAG, Boolean.toString(testDelete));
                deleteItemTest();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        alert.create();
        alert.show();
        return testDelete;
    }

    private void deleteItemTest() {

        mActionMode = startActionMode(new ActionBarCallBack());
        mActionMode.finish();


    }

    private class ActionBarCallBack implements ActionMode.Callback {


        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // inflate contextual menu
            mode.getMenuInflater().inflate(R.menu.context_main, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_delete:
                    // AlertDialog diaBox = AskOption();
                    // diaBox.show();
                    mContext = FileActivity.this;
                    dialogMessage(mContext);

                    Log.e(LOG_TAG, Boolean.toString(testDelete));


                    //  mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // remove selection
            Log.e(LOG_TAG, Boolean.toString(testDelete));
            if (testDelete) {

                // retrieve selected items and delete them out
                SparseBooleanArray selected = adapter
                        .getSelectedIds();
                System.out.println(selected.toString());

                for (int i = (selected.size() - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        Item selectedItem = adapter
                                .getItem(selected.keyAt(i));
                        adapter.remove(selectedItem);
                        String filePath = selectedItem.getPath();
                        File fileDelete = new File(filePath);
                        fileDelete.delete();
                        Log.e(LOG_TAG, fileDelete.toString());
                    }
                }
            }
            testDelete = false;
            adapter.removeSelection();
            mActionMode = null;
        }
    }
}