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

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gabriel on 27/01/2017.
 */

public class PostRepository extends Repository<Post, PostRepository> {

    private static PostRepository instance;
    private PostRecyclerViewAdapter postRecyclerViewAdapter;
    private Query myTopPostsQuery;

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

        parentReference.child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                buildTaskSavePost(post, bitmap, snapshot.getChildrenCount());
                parentReference.child("posts").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void buildTaskSavePost(final Post post, final Bitmap bitmap, final long position) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... params) {

                DatabaseReference myRef = parentReference.child("posts").push();
                String key = myRef.getKey();
                post.setId(key);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/posts/" + position, post);
                childUpdates.put("/user-posts/" + post.getUserId() + "/" + position, post);

                imageStorage.savePostImage(post, parentReference, bitmap, PostRepository.this, childUpdates);

                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    public void getPostsByUser(final List<Post> posts, final User user, final int positionLastItem) {

        myTopPostsQuery = parentReference.child("user-posts").child(user.getUserId()).limitToLast(positionLastItem+5);

        myTopPostsQuery.addChildEventListener( new ChildEventListener() {
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

        });
    }

    private void insertOrReplace(DataSnapshot dataSnapshot, final List<Post> posts) {
        Post newPost = dataSnapshot.getValue(Post.class);

        //Add edition of an post for the viewers
        if (posts.contains(newPost)) {
            Post oldPost = posts.get(posts.indexOf(newPost));
            oldPost.setDatePost(newPost.getDatePost());
            oldPost.setDescription(newPost.getDescription());
        } else {
            posts.add(newPost);
        }

            Collections.sort(posts, new Comparator<Post>() {
                @Override
                public int compare(Post post, Post post2) {
                    return post != null && post2 != null ? - post.getTime().compareTo(post2.getTime()) : 0;
                }
            });
        postRecyclerViewAdapter.notifyDataSetChangedOvirreded();
    }

    public PostRecyclerViewAdapter getPostRecyclerViewAdapter() {
        return postRecyclerViewAdapter;
    }

    public void setPostRecyclerViewAdapter(PostRecyclerViewAdapter postRecyclerViewAdapter) {
        this.postRecyclerViewAdapter = postRecyclerViewAdapter;
    }
//
}
