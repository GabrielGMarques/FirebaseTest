package com.gabrielgomarques.firebasetest.data.firebase;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.gabrielgomarques.firebasetest.enitities.Post;
import com.gabrielgomarques.firebasetest.enitities.User;
import com.gabrielgomarques.firebasetest.ui.adapter.PostRecyclerViewAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Gabriel on 27/01/2017.
 */

public class PostRepository extends Repository<Post, PostRepository> {

    private static PostRepository instance;
    private PostRecyclerViewAdapter postRecyclerViewAdapter;

    private PostRepository() {
        super(Post.class);
    }

    public static PostRepository getInstance() {
        if (instance == null)
            instance = new PostRepository();
        return instance;
    }

    public void updatePost(Post post) {
        //When the request ends so it call the fragment listener so the post list can get updated
        parentReference.child("posts").child(post.getId()).setValue(post);

        parentReference.child("user-posts").child(post.getUserId()).child(post.getId()).setValue(post);

    }

    public void savePost(final Post post, final Bitmap bitmap) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... params) {

                String key = parentReference.child("posts").push().getKey();
                post.setId(key);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/posts/" + key, post);
                childUpdates.put("/user-posts/" + post.getUserId() + "/" + key, post);

                imageStorage.savePostImage(post, parentReference, bitmap, PostRepository.this, childUpdates);

                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    public void getPostsByUser(final List<Post> posts, final User user,final int positionLastItem) {

        final DatabaseReference localRef = parentReference.child("user-posts").child(user.getUserId());
        Query myTopPostsQuery = localRef.orderByChild("time").limitToLast
                (5);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                insertOrReplace(dataSnapshot, posts);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                insertOrReplace(dataSnapshot, posts);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                posts.remove(dataSnapshot.getValue(Post.class));
                postRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myTopPostsQuery.addChildEventListener(childEventListener);
    }

    private void insertOrReplace(DataSnapshot dataSnapshot, final List<Post> posts) {
//        Iterable<DataSnapshot> postsInterator = dataSnapshot.getChildren();
//        while (postsInterator.iterator().hasNext()) {
//        for (DataSnapshot data : dataSnapshot.getChildren()) {

        Post newPost = dataSnapshot.getValue(Post.class);

        if (posts.contains(newPost)) {
            Post oldPost = posts.get(posts.indexOf(newPost));
            oldPost.setDatePost(newPost.getDatePost());
            oldPost.setDescription(newPost.getDescription());
        } else {
            posts.add(0, newPost);
        }
//        }
        postRecyclerViewAdapter.notifyDataSetChanged();
    }

    public PostRecyclerViewAdapter getPostRecyclerViewAdapter() {
        return postRecyclerViewAdapter;
    }

    public void setPostRecyclerViewAdapter(PostRecyclerViewAdapter postRecyclerViewAdapter) {
        this.postRecyclerViewAdapter = postRecyclerViewAdapter;
    }
//    public void getById(final String userId, final DependsOfFirebaseDataActivity<Post> dependsOfFirebaseDataActivity) {
//
//        dependsOfFirebaseDataActivity.onStartRequest();
//
//        parentReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                List<Post> user = dataSnapshot.get
//
//                dependsOfFirebaseDataActivity.onSuccessRequest(user);
//
//                dependsOfFirebaseDataActivity.onFinishRequest();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });
//        return null;
//    }
}
