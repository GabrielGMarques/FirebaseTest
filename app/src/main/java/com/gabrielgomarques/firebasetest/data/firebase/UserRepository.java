package com.gabrielgomarques.firebasetest.data.firebase;

import android.graphics.Bitmap;

import com.gabrielgomarques.firebasetest.enitities.User;
import com.gabrielgomarques.firebasetest.ui.activity.RequestListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Gabriel on 11/01/2017.
 */

public class UserRepository extends Repository<User, UserRepository> {

    private static UserRepository instance;


    private UserRepository() {
        super(User.class);
    }

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    public void saveUser(User user, Bitmap bitmap,final RequestListener<User> requestListener) {

        DatabaseReference localRef = parentReference.child(user.getUserId());

        Task<Void> task = localRef.setValue(user);

        if (bitmap != null)
            imageStorage.saveUserImage(user, this, localRef, bitmap);

        requestListener.onStartRequest();

        localRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                requestListener.onSuccessRequest(user);

                requestListener.onFinishRequest();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public void getById(final String userId, final RequestListener<User> requestListener) {

        DatabaseReference localRef = parentReference.child(userId);

        requestListener.onStartRequest();

        localRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                requestListener.onSuccessRequest(user);

                requestListener.onFinishRequest();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void onConstruct() {
        super.onConstruct(UserRepository.class);
    }

}
