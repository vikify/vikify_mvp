package com.bannuranurag.android.vikify.DataSaving;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bannuranurag.android.vikify.R;

import java.util.ArrayList;
import java.util.List;

public class SavedVideoAdapter extends RecyclerView.Adapter<SavedVideoAdapter.VideoViewHolder> {
List<String> vids = new ArrayList<>();
Context mContext;
    private static final String TAG = "SavedVideoAdapter";
    class VideoViewHolder extends RecyclerView.ViewHolder {
        private final TextView videoItemView;
        public final LinearLayout mLinearLayout;
        private VideoViewHolder(View itemView) {
            super(itemView);
            videoItemView = itemView.findViewById(R.id.VideoName);
            mLinearLayout=itemView.findViewById(R.id.video_layout);
        }
    }

    private LayoutInflater mInflater;
    private List<DBEntityClass> mVideos; // Cached copy of words

    public SavedVideoAdapter(Context context) { mInflater = LayoutInflater.from(context); mContext=context;}

    public SavedVideoAdapter(List<String> vids) {
        this.vids = vids;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_video_section_item, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, final int position) {
        if (mVideos != null) {
            final DBEntityClass current = mVideos.get(position);
            holder.videoItemView.setText(current.getmFilePath());
            holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG,"Position cick: file:///"+ current.getmFilePath());
                    Bundle extras = new Bundle();
                    extras.putString("Filepath",current.getmFilePath());
                    extras.putInt("Position",position);
                    Intent playeSavedVideoIntent = new Intent(mContext,VideoActivity.class);
                    extras.putString("URI","file:///"+ current.getmFilePath());
                    playeSavedVideoIntent.putExtras(extras);
                    mContext.startActivity(playeSavedVideoIntent);
                }
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.videoItemView.setText("No Video");
        }


//        holder.videoItemView.setText(vids.get(position));
    }

    public void setVideo(List<DBEntityClass>  videos){
        mVideos = videos;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mVideos != null)
            return mVideos.size();
        else return 0;
    }
}
