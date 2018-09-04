package com.bannuranurag.android.vikify;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class verticalAdapter extends RecyclerView.Adapter<verticalAdapter.MyViewHolder> {

    Context mContext;
    private List<String> mNameList;
    private List<HorizontalClass> mYearList;
    private List<String> mtags,mFinalTags;
    private RecyclerView.Adapter mAdapter;

    private static RecyclerView horizontalList;
    private static RecyclerView horizontalTagList;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, year, genre, Tags;
        private horizontalAdapter mHorizontalAdapter;
        private TagAdapter mHorizontalTagAdapter;

        public MyViewHolder(View view) {
            super(view);
            Context context= view.getContext();
            title = (TextView) view.findViewById(R.id.title);
//            genre = (TextView) view.findViewById(R.id.genre);
            Tags=(TextView) view.findViewById(R.id.tagText);

            horizontalList=view.findViewById(R.id.horizontal_recycle);
            horizontalTagList=view.findViewById(R.id.genre);

            horizontalList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            horizontalTagList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

            mHorizontalAdapter=new horizontalAdapter();
            mHorizontalTagAdapter=new TagAdapter();

            horizontalList.setAdapter(mHorizontalAdapter);
            horizontalTagList.setAdapter(mHorizontalTagAdapter);
           // year = (TextView) view.findViewById(R.id.year);

        }
    }

    public verticalAdapter(Context mContext, List<String> nameList, List<HorizontalClass> mYearList, List<String> mtags) {
        this.mContext = mContext;
        mNameList = nameList;
        this.mYearList = mYearList;
        this.mtags = mtags;
        Log.v("Vertical Adapter","hello"+this.mtags);
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

        String mString=mNameList.get(position);
        holder.title.setText(mString);
//        holder.genre.setText(dataClass.getGenre());
        holder.mHorizontalAdapter.setData(mYearList);
        try{
            String mData= mtags.get(position);
            //holder.Tags.setText(mData);


            switch(position){
                case 0:
                    mFinalTags=mtags.subList(0,8);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(0,7)+"pornpos: "+position);
                    break;
                case 1:
                    mFinalTags=mtags.subList(8,14);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(8,13)+"pornpos: "+position);
                    break;
                case 2:
                    mFinalTags=mtags.subList(14,25);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(14,23)+"pornpos: "+position);
                    break;
                case 3:
                    mFinalTags=mtags.subList(25,33);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(14,23)+"pornpos: "+position);
                    break;
                case 4:
                    mFinalTags=mtags.subList(33,42);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(14,23)+"pornpos: "+position);
                    break;
                case 5:
                    mFinalTags=mtags.subList(42,49);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(14,23)+"pornpos: "+position);
                    break;
                case 6:
                    mFinalTags=mtags.subList(49,56);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(14,23)+"pornpos: "+position);
                    break;
                 default:
                     Log.v("TAG","Does it work?");

            }
        }
        catch (IndexOutOfBoundsException e){
            Log.v("TAG","Out of bound exception");
        }




       //holder.year.setText(dataClass.getYear());
    }


    @Override
    public int getItemCount() {
        return mNameList.size();
    }
}
