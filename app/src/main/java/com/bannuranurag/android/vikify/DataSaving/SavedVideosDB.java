package com.bannuranurag.android.vikify.DataSaving;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {DBEntityClass.class}, version = 1, exportSchema = false)
public abstract class SavedVideosDB extends RoomDatabase {
    public abstract DBDao daoAccess() ;
}