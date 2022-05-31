package com.fyp.StudenTable.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.fyp.StudenTable.adapter.AssignmentAdapter;
import com.fyp.StudenTable.layout.Assignment;
import com.fyp.StudenTable.R;
import com.fyp.StudenTable.misc.SetupDialog;
import com.fyp.StudenTable.misc.DbSupport;
import java.util.ArrayList;
import java.util.Objects;


public class AssignmentActivity extends AppCompatActivity {

    private final Context context = this;
    private ListView listView;
    private AssignmentAdapter adapter;
    private DbSupport db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        initAll();
    }

    private void initAll() {
        setupAdapter();
        setupListViewMultiSelect();
        setupCustomDialog();
    }

    private void setupAdapter() {
        db = new DbSupport(context);
        listView = findViewById(R.id.assignmentlist);
        adapter = new AssignmentAdapter(AssignmentActivity.this, listView, R.layout.listview_assignment, db.getAssignment());
        listView.setAdapter(adapter);
    }

    private void setupListViewMultiSelect() {
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
                    ArrayList<Assignment> removelist = new ArrayList<>();
                    SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                    for (int i = 0; i < checkedItems.size(); i++) {
                        int key = checkedItems.keyAt(i);
                        if (checkedItems.get(key)) {
                            db.deleteAssignmentById(Objects.requireNonNull(adapter.getItem(key)));
                            removelist.add(adapter.getAssignmentList().get(key));
                        }
                    }
                    adapter.getAssignmentList().removeAll(removelist);
                    db.updateAssignment(adapter.getAssignment());
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
        final View alertLayout = getLayoutInflater().inflate(R.layout.dialog_assignment, null);
        SetupDialog.getAddAssignmentDialog(AssignmentActivity.this, alertLayout, adapter);
    }
}
