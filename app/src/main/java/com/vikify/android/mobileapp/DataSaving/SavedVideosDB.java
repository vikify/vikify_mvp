package com.vikify.android.mobileapp.DataSaving;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {DBEntityClass.class}, version = 1, exportSchema = false)
public abstract class SavedVideosDB extends RoomDatabase {
    public abstract DBDao daoAccess() ;

    private static volatile SavedVideosDB INSTANCE;

    static SavedVideosDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SavedVideosDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SavedVideosDB.class, "SavedVideos")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}