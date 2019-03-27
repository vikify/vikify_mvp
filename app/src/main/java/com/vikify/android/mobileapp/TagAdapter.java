package com.vikify.android.mobileapp;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder> {
    List<String> tags= new ArrayList<>();
    private static final String TAG = "TagAdapter";
    int count=0;
    Random rand = new Random();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, an      d
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public CardView mCardView;

        public MyViewHolder(View v) {

            super(v);
            mTextView = v.findViewById(R.id.tagText);
            mCardView = v.findViewById(R.id.card_view1);
        }
    }

    public TagAdapter() {
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TagAdapter(List<String> mtag) {

    }

    public void setData(List<String> mtag) {
        if (mtag != tags) {
            tags=mtag;
//            Iterator<String> it = tags.iterator();
//            while (it.hasNext())
//                count++;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TagAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_layout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        String dataClass = tags.get(position);
        holder.mTextView.setText(dataClass);


        ((CardView) holder.mCardView).setCardBackgroundColor(Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return  tags.size();
    }
}