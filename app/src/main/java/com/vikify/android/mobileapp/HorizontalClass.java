package com.vikify.android.mobileapp;

import java.util.List;

public class HorizontalClass {
    private String mCreator;
    private String mVideoName;
    private String mVideoURL;
    private String desc;
    private List<String> tags;

    public HorizontalClass(String mCreator, String mVideoName, String mVideoURL,String videoDesc,List<String> tags) {
        this.mCreator = mCreator;
        this.mVideoName = mVideoName;
        this.mVideoURL = mVideoURL;
        desc=videoDesc;
        this.tags=tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getTags(int user){
        return tags.get(user);
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getmCreator() {
        return mCreator;
    }

    public void setmCreator(String mCreator) {
        this.mCreator = mCreator;
    }

    public String getmVideoName() {
        return mVideoName;
    }

    public void setmVideoName(String mVideoName) {
        this.mVideoName = mVideoName;
    }

    public String getmVideoURL() {
        return mVideoURL;
    }

    public void setmVideoURL(String mVideoURL) {
        this.mVideoURL = mVideoURL;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public HorizontalClass(String Creator) {
        this.mCreator = Creator;
    }

    public HorizontalClass() {
    }

    public String getmYear() {
        return mCreator;
    }

    public void setmYear(String Creator) {
        this.mCreator = Creator;
    }
}
