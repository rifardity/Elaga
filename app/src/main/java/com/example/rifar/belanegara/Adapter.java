package com.example.rifar.belanegara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class Adapter extends BaseAdapter {
    private Context context;
    private String[] nama_menu;
    private int[] icon_menu;

    public Adapter(Context c, String[] nama_menu, int[] icon_menu) {
        context = c;
        this.nama_menu = nama_menu;
        this.icon_menu = icon_menu;
    }

    public int getCount() {
        return icon_menu.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = inflater.inflate(R.layout.activity_grid_menu, parent, false);
            ImageView imageView = (ImageView) grid.findViewById(R.id.img_menu);
            TextView txtMenu = (TextView) grid.findViewById(R.id.txt_menu);
            imageView.setImageResource(icon_menu[position]);
            txtMenu.setText(nama_menu[position]);
        } else {
            grid = convertView;
        }
        return grid;

    }
}
