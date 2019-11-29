package com.example.fruit_tourney;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FruitAdapter extends BaseAdapter {

    private final Context mContext;
    private final int[] books;

    // 1
    public FruitAdapter(Context context) {
        this.mContext = context;
        this.books = new int[]{0,1,2,3};
    }

    // 2
    @Override
    public int getCount() {
        return books.length;
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int book = books[position];

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.stats_fruit_layout, null);
        }

        // 3
        final ImageView imageView = convertView.findViewById(R.id.imageview_stat_fruit);
        final TextView nameTextView = convertView.findViewById(R.id.fruit_name);
        final TextView authorTextView = convertView.findViewById(R.id.fruit_stat);

        // 4
        // imageView.setImageResource();
        // nameTextView.setText(mContext.getString(book.getName()));
        // authorTextView.setText(mContext.getString(book.getAuthor()));

        return convertView;
    }

}
