package com.bannuranurag.android.vikify;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class horizontalAdapter extends RecyclerView.Adapter<horizontalAdapter.MyViewHolder> {

    private static final String TAG = "horizontalAdapter";
    Context mContext;


    private List<HorizontalClass> yearList;
    

    public horizontalAdapter() {
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView year;
        public LinearLayout mLinearLayout;
        public ImageView mImageView;


        public MyViewHolder(View view) {
            super(view);
             year = (TextView) view.findViewById(R.id.year);
             mLinearLayout=view.findViewById(R.id.click_to_goto_video);
             mImageView=view.findViewById(R.id.vertical_imageview);
        }
    }

    public horizontalAdapter(List<HorizontalClass> yearList) {
        this.yearList=yearList;
    }

    public void setData(List<HorizontalClass> year, Context context) {
        if (year != yearList) {
            yearList = year;
            notifyDataSetChanged();
        }
        mContext=context;
    }






    @NonNull
    @Override
    public horizontalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontalview_item_layout,parent,false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        HorizontalClass dataClass = yearList.get(position);
        holder.year.setText(dataClass.getmYear());
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Log.v("TAG","position "+holder.year.getText().toString());
                mContext.startActivity(new Intent(mContext, VideoPlayer.class));
            }
        });

    }





    @Override
    public int getItemCount() {
        return yearList.size();
    }
}
