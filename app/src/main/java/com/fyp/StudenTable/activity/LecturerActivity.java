package com.fyp.StudenTable.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.fyp.StudenTable.R;
import com.fyp.StudenTable.adapter.LecturerAdapter;
import com.fyp.StudenTable.layout.Lecturer;
import com.fyp.StudenTable.misc.SetupDialog;
import com.fyp.StudenTable.misc.DbSupport;

import java.util.ArrayList;
import java.util.Objects;


public class LecturerActivity extends AppCompatActivity {

    private final Context context = this;
    private ListView listView;
    private DbSupport db;
    private LecturerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer);
        initAll();
    }

    private void initAll() {
        setupAdapter();
        setupListViewMultiSelect();
        setupCustomDialog();
    }

    private void setupAdapter() {
        db = new DbSupport(context);
        listView = findViewById(R.id.teacherlist);
        adapter = new LecturerAdapter(LecturerActivity.this, listView, R.layout.listview_lecturer, db.getLecturer());
        listView.setAdapter(adapter);
    }

    private void setupListViewMultiSelect() {
        final CoordinatorLayout coordinatorLayout = findViewById(R.id.LecturerTab);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = listView.getCheckedItemCount();
                mode.setTitle(checkedCount + " " + getResources().getString(R.string.selected));
                if(checkedCount == 0) mode.finish();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.toolbar_action_mode, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.action_delete) {
                    ArrayList<Lecturer> removelist = new ArrayList<>();
                    SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                    for (int i = 0; i < checkedItems.size(); i++) {
                        int key = checkedItems.keyAt(i);
                        if (checkedItems.get(key)) {
                            db.deleteLecturerById(Objects.requireNonNull(adapter.getItem(key)));
                            removelist.add(adapter.getLecturerList().get(key));
                        }
                    }
                    adapter.getLecturerList().removeAll(removelist);
                    db.updateLecturer(adapter.getLecturer());
                    adapter.notifyDataSetChanged();
                    mode.finish();
                    return true;
                }
                return false;
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
    }

    private void setupCustomDialog() {
        final View alertLayout = getLayoutInflater().inflate(R.layout.dialog_lecturer, null);
        SetupDialog.getAddTeacherDialog(LecturerActivity.this, alertLayout, adapter);
    }
}
