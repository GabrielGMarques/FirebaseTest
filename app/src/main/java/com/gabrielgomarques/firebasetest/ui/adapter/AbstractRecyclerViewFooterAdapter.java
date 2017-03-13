package com.gabrielgomarques.firebasetest.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.gabrielgomarques.firebasetest.R;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mSobhy on 7/22/15.
 */
public abstract class AbstractRecyclerViewFooterAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VISIBLE_THRESHOLD = 1;

    private final int ITEM_VIEW_TYPE_BASIC = 0;
    private final int ITEM_VIEW_TYPE_FOOTER = 1;

    private List<T> dataSet;

    private int firstVisibleItem, visibleItemCount, totalItemCount, previousTotal, lastCompletelyVisibleItemPosition = 0;
    private boolean loading = false;

    protected View view;

    private ProgressViewHolder progressViewHolder;

    public AbstractRecyclerViewFooterAdapter(RecyclerView recyclerView, List<T> dataSet) {

        this.view = recyclerView;

        this.dataSet = dataSet;

    }

    public void buildScrollListener(RecyclerView recyclerView, final OnLoadMoreListener onLoadMoreListener) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    totalItemCount = linearLayoutManager.getItemCount();
//                    visibleItemCount = linearLayoutManager.getChildCount();
//                 firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//
//
//                    if (loading) {
//                        if (totalItemCount > previousTotal) {
//                            loading = false;
//                            previousTotal = totalItemCount;
//                        }
//                    }
//                    if (!loading && (totalItemCount - visibleItemCount)
//                            < (firstVisibleItem + VISIBLE_THRESHOLD)) {
//                        // End has been reached
//
//                        addItem(null);


                    if (lastCompletelyVisibleItemPosition == (totalItemCount - 1) && !loading) {
                        addItem(null);
                        onLoadMoreListener.onLoadMore();
                        loading = true;
                    }
//                        loading = true;
//                    }
                }
            });
        }
    }

    public int getFirstVisibleItem() {
        return firstVisibleItem;
    }

    public void resetItems(@NonNull List<T> newDataSet) {
        loading = true;
        firstVisibleItem = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        previousTotal = 0;

        dataSet.clear();
        addItems(newDataSet);
    }

    public void addItems(@NonNull List<T> newDataSetItems) {
        dataSet.addAll(newDataSetItems);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }

    }
    public void notifyDataSetChangedOvirreded(){
        super.notifyDataSetChanged();
        if (loading) {
            removeItem(null);
            loading = false;
        }
    }

    public void removeItem(T item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public T getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }

    public List<T> getDataSet() {
        return dataSet;
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) != null ? ITEM_VIEW_TYPE_BASIC : ITEM_VIEW_TYPE_FOOTER;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_BASIC) {
            return onCreateBasicItemViewHolder(parent, viewType);
        } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent, viewType);
        } else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder genericHolder, int position) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE_BASIC) {
            onBindBasicItemView(genericHolder, position);
        } else {
            onBindFooterView(genericHolder, position);
        }
    }

    public abstract RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindBasicItemView(RecyclerView.ViewHolder genericHolder, int position);

    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        //noinspection ConstantConditions
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_bar, parent, false);
        return (progressViewHolder = new ProgressViewHolder(v));
    }

    public void onBindFooterView(RecyclerView.ViewHolder genericHolder, int position) {
        ((ProgressViewHolder) genericHolder).progressBar.setIndeterminate(true);
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progressBar)
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
