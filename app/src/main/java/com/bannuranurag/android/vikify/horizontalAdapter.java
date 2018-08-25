package com.bannuranurag.android.vikify;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class horizontalAdapter extends RecyclerView.Adapter<horizontalAdapter.MyViewHolder> {

    private List<HorizontalClass> yearList;

    public horizontalAdapter() {
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView year;

        public MyViewHolder(View view) {
            super(view);
             year = (TextView) view.findViewById(R.id.year);

        }
    }

    public horizontalAdapter(List<HorizontalClass> yearList) {
        this.yearList=yearList;
    }

    public void setData(List<HorizontalClass> year) {
        if (year != yearList) {
            yearList = year;
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public horizontalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontalview_item_layout,parent,false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HorizontalClass dataClass = yearList.get(position);
        MyViewHolder myholder= (MyViewHolder) holder;
        holder.year.setText(dataClass.getmYear());

    }





    @Override
    public int getItemCount() {
        return yearList.size();
    }
}
