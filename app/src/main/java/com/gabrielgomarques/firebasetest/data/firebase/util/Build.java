package com.gabrielgomarques.firebasetest.data.firebase.util;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Gabriel on 18/01/2017.
 */

public class Build {

    public static DatabaseReference getLocalUserReference(DatabaseReference parentReference){
        return  parentReference.child("localmode").child("user").child("value");
    }
}
