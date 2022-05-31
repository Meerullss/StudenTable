package com.fyp.StudenTable.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.fyp.StudenTable.adapter.NotepadAdapter;
import com.fyp.StudenTable.layout.Notepad;
import com.fyp.StudenTable.R;
import com.fyp.StudenTable.misc.SetupDialog;
import com.fyp.StudenTable.misc.DbSupport;

import java.util.ArrayList;
import java.util.Objects;

public class NotepadActivity extends AppCompatActivity {

    public static String NOTEPAD_KEY = "note";
    private final Context context = this;
    private ListView listView;
    private DbSupport db;
    private NotepadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        initAll();
    }

    private void initAll() {
        setupAdapter();
        setupListViewMultiSelect();
        setupCustomDialog();
    }

    private void setupAdapter() {
        db = new DbSupport(context);
        listView = findViewById(R.id.notepadlist);
        adapter = new NotepadAdapter(NotepadActivity.this, listView, R.layout.listview_notepad, db.getNotepad());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(context, DataActivity.class);
            intent.putExtra(NOTEPAD_KEY, adapter.getNoteList().get(position));
            startActivity(intent);
        });
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
                    ArrayList<Notepad> removelist = new ArrayList<>();
                    SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                    for (int i = 0; i < checkedItems.size(); i++) {
                        int key = checkedItems.keyAt(i);
                        if (checkedItems.get(key)) {
                            db.deleteNotepadById(Objects.requireNonNull(adapter.getItem(key)));
                            removelist.add(adapter.getNoteList().get(key));
                        }
                    }
                    adapter.getNoteList().removeAll(removelist);
                    db.updateNotepad(adapter.getNote());
                    adapter.notifyDataSetChanged();
                    mode.finish();
                    return true;
                }
                return false;
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) { }
        });
    }

    private void setupCustomDialog() {
        final View alertLayout = View.inflate(context, R.layout.dialog_notepad, null);
        SetupDialog.getAddNotepadDialog(NotepadActivity.this, alertLayout, adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        adapter.addAll(db.getNotepad());
        adapter.notifyDataSetChanged();
    }
}
