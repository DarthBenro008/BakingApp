package com.benrostudios.bakingapp.network.response;

import java.io.Serializable;
import java.nio.file.SecureDirectoryStream;

public class StepsBean implements Serializable {
    /**
     * id : 0
     * shortDescription : Recipe Introduction
     * description : Recipe Introduction
     * videoURL : https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4
     * thumbnailURL :
     */

    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public StepsBean(int mId, String mShortDescription, String mDescription, String mVideoUrl, String mThumbnailUrl) {
         id  = mId;
         shortDescription = mShortDescription ;
         description = mDescription ;
         videoURL = mVideoUrl ;
         thumbnailURL = mThumbnailUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
