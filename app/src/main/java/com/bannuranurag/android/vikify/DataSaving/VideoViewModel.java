package com.bannuranurag.android.vikify.DataSaving;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {

    private VideoRepo mRepository;

    private LiveData<List<DBEntityClass>> mAllVideos;
    int count;

    public VideoViewModel (Application application) {
        super(application);
        mRepository = new VideoRepo(application);
        mAllVideos = mRepository.getmAllVideos();
    }

    public LiveData<List<DBEntityClass>> getVideos() { return mAllVideos; }

    public int getVideoCount(){return count;}

    public void insert(DBEntityClass video) { mRepository.insertOnlySingleVideo(video); }

    public void deleteWord(DBEntityClass video) {mRepository.deleteWord(video);}
}
