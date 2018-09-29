package com.bannuranurag.android.vikify.DataSaving;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
public interface DBDao {                            //This interface is used to generate implementation of the methods shown (CRUD)

    @Query("SELECT * FROM saved_videos")            //db name
    List<DBEntityClass> getAllVideos();

    @Query("DELETE FROM saved_videos")
    void deleteAll();

    @Insert
    void insertOnlySingleVideo(DBEntityClass video);

    @Query("SELECT COUNT(*) from SAVED_VIDEOS")
    int countVideos();

    @Insert
    void insertAll(DBEntityClass... videos);

    @Delete
    void delete(DBEntityClass video);

    @Delete
    void deleteVideo(DBEntityClass video);
    
}
