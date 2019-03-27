package com.vikify.android.mobileapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ProfileVideosAdapter extends RecyclerView.Adapter<ProfileVideosAdapter.MyViewHolder> {

    private List<String>videoname,creatorname,downloadurl;
    private List<List<String>> tags;
    private List<String> description;
    Context mContext;
    private static final String TAG = "ProfileVideosAdapter";



    public  ProfileVideosAdapter(){

    }

    public ProfileVideosAdapter(List<String> videoname, List<String> creatorname, List<String> downloadurl, List<List<String>> tags,Context mContext,List<String> description) {
        this.videoname = videoname;
        this.creatorname = creatorname;
        this.downloadurl = downloadurl;
        this.mContext=mContext;
        this.tags = tags;
        this.description=description;
    }



    @NonNull
    @Override
    public ProfileVideosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_video_section_item,parent,false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.videoName.setText(videoname.get(position));
            holder.name.setText(creatorname.get(position));
            Log.v(TAG,"Vidnam"+videoname.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String url=downloadurl.get(position).toString();
                        if(!url.contains("uniqueTimeStamp")){
                            String youtube_id=url.substring(url.length()-11,url.length());
                            Log.v(TAG,"Youtube video clicked "+ youtube_id);
                            Intent mIntent = new Intent(mContext, youtube_player_activity.class);
                            mIntent.putExtra("youtube_video_id",youtube_id);
                            mContext.startActivity(mIntent);
                        }
                        else {
                            Intent mIntent = new Intent(mContext, PlayerActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("URL", downloadurl.get(position));
                            Log.v(TAG, "Download URL" + downloadurl.get(position));
                            bundle.putString("Description", description.get(position));
                            mIntent.putExtras(bundle);
                            mContext.startActivity(mIntent);
                        }
                    }
                    catch (NullPointerException e){
                        Log.v(TAG,"NULL pointer at description "+e);
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return creatorname.size();
    }

    private void setData(List<String> videoname, List<String> creatorname, List<String> downloadurl){

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,videoName;
        public ListView tags;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.CreatorName);
            videoName=itemView.findViewById(R.id.VideoName);
//            tags=itemView.findViewById(R.id.tags);
        }
    }
}
