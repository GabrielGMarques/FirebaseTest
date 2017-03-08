package com.gabrielgomarques.firebasetest.data.firebase;

import android.util.Log;

import com.gabrielgomarques.firebasetest.storage.ImageStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Gabriel on 07/01/2017.
 */

public class Repository<T,R> {

    protected static FirebaseDatabase database;
    protected Class<T> tClass;
    protected Class<R> repository;
    protected DatabaseReference parentReference;
    protected ImageStorage imageStorage;

    public Repository(Class<T> tClass) {
        if(database==null) {
            this.database = FirebaseDatabase.getInstance();
            this.database.setPersistenceEnabled(true);
        }
        this.tClass = tClass;
        this.parentReference = database.getReference(tClass.getSimpleName());
        this.imageStorage  = new ImageStorage();
        this.parentReference.keepSynced(true);
    }

    public void onConstruct(Class<R> repository){
        this.repository = repository;
    }





    public void onDataChange(Class tClass){
            Log.i(repository.getName(), "Changed: "+tClass.getName());
    }



}
