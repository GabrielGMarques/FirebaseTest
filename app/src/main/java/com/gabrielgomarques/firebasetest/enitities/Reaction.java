package com.gabrielgomarques.firebasetest.enitities;

/**
 * Created by Gabriel on 20/01/2017.
 */

public class Reaction {

    private User user;

    private String comment;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
