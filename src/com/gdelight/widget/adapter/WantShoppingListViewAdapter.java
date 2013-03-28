package com.gdelight.widget.adapter;

import java.util.ArrayList;

import com.gdelight.R;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class WantShoppingListViewAdapter extends ArrayAdapter<String> {
    private ArrayList<String> mData;

    public WantShoppingListViewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mData = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int index) {
        return mData.get(index);
    }
    
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        TextView originalView = (TextView) super.getView(position, convertView, parent); // Get the original view

        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final TextView view = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);

        // Start tweaking
        view.setText(originalView.getText());
        //view.setTextColor(getResources().R.color.red);  // also useful if you have a color scheme that makes the text show up white
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10); // override the text size
        return view;
    }
}