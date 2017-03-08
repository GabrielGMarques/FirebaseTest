package com.gabrielgomarques.firebasetest.enitities;

import java.util.Date;

/**
 * Created by Gabriel on 20/01/2017.
 */

public class Relashionaship {

    private Date begin;

    private User following;

    private User followed;

    public User getFollowed() {
        return followed;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }
}
