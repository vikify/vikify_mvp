package com.vikify.android.mobileapp.DataSaving;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
public interface DBDao {                            //This interface is used to generate implementation of the methods shown (CRUD)

    @Query("SELECT * FROM saved_videos")            //db name
    LiveData<List<DBEntityClass>> getAllVideos();

    @Query("DELETE FROM saved_videos")
    void deleteAll();

    @Query("SELECT * FROM saved_videos where creator_uid = :creatorUID")
    LiveData<List<DBEntityClass>> getVideos(String creatorUID);

    @Insert
    void insertOnlySingleVideo(DBEntityClass video);

    @Query("SELECT COUNT(*) from saved_videos")
    int countVideos();

    @Query("DELETE FROM saved_videos WHERE `key`=:key")
    void getVideoWithKey(int key);

    @Query("DELETE  FROM saved_videos WHERE file_path=:mFilePath")
    void deleteVideoWithPath(String mFilePath);


    @Query("SELECT COUNT(*) FROM saved_videos WHERE creator_uid = :creatorUID ")
    int getCount(String creatorUID);

    @Insert
    void insertAll(DBEntityClass... videos);

    @Delete
    void delete(DBEntityClass video);

//    @Query("DELETE FROM saved_videos WHERE ID= :key+1")
//    void delete(int key);

    @Delete
    void deleteVideo(DBEntityClass video);
    
}
