package com.gabrielgomarques.firebasetest.enitities;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Gabriel on 19/01/2017.
 */

public class Post {

    private String id;

    private String authorName;

    private String userId;

    private String mediaUrl;

    private Date datePost;

    private String description;

    private Long time;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Date getDatePost() {
        return datePost;
    }

    public void setDatePost(Date datePost) {
        this.datePost = datePost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTime() {
        return datePost != null ? datePost.getTime() : 0;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < id.length(); i++)
            hash = 31 * hash + id.charAt(i);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null && getClass() == obj.getClass() && hashCode() == obj.hashCode());
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", datePost=" + datePost +
                ", description='" + description + '\'' +
                '}';
    }
}
