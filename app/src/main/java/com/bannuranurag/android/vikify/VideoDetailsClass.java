package com.bannuranurag.android.vikify;

import java.util.ArrayList;
import java.util.List;

public class VideoDetailsClass {
    public String mVideoNodeName;
    public String name;
    List<String> tags = new ArrayList<>();
    public String downloadUrl;

    public VideoDetailsClass() {
    }

    public VideoDetailsClass(String name, List<String> Tags, String url){
        this.name=name;
        this.tags=Tags;
        downloadUrl=url;
    }

    public VideoDetailsClass(String mVideoNodeName, String name, List<String> tags, String downloadUrl) {
        this.mVideoNodeName = mVideoNodeName;
        name = name;
        tags = tags;
        this.downloadUrl = downloadUrl;
    }

    public String getmVideoNodeName() {
        return mVideoNodeName;
    }

    public void setmVideoNodeName(String mVideoNodeName) {
        this.mVideoNodeName = mVideoNodeName;
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
