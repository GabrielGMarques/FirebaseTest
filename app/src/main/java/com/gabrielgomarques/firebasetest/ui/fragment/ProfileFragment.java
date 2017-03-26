package com.gabrielgomarques.firebasetest.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gabrielgomarques.firebasetest.R;
import com.gabrielgomarques.firebasetest.data.firebase.UserRepository;
import com.gabrielgomarques.firebasetest.enitities.User;
import com.gabrielgomarques.firebasetest.ui.activity.MenuOptionsBridge;
import com.gabrielgomarques.firebasetest.ui.activity.RequestListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    //region views
    @BindView(R.id.profile_image_view)
    CircleImageView imageViewProfile;
    @BindView(R.id.profile_name)
    TextView profileName;

    @BindView(R.id.profile_introduction)
    TextView profileIntroduction;

    @BindView(R.id.profile_data_edit)
    LinearLayout profileDataEdit;

    @BindView(R.id.profile_data)
    LinearLayout profileData;

    //endregion
    //region Data Objects
    private UserRepository userRepository = UserRepository.getInstance();
    private User user;
    private String userId;

    //endregion
    //region Listeners
    private RequestListenerImpl requestListener = new RequestListenerImpl();
    private MenuOptionsBridge optionsBridge;
    //endregion listeners

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, 0, 0, "Option1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        ButterKnife.bind(this, view);

        userRepository.getById(userId,requestListener);
        profileDataEdit.setVisibility(View.INVISIBLE);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MenuOptionsBridge){
            optionsBridge = (MenuOptionsBridge)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public class RequestListenerImpl implements RequestListener<User>{

        @Override
        public void onStartRequest() {

        }

        @Override
        public void onFinishRequest() {

        }

        @Override
        public void onSuccessRequest(User obj) {
            user = obj;
            profileName.setText(user.getName());
            profileIntroduction.setText(user.getIntroduction());
            if(user.getImageUrl()!=null){
                Picasso.with(ProfileFragment.this.getContext()).load(Uri.parse(user.getImageUrl())).fit().centerInside().into(imageViewProfile);
            }
        }

        @Override
        public void onFailRequest() {

        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
