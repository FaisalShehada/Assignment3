package com.example.faisal.sudokugame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Names> {
    ArrayList<Names> namesList;

    public CustomAdapter(Context context, ArrayList<Names> list) {
        super(context, R.layout.custom_row, list);

        this.namesList = list;

    }

    @Override
    public int getCount() {
        return this.namesList.size();
    }

    @Override
    public Names getItem(int position) {
        return this.namesList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater faisalInflater = LayoutInflater.from(getContext());
        View customView = faisalInflater.inflate(R.layout.custom_row, parent, false);

        Names current = namesList.get(position);
        TextView name = (TextView) customView.findViewById(R.id.theName);
        TextView time = (TextView) customView.findViewById(R.id.theTime);

        name.setText(current.getName());
        time.setText(current.getTime());

        return customView;
    }
}
