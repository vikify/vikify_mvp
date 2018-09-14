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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class horizontalAdapter extends RecyclerView.Adapter<horizontalAdapter.MyViewHolder> {
  StorageReference  mStorageRef=FirebaseStorage.getInstance().getReference().child("videos/");

    private static final String TAG = "horizontalAdapter";
    Context mContext;


    private List<HorizontalClass> mCreatorList;
    

    public horizontalAdapter() {
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mCreator;
        public LinearLayout mLinearLayout;
        public ImageView mImageView;


        public MyViewHolder(View view) {
            super(view);
             mCreator = (TextView) view.findViewById(R.id.creator);
             mLinearLayout=view.findViewById(R.id.click_to_goto_video);
             mImageView=view.findViewById(R.id.vertical_imageview);
        }
    }

    public horizontalAdapter(List<HorizontalClass> yearList) {
        this.mCreatorList=yearList;
    }

    public void setData(List<HorizontalClass> creator, Context context) {
        if (creator != mCreatorList) {
            mCreatorList = creator;
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
        HorizontalClass dataClass = mCreatorList.get(position);
        holder.mCreator.setText(dataClass.getmYear());
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Log.v("TAG","position "+holder.mCreator.getText().toString());
               mContext.startActivity(new Intent(mContext, PlayerActivity.class));
              Log.v("TAG", "Bucket"+ mStorageRef.getBucket());
            }
        });

    }

//    public void startVideo(Task<Uri> mStorageRef){
//        Intent mIntent= new Intent(mContext,PlayerActivity.class);
//
//        Log.v("TAG","URLVIDS"+mStorageRef);
//        mContext.startActivity(mIntent);
//    }





    @Override
    public int getItemCount() {
        return mCreatorList.size();
    }
}
