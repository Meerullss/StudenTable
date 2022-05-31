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

import com.fyp.StudenTable.layout.Lecturer;
import com.fyp.StudenTable.R;
import com.fyp.StudenTable.misc.SetupDialog;
import com.fyp.StudenTable.misc.DbSupport;

import java.util.ArrayList;
import java.util.Objects;


public class LecturerAdapter extends ArrayAdapter<Lecturer> {

    private final Activity mActivity;
    private final int mResource;
    private final ArrayList<Lecturer> lecturerlist;
    private Lecturer lecturer;
    private final ListView mListView;

    private static class ViewHolder {
        TextView name;
        TextView post;
        TextView phonenumber;
        TextView email;
        CardView cardView;
        ImageView popup;
    }

    public LecturerAdapter(Activity activity, ListView listView, int resource, ArrayList<Lecturer> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mListView = listView;
        mResource = resource;
        lecturerlist = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        String name = Objects.requireNonNull(getItem(position)).getName();
        String post = Objects.requireNonNull(getItem(position)).getPost();
        String phonenumber = Objects.requireNonNull(getItem(position)).getPhonenumber();
        String email = Objects.requireNonNull(getItem(position)).getEmail();
        int color = Objects.requireNonNull(getItem(position)).getColor();

        lecturer = new Lecturer(name, post, phonenumber, email, color);
        final ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name = convertView.findViewById(R.id.namelecturer);
            holder.post = convertView.findViewById(R.id.postlecturer);
            holder.phonenumber = convertView.findViewById(R.id.numberlecturer);
            holder.email = convertView.findViewById(R.id.emaillecturer);
            holder.cardView = convertView.findViewById(R.id.lecturer_cardview);
            holder.popup = convertView.findViewById(R.id.popupbtn);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(lecturer.getName());
        holder.post.setText(lecturer.getPost());
        holder.phonenumber.setText(lecturer.getPhonenumber());
        holder.email.setText(lecturer.getEmail());
        holder.cardView.setCardBackgroundColor(lecturer.getColor());
        holder.popup.setOnClickListener(v -> {
            final PopupMenu popup = new PopupMenu(mActivity, holder.popup);
            final DbSupport db = new DbSupport(mActivity);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.delete_popup:
                            db.deleteLecturerById(Objects.requireNonNull(getItem(position)));
                            db.updateLecturer(Objects.requireNonNull(getItem(position)));
                            lecturerlist.remove(position);
                            notifyDataSetChanged();
                            return true;

                        case R.id.edit_popup:
                            final View alertLayout = mActivity.getLayoutInflater().inflate(R.layout.dialog_lecturer, null);
                            SetupDialog.getEditTeacherDialog(mActivity, alertLayout, lecturerlist, mListView, position);
                            notifyDataSetChanged();
                            return true;
                        default:
                            return onMenuItemClick(item);
                    }
                }
            });
            popup.show();
        });

        hidePopUpMenu(holder);

        return convertView;
    }

    public ArrayList<Lecturer> getLecturerList() {
        return lecturerlist;
    }

    public Lecturer getLecturer() {
        return lecturer;
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