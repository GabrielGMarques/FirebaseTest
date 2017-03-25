package com.gabrielgomarques.firebasetest.ui.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gabrielgomarques.firebasetest.R;
import com.gabrielgomarques.firebasetest.data.firebase.PostRepository;
import com.gabrielgomarques.firebasetest.enitities.Post;
import com.gabrielgomarques.firebasetest.enitities.User;
import com.gabrielgomarques.firebasetest.ui.activity.RequestListener;
import com.squareup.picasso.Picasso;

import java.util.Date;


public class PostModalFragment extends DialogFragment {


    //region Object Views
    public ImageView imageView;

    public EditText editPostDescription;

    public TextView totalLastOfDescriptionCaracteres;

    public Button btnCancelPost;

    public Button btnPost;

    //endregion

    //region Objects
    private OnFragmentInteractionListener mListener;

    private Uri imageUri;

    private User user;

    private PostRepository postRepository = PostRepository.getInstance();

    private RequestDataListenerImpl postDataListenerImpl = new RequestDataListenerImpl();

    private PostFragment postFragment;

    //endregion

    //region Life circle methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_modal, container, false);
        imageView = (ImageView) view.findViewById(R.id.post_image);
        btnPost = (Button) view.findViewById(R.id.btn_post);
        btnCancelPost = (Button) view.findViewById(R.id.btn_cancel_post);
        editPostDescription = (EditText) view.findViewById(R.id.post_description);
        totalLastOfDescriptionCaracteres = (TextView) view.findViewById(R.id.total_rest_of_caracteres);

        //imageView.setImageBitmap(postBitmap);
        Picasso.with(view.getContext()).load(imageUri).into(imageView);

        setListeners();

        return view;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            mListener = new OnFragmentInteractionListenerImpl();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setListeners(){
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post();
                post.setDescription(editPostDescription.getText().toString());
                post.setAuthorName(user.getName());
                post.setUserId(user.getUserId());
                post.setDatePost(new Date());
                PostModalFragment.this.dismiss();
                postRepository.savePost(post, ((BitmapDrawable)imageView.getDrawable()).getBitmap());
            }
        });
        btnCancelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostModalFragment.this.dismiss();
            }
        });
        editPostDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text  = editable.toString().trim();

                int totalLast = 240-text.length();

                totalLastOfDescriptionCaracteres.setText(String.valueOf(totalLast));
            }
        });
    }
    //endregion

    //region Inner classes and interfaces
    public class OnFragmentInteractionListenerImpl implements OnFragmentInteractionListener {

        @Override
        public void onFragmentInteraction(Uri uri) {

        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class RequestDataListenerImpl implements RequestListener<Post> {

        @Override
        public void onStartRequest() {

        }

        @Override
        public void onFinishRequest() {

        }

        @Override
        public void onSuccessRequest(Post obj) {
            postFragment.requestDataListener.onSuccessRequest(obj);
        }

        @Override
        public void onFailRequest() {

        }
    }

    //endregion


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PostFragment getPostFragment() {
        return postFragment;
    }

    public void setPostFragment(PostFragment postFragment) {
        this.postFragment = postFragment;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    //endregion
}
