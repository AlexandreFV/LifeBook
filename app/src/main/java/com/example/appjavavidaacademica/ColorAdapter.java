package com.example.appjavavidaacademica;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.graphics.drawable.GradientDrawable;
public class ColorAdapter extends BaseAdapter {

    private Context context;
    private String[] hexColors;

    public ColorAdapter(Context context, String[] hexColors) {
        this.context = context;
        this.hexColors = hexColors;
    }

    @Override
    public int getCount() {
        return hexColors.length;
    }

    @Override
    public Object getItem(int position) {
        return hexColors[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.color_list_item, null);
        }

        ImageView colorSquare = view.findViewById(R.id.colorSquare);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor(hexColors[position]));
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setSize(70, 70); // Ajuste o tamanho conforme necessário
        colorSquare.setImageDrawable(drawable);

        return view;
    }
}
