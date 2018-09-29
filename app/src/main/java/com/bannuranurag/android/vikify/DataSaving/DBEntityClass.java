package com.bannuranurag.android.vikify.DataSaving;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "saved_videos")
public class DBEntityClass {

    @PrimaryKey(autoGenerate = true)
    private int key;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }



    @NonNull

    @ColumnInfo(name = "file_path")
    public String mFilePath;


    @ColumnInfo(name = "time_stamp")
    private long unixTimeStamp;

    @ColumnInfo(name = "creator_uid")
    private String creatorUID;

    public DBEntityClass() {
    }

//    public DBEntityClass(@NonNull String mFilePath, long unixTimeStamp, String creatorUID) {
//        this.mFilePath = mFilePath;
//        this.unixTimeStamp = unixTimeStamp;
//        this.creatorUID = creatorUID;
//    }

    @NonNull
    public String getmFilePath() {

        return mFilePath;
    }

    public void setmFilePath(@NonNull String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public long getUnixTimeStamp() {
        return unixTimeStamp;
    }

    public void setUnixTimeStamp(long unixTimeStamp) {
        this.unixTimeStamp = unixTimeStamp;
    }

    public String getCreatorUID() {
        return creatorUID;
    }

    public void setCreatorUID(String creatorUID) {
        this.creatorUID = creatorUID;
    }
}
