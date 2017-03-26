package com.gabrielgomarques.firebasetest.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.gabrielgomarques.firebasetest.R;
import com.gabrielgomarques.firebasetest.data.local.util.SQLiteUserRepository;
import com.gabrielgomarques.firebasetest.enitities.User;
import com.gabrielgomarques.firebasetest.ui.activity.HomeActivity;
import com.gabrielgomarques.firebasetest.ui.activity.LoginActivity;
import com.gabrielgomarques.firebasetest.ui.activity.NavigationListener;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Gabriel on 08/01/2017.
 */

public class UtilFactory {

    private static final String CLASS_NAME = UtilFactory.class.getName();

    public static  Drawer buildDraweLayout(final NavigationListener listener,final Activity context, long positionAtOptions){

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIcon(FontAwesome.Icon.faw_home).withName("Home").withIdentifier(1);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIcon(FontAwesome.Icon.faw_user).withName("Profile").withIdentifier(2);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_perm_data_setting).withName("Settings").withIdentifier(3);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIcon(FontAwesome.Icon.faw_sign_out).withName("Log Out").withIdentifier(4);

        final Drawer drawerLayout = new DrawerBuilder().withActivity(context).build();

        drawerLayout.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                drawerLayout.deselect(listener.onSelectItem(position));
                drawerItem.withSetSelected(true);
                drawerLayout.closeDrawer();
                return  true;
            }
        });
        drawerLayout.addItems(item1,item2,item3,item4);

        drawerLayout.setSelection(positionAtOptions);

        return drawerLayout;
    }
    public static void buildHeader(final User user, final IProfile profile, final Activity activity, final Drawer drawerLayout) {
        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... strings) {
                Bitmap bitmap = null;
                try (InputStream is = new URL(user.getImageUrl()).openStream()) {
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    FirebaseCrash.report(e);
                }

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null)
                    buildDrawerHeader(bitmap,profile,user,activity,drawerLayout);
            }
        };
        task.execute();
    }

    private static void buildDrawerHeader(Bitmap bitmap,IProfile profile,User user,Activity activity,Drawer drawerLayout) {

        profile = new ProfileDrawerItem().withName(user.getName()).withEmail(user.getEmail()).withIcon(bitmap);

        AccountHeader drawerHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.drawer_header_wallpapper)
                .addProfiles(profile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                }).build();

        drawerLayout.setHeader(drawerHeader.getView());
    }

}
