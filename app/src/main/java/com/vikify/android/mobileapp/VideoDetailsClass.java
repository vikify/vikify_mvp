package com.vikify.android.mobileapp;

import android.support.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

@Keep
public class VideoDetailsClass {
    public String mVideoName;
    public String name;
    List<String> tags = new ArrayList<>();
    public String downloadUrl;
    String description;

    public VideoDetailsClass() {
    }

    public VideoDetailsClass(String Videoname, List<String> Tags, String url,String name,String description){
        mVideoName=Videoname;
        this.tags=Tags;
        downloadUrl=url;
        this.name=name;
        this.description=description;
    }

    public VideoDetailsClass(String creatorname,String downloadUrl, String description) {
        this.downloadUrl = downloadUrl;
        this.description = description;
        this.name=creatorname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VideoDetailsClass(String mVideoNodeName, String name, List<String> tags, String downloadUrl) {
        this.mVideoName = mVideoNodeName;
        name = name;
        tags = tags;
        this.downloadUrl = downloadUrl;
    }

    public String getmVideoName() {
        return mVideoName;
    }

    public void setmVideoName(String mVideoNodeName) {
        this.mVideoName = mVideoNodeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        tags = tags;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
