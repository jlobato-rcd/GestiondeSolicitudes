package com.rcdhotels.gestiondesolicitudes.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rcdhotels.gestiondesolicitudes.R;

public class SpinnersLanguagesAdapter extends BaseAdapter {

    private String[] list;
    private LayoutInflater mInflater;
    private Context mContext;

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    public SpinnersLanguagesAdapter(Activity mContext, String[] list) {
        super();
        this.list = list;
        this.mInflater = mContext.getLayoutInflater();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public String getItem(int position) {

        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String value = list[position];

        View view = mInflater.inflate(R.layout.item_lang_spinner, null);
        ImageView imageView = view.findViewById(R.id.imageViewLang);
        TextView textViewItem = (TextView) view.findViewById(R.id.textViewLang);
        switch (position){
            case 0:
                imageView.setVisibility(View.INVISIBLE);
                break;
            case 1:
                imageView.setImageResource(R.drawable.ic_spanish);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_english);
                break;
        }

        textViewItem.setText(value);

        return (view);
    }
}