package com.fyp.StudenTable.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fyp.StudenTable.adapter.WeekAdapter;
import com.fyp.StudenTable.misc.DbSupport;
import com.fyp.StudenTable.R;
import com.fyp.StudenTable.misc.FragmentSupport;

public class TuesdayFragment extends Fragment {

    public static final String KEY_TUESDAY_FRAGMENT = "Tuesday";
    private WeekAdapter adapter;
    private ListView listView;
    private DbSupport db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tuesday, container, false);
        setupAdapter(view);
        setupListViewMultiSelect();
        return view;
    }

    private void setupAdapter(View view) {
        db = new DbSupport(getActivity());
        listView = view.findViewById(R.id.tuesdaylist);
        adapter = new WeekAdapter(getActivity(), listView, R.layout.listview_week, db.getWeek(KEY_TUESDAY_FRAGMENT));
        listView.setAdapter(adapter);
    }

    private void setupListViewMultiSelect() {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(FragmentSupport.setupListViewMultiSelect(getActivity(), listView, adapter, db));
    }
}