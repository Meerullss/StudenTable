package com.fyp.StudenTable.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fyp.StudenTable.fragment.SettingsFragment;
import com.fyp.StudenTable.R;

public class SettingActivity extends AppCompatActivity {
    public static final String
            KEY_SEVEN_DAYS_SETTING = "sevendays";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
