package com.gabrielgomarques.firebasetest.storage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.gabrielgomarques.firebasetest.data.firebase.PostRepository;
import com.gabrielgomarques.firebasetest.data.firebase.UserRepository;
import com.gabrielgomarques.firebasetest.enitities.Post;
import com.gabrielgomarques.firebasetest.enitities.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Created by Gabriel on 07/01/2017.
 */

public class ImageStorage extends Storage {

    public Task<Uri> getImage(Class tClass, String id, String path) {
        StorageReference imagesRef = storageRef.child(tClass.getSimpleName() + "/" + id);
        StorageReference interestrellarRef = storageRef.child(path);
        return interestrellarRef.getDownloadUrl();
    }

    /**
     * Save image
     *
     * @param user
     * @return pathOfImage
     */
    public void saveUserImage(final User user, final UserRepository userRepository, final DatabaseReference dataRef, Bitmap bitmap) {

        StorageReference interestrellarRef = storageRef.child(User.class.getSimpleName()).child(user.getUserId()).child("images").child("profile").child("pofileImage");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = interestrellarRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                user.setImageUrl(downloadUrl.toString());

                dataRef.setValue(user);
            }
        });
    }

    public void savePostImage(final Post post, final DatabaseReference dataRef, Bitmap bitmap, final PostRepository postRepository, final Map<String, Object> childUpdates) {

        StorageReference interestrellarRef = storageRef.child(User.class.getSimpleName()).child(post.getUserId()).child("images").child("posts").child("post_" + post.getId());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 90, baos);
        final byte[] data = baos.toByteArray();

        UploadTask uploadTask = interestrellarRef.putBytes(data);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                post.setMediaUrl(downloadUrl.toString());
                dataRef.updateChildren(childUpdates);
                //postRepository.updatePost(post);
            }
        });
    }


}
