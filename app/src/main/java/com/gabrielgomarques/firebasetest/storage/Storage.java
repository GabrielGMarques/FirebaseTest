package com.gabrielgomarques.firebasetest.storage;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Gabriel on 07/01/2017.
 */

public class Storage {
    protected FirebaseStorage storage;
    protected StorageReference storageRef;

    public Storage() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://projecttest-f78de.appspot.com");

    }
}
