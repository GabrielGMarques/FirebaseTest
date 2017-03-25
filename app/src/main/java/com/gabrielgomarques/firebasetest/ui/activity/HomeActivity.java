package com.gabrielgomarques.firebasetest.ui.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.gabrielgomarques.firebasetest.R;
import com.gabrielgomarques.firebasetest.data.firebase.UserRepository;
import com.gabrielgomarques.firebasetest.data.local.util.SQLiteUserRepository;
import com.gabrielgomarques.firebasetest.enitities.User;
import com.gabrielgomarques.firebasetest.ui.fragment.PostFragment;
import com.gabrielgomarques.firebasetest.ui.fragment.PostModalFragment;
import com.gabrielgomarques.firebasetest.ui.fragment.ProfileFragment;
import com.gabrielgomarques.firebasetest.util.UtilFactory;
import com.gabrielgomarques.firebasetest.util.resources.PictureTakerUtil;
import com.google.firebase.crash.FirebaseCrash;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.content_home_layout)
    public RelativeLayout contentHomeLayout;

    @BindView(R.id.profile_data_progress)
    public ProgressBar profileDataProgress;

    @BindView(R.id.home_toolbar)
    public Toolbar homeToolbar;

    @BindView(R.id.action_drawer_list)
    public ImageButton btnDrawerList;

    @BindView(R.id.action_search)
    public ImageButton btnSearch;

    @BindView(R.id.search_posts_field)
    public EditText editSearch;


    private FloatingActionButton btnCamera;


    private User user;

    private static final int IMAGE_PICK_REQUEST_CODE = 1;

    private UserRepository userRepository;

    private SQLiteUserRepository sqLiteUserRepository;

    private final OnUserRequestListener userRequestListener = new OnUserRequestListener();

    private static Drawer drawerLayout;

    private AccountHeader drawerHeader;

    private Uri imageUri;

    private IProfile profile;

    //Fragments of application, with position at drawer list as key
    private HashMap<Integer, Fragment> appFragments = new HashMap<Integer, Fragment>();

    private Integer currentFragmentPosition = 1;

    public boolean hasReadExternalStorePermission = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setSupportActionBar(homeToolbar);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        userRepository = UserRepository.getInstance();

        sqLiteUserRepository = new SQLiteUserRepository(this);

        Bundle bundle = intent.getExtras();

        initializeViews(bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_activity_menu, menu);
        MenuItem item = menu.getItem(0);

        return true;
    }

    private void initializeViews(Bundle bundle) {
        contentHomeLayout.setVisibility(View.INVISIBLE);
        profileDataProgress.setVisibility(View.VISIBLE);

        this.user = (User) bundle.get("user");

        userRepository.getById(user.getUserId(), userRequestListener);

        drawerLayout = UtilFactory.buildDraweLayout(this, 1);

        editSearch.setVisibility(View.INVISIBLE);


        btnDrawerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer();
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editSearch.getVisibility() == View.INVISIBLE) {
                    btnDrawerList.setVisibility(View.GONE);
                    editSearch.setVisibility(View.VISIBLE);
                } else {
                    btnDrawerList.setVisibility(View.VISIBLE);
                    editSearch.setVisibility(View.INVISIBLE);
                }
            }
        });

        homeToolbar.inflateMenu(R.menu.home_activity_menu);

        this.buildFragments();

        View view = FloatingActionButton.inflate(this, R.layout.camera_button, (ViewGroup) findViewById(R.id.fragment_container));

        btnCamera = (FloatingActionButton) view.findViewById(R.id.btn_camera);

        hasReadExternalStorePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        if (hasReadExternalStorePermission) {
            setPostButtonClickListener();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }

        getFragmentManager().beginTransaction().add(R.id.fragment_container, appFragments.get(1)).commit();

    }

    private void buildFragments() {
        PostFragment postFragment = new PostFragment();
        postFragment.setUser(user);
        appFragments.put(1, postFragment);

        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setUserId(user.getUserId());
        appFragments.put(2, profileFragment);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == PictureTakerUtil.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                PostModalFragment postModalFragment = new PostModalFragment();
                postModalFragment.show(ft, "dialog");
//                postModalFragment.setPostBitmap(MediaStore.Images.Media.getBitmap(
//                        getContentResolver(), imageUri));
                postModalFragment.setImageUri(imageUri);
                postModalFragment.setUser(this.user);
            }
        } catch (Exception e) {
            Log.e(HomeActivity.class.getName(), e.getMessage() + "");
            FirebaseCrash.report(e);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (!hasReadExternalStorePermission)
            hasReadExternalStorePermission = Arrays.asList(permissions).contains(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasReadExternalStorePermission)
            setPostButtonClickListener();

    }

    private void setPostButtonClickListener() {


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = PictureTakerUtil.dispatchTakePictureIntent(HomeActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen()){
            drawerLayout.closeDrawer();
        }else{
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    public void changeContext(Integer position) {

        if (position != this.currentFragmentPosition) {
            try {
                if(position==4) {
                    sqLiteUserRepository.deleteCurrentUserData();
                    Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                    this.currentFragmentPosition = position;
                    finish();
                    startActivity(i);
                    return;
                }
                getFragmentManager().beginTransaction().remove(appFragments.get(this.currentFragmentPosition)).add(R.id.fragment_container, appFragments.get(2)).commit();
                this.currentFragmentPosition = position;

//                switch (position) {
//                    case 1:
//                        getFragmentManager().beginTransaction().add(R.id.fragment_container, appFragments.get(1)).commit();
//                        this.currentFragmentPosition = position;
//                        break;
//                    case 2:
//                        getFragmentManager().beginTransaction().remove(appFragments.get(this.currentFragmentPosition));
//                        getFragmentManager().beginTransaction().add(R.id.fragment_container, appFragments.get(2)).commit();
//                        this.currentFragmentPosition = position;
//                        break;
//                }
            } catch (Exception e) {
                Log.d(HomeActivity.class.getName(), e.getMessage() + "");
                FirebaseCrash.report(e);
            }
        }

    }

    public class OnUserRequestListener implements RequestListener<User> {

        @Override
        public void onStartRequest() {
            contentHomeLayout.setVisibility(View.INVISIBLE);
            profileDataProgress.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFinishRequest() {
            contentHomeLayout.setVisibility(View.VISIBLE);
            profileDataProgress.setVisibility(View.INVISIBLE);
        }


        @Override
        public void onSuccessRequest(User obj) {

            try {
                user = obj;
                sqLiteUserRepository.updateUser(user);
                UtilFactory.buildHeader(user, profile, HomeActivity.this, drawerLayout);
            } catch (Exception e) {
                FirebaseCrash.report(e);
            }

        }

        @Override
        public void onFailRequest() {

        }
    }
}
