package com.example.cortex.kwark;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cortex.kwark.DAL.DaoFactory;
import com.example.cortex.kwark.domain.Routine;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

/**
 * List overview activity of all stored routines.
 * Can initiate routine CRUDs.
 *
 * Created by cortex on 03/05/2015.
 */
public class Routines extends Activity {
    private static final String KEY_ROUTINE = "Routine";
    private static final int ACTIVITY_EDIT_ROUTINE = 0;
    private static final int HOME_MENU_ID = Menu.FIRST;
    private static final int ADD_ROUTINE_MENU_ID = Menu.FIRST + 1;
    private static final int EDIT_ROUTINE_CONTEXT_MENU_ID = Menu.FIRST + 2;
    private static final int DELETE_ROUTINE_CONTEXT_MENU_ID = Menu.FIRST + 3;

    private ListView routineListView;
    private DaoFactory daoFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routines);
        daoFactory = new DaoFactory(this);
        routineListView = (ListView) findViewById(R.id.routineListView);
        registerForContextMenu(routineListView);
        addOnClickListener();
        updateGUI();
    }

    private void updateGUI() {
        // Get and display all routines from db to list.
        RuntimeExceptionDao<Routine, Integer> dao = daoFactory.getRoutineRuntimeExceptionDao();
        List<Routine> routines = dao.queryForAll();
        final ArrayAdapter<Routine> adapter = new ArrayAdapter<Routine>(this,
                android.R.layout.simple_list_item_1, routines);
        routineListView.setAdapter(adapter);
        // Display routine count.
        TextView countText = (TextView) findViewById(R.id.countLabel);
        int count = (int) dao.countOf();
        countText.setText("(" + count + ")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, HOME_MENU_ID, 0, R.string.home);
        menu.add(0, ADD_ROUTINE_MENU_ID, 0, R.string.add_routine);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case HOME_MENU_ID:
                goHome();
                break;
            case ADD_ROUTINE_MENU_ID:
                addRoutine();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void goHome() {
        Intent i= new Intent(this, Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ROUTINE_CONTEXT_MENU_ID, 0, R.string.edit_routine);
        menu.add(0, DELETE_ROUTINE_CONTEXT_MENU_ID, 0, R.string.delete_routine);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Fetch the real Routine entity from given menu item
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Routine routine = (Routine)routineListView.getAdapter().getItem(info.position);
        switch(item.getItemId()) {
            case EDIT_ROUTINE_CONTEXT_MENU_ID:
                editRoutine(routine);
                return true;
            case DELETE_ROUTINE_CONTEXT_MENU_ID:
                deleteRoutine(routine);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenHelperManager.releaseHelper();
    }

    private void addRoutine() {
        editRoutine(new Routine());
    }

    private void editRoutine(Routine routine) {
        Intent i = new Intent(this, RoutineEdit.class);
        i.putExtra(KEY_ROUTINE, routine);
        startActivityForResult(i, ACTIVITY_EDIT_ROUTINE);
    }

    private void deleteRoutine(final Routine routine) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        RuntimeExceptionDao<Routine, Integer> dao = daoFactory.getRoutineRuntimeExceptionDao();
                        dao.delete(routine);
                        updateGUI();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.addRoutineButton:
                addRoutine();
                return;
        }
    }

    private void addOnClickListener() {
        routineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Routine routine = (Routine)routineListView.getAdapter().getItem(position);
                editRoutine(routine);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        updateGUI();
    }
}