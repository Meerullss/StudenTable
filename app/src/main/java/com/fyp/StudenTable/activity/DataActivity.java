package com.fyp.StudenTable.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.fyp.StudenTable.layout.Notepad;
import com.fyp.StudenTable.R;
import com.fyp.StudenTable.misc.DbSupport;

public class DataActivity extends AppCompatActivity {

    private DbSupport db;
    private Notepad notepad;
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        setupIntent();
    }

    private void setupIntent() {
        db = new DbSupport(DataActivity.this);
        notepad = (Notepad) getIntent().getSerializableExtra(NotepadActivity.NOTEPAD_KEY);
        text = findViewById(R.id.editNotepad);
        if(notepad.getText() != null) {
            text.setText(notepad.getText());
        }
    }

    @Override
    public void onBackPressed() {
        notepad.setText(text.getText().toString());
        db.updateNotepad(notepad);
        Toast.makeText(DataActivity.this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            notepad.setText(text.getText().toString());
            db.updateNotepad(notepad);
            Toast.makeText(DataActivity.this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
