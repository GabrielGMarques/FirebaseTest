package com.gabrielgomarques.firebasetest.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gabrielgomarques.firebasetest.R;
import com.gabrielgomarques.firebasetest.data.firebase.UserRepository;
import com.gabrielgomarques.firebasetest.data.local.util.SQLiteUserRepository;
import com.gabrielgomarques.firebasetest.enitities.User;
import com.gabrielgomarques.firebasetest.util.UtilFactory;
import com.gabrielgomarques.firebasetest.util.resources.PictureTakerUtil;
import com.google.firebase.crash.FirebaseCrash;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.InputStream;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.profile_data)
    public LinearLayout profileData;

    @BindView(R.id.profile_image_view)
    public CircleImageView circleImageView;

    @BindView(R.id.profile_name)
    public TextView profileName;

    @BindView(R.id.profile_introduction)
    public TextView profileIntroduction;

    @BindView(R.id.profile_data_edit)
    public LinearLayout profileDataEdit;

    @BindView(R.id.profile_image_view_edit)
    public CircleImageView circleImageViewEdit;

    @BindView(R.id.profile_name_edit)
    public EditText profileNameEdit;

    @BindView(R.id.profile_introduction_edit)
    public EditText profileIntroductionEdit;

    @BindView(R.id.edit_data_btn)
    public FloatingActionButton btnEditProfileData;

    @BindView(R.id.edit_save_data_btn)
    public FloatingActionButton btnSaveEditProfileData;

    @BindView(R.id.edit_cancel_data_btn)
    public FloatingActionButton btnCancelEditProfileData;

    @BindView(R.id.content_home_layout)
    public RelativeLayout contentHomeLayout;

    @BindView(R.id.profile_data_progress)
    public ProgressBar profileDataProgress;


    private User user;

    private static final int IMAGE_PICK_REQUEST_CODE = 1;

    private UserRepository userRepository;

    private SQLiteUserRepository sqLiteUserRepository;

    private final ProfileActivity.OnUserRequestListener userRequestListener = new ProfileActivity.OnUserRequestListener();

    private static Drawer drawerLayout;

    private AccountHeader drawerHeader;
    private IProfile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);


        ButterKnife.bind(this);

        Intent intent = getIntent();

        userRepository = UserRepository.getInstance();

        sqLiteUserRepository = new SQLiteUserRepository(this);

        Bundle bundle = intent.getExtras();

        initializeViews(bundle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


    }

    private void initializeViews(Bundle bundle) {
        contentHomeLayout.setVisibility(View.INVISIBLE);
        profileDataProgress.setVisibility(View.VISIBLE);

        this.user = (User) bundle.get("user");

        userRepository.getById(user.getUserId(), userRequestListener);

        updateProfileData(true);

        toogleViews(false);

        }

    private void updateProfileData(boolean initalize) {
        profileName.setText((this.user.getName() == null || this.user.getName().trim().isEmpty()) ? "There is not a name" : this.user.getName());

        profileIntroduction.setText((this.user.getIntroduction() == null || this.user.getIntroduction().trim().isEmpty()) ? "There is not an introduction...." : this.user.getIntroduction());

        if (user.getImageUrl() != null) {
            setBitmap(this.user.getImageUrl(), circleImageView);
            setBitmap(this.user.getImageUrl(), circleImageViewEdit);
        } else {
            circleImageView.setImageResource(R.drawable.user);
            circleImageViewEdit.setImageResource(R.drawable.user);
        }
        if (!initalize) {
            UtilFactory.buildHeader(user,profile,this,drawerLayout);
        }
    }

    private void updateProfileDataEdit() {
        profileNameEdit.setText((this.user.getName() == null || this.user.getName().trim().isEmpty()) ? "There is not a name" : this.user.getName());

        profileIntroductionEdit.setText((this.user.getIntroduction() == null || this.user.getIntroduction().trim().isEmpty()) ? "There is not an introduction...." : this.user.getIntroduction());

        if (user.getImageUrl() != null)
            setBitmap(this.user.getImageUrl(), circleImageViewEdit);
        else
            circleImageViewEdit.setImageResource(R.drawable.user);

    }

    private void setBitmap(final String url, final CircleImageView imageView) {

        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... strings) {
                Bitmap bitmap = null;
                try (InputStream is = new URL(url).openStream()) {
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    FirebaseCrash.report(e);
                }

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null)
                    imageView.setImageBitmap(bitmap);
            }
        };
        task.execute();
    }





    public void toogleViews(boolean inEditing) {

        if (inEditing) {
            profileDataEdit.setVisibility(View.VISIBLE);
            btnSaveEditProfileData.setVisibility(View.VISIBLE);
            btnCancelEditProfileData.setVisibility(View.VISIBLE);
            btnEditProfileData.setVisibility(View.GONE);
            profileData.setVisibility(View.GONE);
            updateProfileDataEdit();
        } else {
            profileDataEdit.setVisibility(View.GONE);
            btnSaveEditProfileData.setVisibility(View.GONE);
            btnCancelEditProfileData.setVisibility(View.GONE);
            btnEditProfileData.setVisibility(View.VISIBLE);
            profileData.setVisibility(View.VISIBLE);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.profile_image_view_edit)
    public void doActionOpenImagePicker() {
        PictureTakerUtil.dispatchTakePictureIntent(this);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.edit_data_btn)
    public void doActionEditProfileData() {
        toogleViews(true);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.edit_save_data_btn)
    public void doActionSaveProfileData() {
        user.setName(profileNameEdit.getText().toString());
        user.setIntroduction(profileIntroductionEdit.getText().toString());
        this.circleImageViewEdit.setDrawingCacheEnabled(true);
        this.circleImageViewEdit.buildDrawingCache();
        toogleViews(false);
        userRepository.saveUser(user, this.circleImageViewEdit.getDrawingCache(), userRequestListener);

    }

    @SuppressWarnings("unused")
    @OnClick(R.id.edit_cancel_data_btn)
    public void doActionCancelEditProfileData() {
        toogleViews(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PictureTakerUtil.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            circleImageViewEdit.setImageBitmap(imageBitmap);
        }
    }

    public class OnUserRequestListener implements DependsOfFirebaseDataActivity<User> {

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
                updateProfileData(false);
                sqLiteUserRepository.updateUser(user);
            } catch (Exception e) {
                FirebaseCrash.report(e);
            }

        }

        @Override
        public void onFailRequest() {

        }
    }
}
