package com.bannuranurag.android.vikify;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class verticalAdapter extends RecyclerView.Adapter<verticalAdapter.MyViewHolder> {

    Context mContext;
    private List<DataClass> moviesList;
    private List<HorizontalClass> mYearList;
    private static RecyclerView horizontalList;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, year, genre;
        private horizontalAdapter mHorizontalAdapter;

        public MyViewHolder(View view) {
            super(view);
            Context context= view.getContext();
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            horizontalList=view.findViewById(R.id.horizontal_recycle);
            horizontalList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            mHorizontalAdapter=new horizontalAdapter();
            horizontalList.setAdapter(mHorizontalAdapter);
           // year = (TextView) view.findViewById(R.id.year);

        }
    }

    public verticalAdapter(Context context, List<DataClass> moviesList, List<HorizontalClass> mYearList ) {
        mContext=context;
        this.moviesList=moviesList;
        this.mYearList=mYearList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create new view
       final View view= LayoutInflater.from(mContext).inflate(R.layout.my_vertical_view,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass dataClass = moviesList.get(position);
        holder.title.setText(dataClass.getTitle());
        holder.genre.setText(dataClass.getGenre());
        holder.mHorizontalAdapter.setData(mYearList);

       //holder.year.setText(dataClass.getYear());
    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
