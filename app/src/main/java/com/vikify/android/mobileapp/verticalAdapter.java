package com.vikify.android.mobileapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class verticalAdapter extends RecyclerView.Adapter<verticalAdapter.MyViewHolder> {

    Context mContext;
    private static final String TAG = "verticalAdapter";
    private List<String> mNameList;
    private List<HorizontalClass> mVideoList;
    private List<String> mtags,mFinalTags;
    private RecyclerView.Adapter mAdapter;
    List<HorizontalClass> selectedVideos = new ArrayList<>();
    private DatabaseReference databaseReference;
    private static RecyclerView horizontalList;
    private static RecyclerView horizontalTagList;
    String creatorName;String tags;String videoURL;String videoDescription;String videoName;
    private List<HorizontalClass> videoList;
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, year, genre, Tags;
        private horizontalAdapter mHorizontalAdapter;
        private TagAdapter mHorizontalTagAdapter;
        CardView mCardView;



        public MyViewHolder(View view) {
            super(view);
            Context context= view.getContext();
            title = (TextView) view.findViewById(R.id.title);
//            genre = (TextView) view.findViewById(R.id.genre);
//            mCardView=view.findViewById(R.id.card_view);
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

    public verticalAdapter(Context mContext, List<String> nameList, List<HorizontalClass> videoList, List<String> mtags, DatabaseReference databaseReference) {
        this.mContext = mContext;
        mNameList = nameList;
        mVideoList=videoList;
        this.mtags = mtags;
        this.databaseReference=databaseReference;
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
        holder.mHorizontalAdapter.setData(mVideoList,mContext);
        try{
           // String mData= mtags.get(position);
            //holder.Tags.setText(mData);


            switch(position){
                case 0:
                    mFinalTags=mtags.subList(0,8);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    selectedVideos= parseDatatToBeDisplayed(mFinalTags,mVideoList);
                    holder.mHorizontalAdapter.setData(selectedVideos,mContext);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(0,7)+"pos: "+position);
                    break;
                case 1:
                    mFinalTags=mtags.subList(8,14);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    selectedVideos= parseDatatToBeDisplayed(mFinalTags,mVideoList);
                    holder.mHorizontalAdapter.setData(selectedVideos,mContext);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(8,13)+"pos: "+position);
                    break;
                case 2:
                    mFinalTags=mtags.subList(14,25);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    selectedVideos= parseDatatToBeDisplayed(mFinalTags,mVideoList);
                    holder.mHorizontalAdapter.setData(selectedVideos,mContext);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(14,23)+"pos: "+position);
                    break;
                case 3:
                    mFinalTags=mtags.subList(25,33);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    selectedVideos= parseDatatToBeDisplayed(mFinalTags,mVideoList);
                    holder.mHorizontalAdapter.setData(selectedVideos,mContext);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(14,23)+"pos: "+position);
                    break;
                case 4:
                    mFinalTags=mtags.subList(33,42);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    selectedVideos= parseDatatToBeDisplayed(mFinalTags,mVideoList);
                    holder.mHorizontalAdapter.setData(selectedVideos,mContext);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(14,23)+"pos: "+position);
                    break;
                case 5:
                    mFinalTags=mtags.subList(42,49);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    selectedVideos= parseDatatToBeDisplayed(mFinalTags,mVideoList);
                    holder.mHorizontalAdapter.setData(selectedVideos,mContext);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(14,23)+"pos: "+position);
                    break;
                case 6:
                    mFinalTags=mtags.subList(49,56);
                    holder.mHorizontalTagAdapter.setData(mFinalTags);
                    selectedVideos= parseDatatToBeDisplayed(mFinalTags,mVideoList);
                    holder.mHorizontalAdapter.setData(selectedVideos,mContext);
                    Log.v("TAG","TotalTags to be passed"+mtags.subList(14,23)+"pos: "+position);
                    break;
                 default:
                     Log.v("TAG","Does it work? It does not.");

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

    public List<HorizontalClass> parseDatatToBeDisplayed( List<String> mFinalTags, List<HorizontalClass> mVideoList){
        List<HorizontalClass> selectedVideos = new ArrayList<>();
        Set<HorizontalClass> hashSet = new HashSet<>();
            int sum=0;
            for(int i =0;i<mFinalTags.size();i++){
                for(int j=0;j<mVideoList.size();j++)
                {
                    for(int k=0;k<mVideoList.get(j).getTags().size();k++){
                     //   Log.v(TAG,"Tags given are:"+mFinalTags.get(i)+"Tags got are: "+mVideoList.get(j).getTags().get(k));
                        if(mFinalTags.get(i).equals(mVideoList.get(j).getTags().get(k))){
                            selectedVideos.add(mVideoList.get(j));
                            hashSet.addAll(selectedVideos);
                            selectedVideos.clear();
                            selectedVideos.addAll(hashSet);
                        //    Log.v(TAG,"Video Number "+j+" has matched the tag "+mVideoList.get(j).getTags().get(k));
                        }
                   //     Log.v(TAG,"The value of the sum is "+ sum);

                    }

                }
            }

        return selectedVideos;
//
    //    Log.v(TAG,"Tags are1 "+mFinalTags.get(0));

          //  Log.v(TAG,"User made video tags are"+mVideoList.get(4).getTags());
    }
}



