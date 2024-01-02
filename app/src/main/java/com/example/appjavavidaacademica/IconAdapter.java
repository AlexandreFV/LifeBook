package com.example.appjavavidaacademica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.List;

public class IconAdapter extends ArrayAdapter<Integer> {
    private Integer[] iconList;
    private LayoutInflater inflater;

    public IconAdapter(Context context, Integer[] iconList) {
        super(context, R.layout.icon_list_item, iconList);
        this.iconList = iconList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.icon_list_item, parent, false);
        }

        ImageView iconImageView = convertView.findViewById(R.id.IconImage);
        Integer iconResId = getItem(position);
        if (iconResId != null) {
            iconImageView.setImageResource(iconResId);
        }

        return convertView;
    }
}
