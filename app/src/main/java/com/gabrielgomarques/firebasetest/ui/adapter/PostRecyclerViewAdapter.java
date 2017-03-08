package com.gabrielgomarques.firebasetest.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gabrielgomarques.firebasetest.R;
import com.gabrielgomarques.firebasetest.enitities.Post;
import com.gabrielgomarques.firebasetest.ui.fragment.PostFragment;
import com.gabrielgomarques.firebasetest.util.Consts;
import com.gabrielgomarques.firebasetest.util.MediaUtil;

import java.util.List;


public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {

    private final List<Post> mValues;
    private final PostFragment.OnListFragmentInteractionListener mListener;
    private View view;

    public PostRecyclerViewAdapter(List<Post> items, PostFragment.OnListFragmentInteractionListener listener, final View view) {
        mValues = items;
        mListener = listener;
        this.view = view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        MediaUtil.setBitmap(holder.mItem.getMediaUrl(), holder.postPicture, holder.progressBar, holder.postImageWrapper, this.view.getContext(), true);
        holder.textAuthor.setText(String.valueOf(holder.mItem.getAuthorName()));
        holder.textDecription.setText(String.valueOf(holder.mItem.getDescription()));
        holder.postDate.setText(Consts.getTimeDiference(holder.mItem.getDatePost()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout postImageWrapper;
        public final View mView;
        public final ImageView postPicture;
        public final ProgressBar progressBar;
        public final TextView textAuthor;
        public final TextView postDate;
        public final TextView textDecription;
        public Post mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            postImageWrapper = (RelativeLayout) view.findViewById(R.id.post_image_wrapper);
            postPicture = (ImageView) view.findViewById(R.id.post_image);
            progressBar = (ProgressBar) view.findViewById(R.id.media_progress);
            textAuthor = (TextView) view.findViewById(R.id.post_author);
            textDecription = (TextView) view.findViewById(R.id.post_description);
            postDate = (TextView) view.findViewById(R.id.post_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textDecription.getText() + "'";
        }
    }


}
