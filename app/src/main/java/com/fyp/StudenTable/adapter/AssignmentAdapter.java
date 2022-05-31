package com.fyp.StudenTable.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fyp.StudenTable.layout.Assignment;
import com.fyp.StudenTable.R;
import com.fyp.StudenTable.misc.SetupDialog;
import com.fyp.StudenTable.misc.DbSupport;

import java.util.ArrayList;
import java.util.Objects;


public class AssignmentAdapter extends ArrayAdapter<Assignment> {

    private final Activity mActivity;
    private final int mResource;
    private final ArrayList<Assignment> assignmentlist;
    private Assignment assignment;
    private final ListView mListView;

    private static class ViewHolder {
        TextView module;
        TextView description;
        TextView date;
        CardView cardView;
        ImageView popup;
    }

    public AssignmentAdapter(Activity activity, ListView listView, int resource, ArrayList<Assignment> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mListView = listView;
        mResource = resource;
        assignmentlist = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        String subject = Objects.requireNonNull(getItem(position)).getModule();
        String description = Objects.requireNonNull(getItem(position)).getDescription();
        String date = Objects.requireNonNull(getItem(position)).getDate();
        int color = Objects.requireNonNull(getItem(position)).getColor();

        assignment = new Assignment(subject, description, date, color);
        final ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.module = convertView.findViewById(R.id.moduleassignment);
            holder.description = convertView.findViewById(R.id.descriptionassignment);
            holder.date = convertView.findViewById(R.id.dateassignment);
            holder.cardView = convertView.findViewById(R.id.assignment_cardview);
            holder.popup = convertView.findViewById(R.id.popupbtn);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.module.setText(assignment.getModule());
        holder.description.setText(assignment.getDescription());
        holder.date.setText(assignment.getDate());
        holder.cardView.setCardBackgroundColor(assignment.getColor());
        holder.popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(mActivity, holder.popup);
                final DbSupport db = new DbSupport(mActivity);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_popup:
                                db.deleteAssignmentById(Objects.requireNonNull(getItem(position)));
                                db.updateAssignment(Objects.requireNonNull(getItem(position)));
                                assignmentlist.remove(position);
                                notifyDataSetChanged();
                                return true;

                            case R.id.edit_popup:
                                final View alertLayout = mActivity.getLayoutInflater().inflate(R.layout.dialog_assignment, null);
                                SetupDialog.getEditAssignmentDialog(mActivity, alertLayout, assignmentlist, mListView, position);
                                notifyDataSetChanged();
                                return true;
                            default:
                                return onMenuItemClick(item);
                        }
                    }
                });
                popup.show();
            }
        });

        hidePopUpMenu(holder);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public ArrayList<Assignment> getAssignmentList() {
        return assignmentlist;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    private void hidePopUpMenu(ViewHolder holder) {
        SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();
        if (checkedItems.size() > 0) {
            for (int i = 0; i < checkedItems.size(); i++) {
                int key = checkedItems.keyAt(i);
                if (checkedItems.get(key)) {
                    holder.popup.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            holder.popup.setVisibility(View.VISIBLE);
        }
    }
}

