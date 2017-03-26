package com.gabrielgomarques.firebasetest.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.gabrielgomarques.firebasetest.ui.activity.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gabriel on 07/01/2017.
 */

public class MediaUtil {
    public static void setBitmap(final String url, final ImageView postImage, final ProgressBar progressBar, final Context context) {

        progressBar.setVisibility(View.VISIBLE);

        //Using picasso to scale and compress bitmap to the imageview size
        Picasso.with(context).load(Uri.parse(url)).fit().centerInside().into(postImage, new Callback() {

            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
    }
}
