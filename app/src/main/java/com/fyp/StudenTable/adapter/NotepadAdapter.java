package com.fyp.StudenTable.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.fyp.StudenTable.layout.Notepad;
import com.fyp.StudenTable.R;
import com.fyp.StudenTable.misc.SetupDialog;
import com.fyp.StudenTable.misc.DbSupport;

import java.util.ArrayList;
import java.util.Objects;

public class NotepadAdapter extends ArrayAdapter<Notepad> {

    private final Activity mActivity;
    private final int mResource;
    private final ArrayList<Notepad> notelist;
    private Notepad notepad;
    private final ListView mListView;

    private static class ViewHolder {
        TextView title;
        ImageView popup;
        CardView cardView;
    }

    public NotepadAdapter(Activity activity, ListView listView, int resource, ArrayList<Notepad> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mListView = listView;
        mResource = resource;
        notelist = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String title = Objects.requireNonNull(getItem(position)).getTitle();
        String text = Objects.requireNonNull(getItem(position)).getText();
        int color = Objects.requireNonNull(getItem(position)).getColor();

        notepad = new Notepad(title, text, color);
        final ViewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.titlenote);
            holder.popup = convertView.findViewById(R.id.popupbtn);
            holder.cardView = convertView.findViewById(R.id.notes_cardview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(notepad.getTitle());
        holder.cardView.setCardBackgroundColor(notepad.getColor());
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
                                db.deleteNotepadById(Objects.requireNonNull(getItem(position)));
                                db.updateNotepad(Objects.requireNonNull(getItem(position)));
                                notelist.remove(position);
                                notifyDataSetChanged();
                                return true;

                            case R.id.edit_popup:
                                final View alertLayout = mActivity.getLayoutInflater().inflate(R.layout.dialog_notepad, null);
                                SetupDialog.getEditNotepadDialog(mActivity, alertLayout, notelist, mListView, position);
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

    public ArrayList<Notepad> getNoteList() {
        return notelist;
    }

    public Notepad getNote() {
        return notepad;
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
