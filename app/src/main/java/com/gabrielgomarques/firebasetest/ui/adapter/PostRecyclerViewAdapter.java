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


public class PostRecyclerViewAdapter extends AbstractRecyclerViewFooterAdapter<Post>{

//    private final List<Post> mValues;


    public PostRecyclerViewAdapter(RecyclerView recyclerView, List<Post> dataSet) {
        super(recyclerView, dataSet);
    }
//
//    public PostRecyclerViewAdapteonBindBasicItemViewr(List<Post> items, PostFragment.OnListFragmentInteractionListener listener, final View view) {
//        mValues = items;
//        mListener = listener;
//        this.view = view;
//    }



    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card_item, parent, false);
        return new ViewPostHolder(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        final ViewPostHolder postHolder = (ViewPostHolder) holder;
        postHolder.mItem = getDataSet().get(position);

        MediaUtil.setBitmap(postHolder.mItem.getMediaUrl(), postHolder.postPicture, postHolder.progressBar, this.view.getContext());
        postHolder.textAuthor.setText(String.valueOf(postHolder.mItem.getAuthorName()));
        postHolder.textDecription.setText(String.valueOf(postHolder.mItem.getDescription()));
        postHolder.postDate.setText(Consts.getTimeDiference(postHolder.mItem.getDatePost()));
//        postHolder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(postHolder.mItem);
//                }
//            }
//        });
    }


//    @Override
//    public int getItemCount() {
//        return mValues.size();
//    }

    public class ViewPostHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout postImageWrapper;
        public final View mView;
        public final ImageView postPicture;
        public final ProgressBar progressBar;
        public final TextView textAuthor;
        public final TextView postDate;
        public final TextView textDecription;
        public Post mItem;

        public ViewPostHolder(View view) {
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
