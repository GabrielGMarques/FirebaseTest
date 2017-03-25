package com.gabrielgomarques.firebasetest.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabrielgomarques.firebasetest.R;
import com.gabrielgomarques.firebasetest.data.firebase.PostRepository;
import com.gabrielgomarques.firebasetest.enitities.Post;
import com.gabrielgomarques.firebasetest.enitities.User;
import com.gabrielgomarques.firebasetest.ui.activity.RequestListener;
import com.gabrielgomarques.firebasetest.ui.adapter.OnLoadMoreListener;
import com.gabrielgomarques.firebasetest.ui.adapter.PostRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {


    //region Objects
    public RequestDataListenerImpl requestDataListener = new RequestDataListenerImpl();
    private PostRepository postRepository = PostRepository.getInstance();
    private User user;
    private PostRecyclerViewAdapter postRecyclerViewAdapter;
    //endregion

    //region Constants and Util Attributes
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    //endregion


    public PostFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PostFragment newInstance(int columnCount) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_fragment_recycler_view, container, false);


        final List<Post> posts = new ArrayList<Post>();


        postRecyclerViewAdapter = new PostRecyclerViewAdapter((RecyclerView) view, posts);


        postRepository.setPostRecyclerViewAdapter(postRecyclerViewAdapter);
        postRepository.getPostsByUser(posts, user, 0);
        postRecyclerViewAdapter.buildScrollListener((RecyclerView) view, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                postRepository.getPostsByUser(posts, user, posts.size()-1);
            }
        });

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;

        recyclerView.setAdapter(postRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        }

        @Override
        public void onFailRequest() {

        }
    }

    //region Getters and Setters

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //endregion
}
