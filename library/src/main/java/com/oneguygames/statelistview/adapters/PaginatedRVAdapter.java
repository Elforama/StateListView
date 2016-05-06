package com.oneguygames.statelistview.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.oneguygames.statelistview.R;
import com.oneguygames.statelistview.interfaces.ContentStates;
import com.oneguygames.statelistview.interfaces.Paginate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public abstract class PaginatedRVAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    //============================================================
    // Variables
    //============================================================

    public static final int TYPE_HEADER = 2;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_PROGRESS = 0;
    private static final String TAG = "PaginatedRVAdapter";

    private int paginationThreshold = 10;
    private int itemPosition;
    private int totalItems;
    private boolean isShowingProgressBar = false;
    private boolean hasHeader = false;
    private ContentStates listener;
    private List<T> data = new ArrayList<>();

    //============================================================
    // Constructors
    //============================================================

    public PaginatedRVAdapter(RecyclerView recyclerView, Paginate paginate)
    {
        init(recyclerView, paginate, null);
    }

    public PaginatedRVAdapter(final Paginate paginate, RecyclerView recyclerView, ContentStates listener)
    {
        init(recyclerView, paginate, listener);
    }

    private void init(RecyclerView recyclerView, final Paginate paginate, ContentStates listener)
    {
        this.listener = listener;

        if (recyclerView.getLayoutManager() == null)
        {
            Log.e(TAG, "You must to attach a LayoutManager to your RecyclerView");
            return;
        }

        final LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();

        // Allow footer and header to show as one column if the list is a grid
        if (layoutManager instanceof GridLayoutManager)
        {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager)layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    return isHeader(position) || isProgressBar(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }

        // Only check for pagination if the paginate listener is passed
        if (paginate != null)
        {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
            {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState)
                {

                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                {
                    // If not loading more items and there are more to load
                    // we check the position of the scroll.
                    // If we are close enough to the bottom, we load more
                    if (!paginate.isLoadingPage() && paginate.hasMorePages())
                    {
                        itemPosition = layoutManager.findLastVisibleItemPosition();
                        totalItems = layoutManager.getItemCount();

                        if (itemPosition > totalItems - paginationThreshold)
                        {
                            paginate.onLoadPage();
                        }
                    }

                    if (!isShowingProgressBar && paginate.isLoadingPage()) // show loading spinner
                    {
                        isShowingProgressBar = true;
                        notifyItemInserted(data.size() + (hasHeader ? 1 : 0));
                    }
                }
            });
        }

        if (listener != null)
        {
            listener.onShowLoading();
        }
    }

    //============================================================
    // Adapter Overridden Methods
    //============================================================

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_ITEM)
        {
            return onCreateCustomViewHolder(parent, viewType);
        }
        else if (viewType == TYPE_PROGRESS)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_footer_progress, parent, false);
            return new ProgressViewHolder(view);
        }
        else
        {
            return onCreateCustomViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof ProgressViewHolder)
        {
            // nothing to do
        }
        else
        {
            // call abstract methods for user to fill custom viewholders
            onBindViewHolder(holder, position, data.get(getAdjustedPosition(position)));
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (isHeader(position))
        {
            return TYPE_HEADER;
        }
        else if (isProgressBar(position))
        {
            return TYPE_PROGRESS;
        }
        else
        {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size() + (hasHeader ? 1 : 0) + (isShowingProgressBar ? 1 : 0);
    }

    //============================================================
    // Public Methods
    //============================================================

    private boolean isHeader(int position)
    {
        return position == 0 && hasHeader;
    }

    private boolean isProgressBar(int position)
    {
        return position == getItemCount() - 1 && isShowingProgressBar;
    }

    private int getAdjustedPosition(int position)
    {
        return (hasHeader && !isHeader(position)) ? position - 1 : position;
    }

    //============================================================
    // Private Methods
    //============================================================

    public void enableHeader(boolean hasHeader)
    {
        this.hasHeader = hasHeader;
    }

    public void refreshHeader()
    {
        if (hasHeader)
        {
            notifyItemChanged(0);
        }
    }

    public void updateDataAtPosition(T data, int position)
    {
        this.data.set(position, data);
        notifyItemChanged(getAdjustedPosition(position));
    }

    public void setData(List<T> newData)
    {
        if (data.isEmpty())
        {
            if (newData == null || newData.isEmpty())
            {
                if (listener != null)
                {
                    listener.onShowEmpty();
                }

                if (newData == null)
                {
                    return;
                }
            }
            else
            {
                if (listener != null)
                {
                    listener.onShowContent();
                }
            }
        }
        else
        {
            if (listener != null)
            {
                listener.onShowContent();
            }
        }

        if (isShowingProgressBar)
        {
            isShowingProgressBar = false;
        }

        //int initialSize = this.data.size();
        this.data = newData;
        //notifyItemRangeInserted(initialSize, newData.size());
        //layoutManager.scrollToPosition(initialSize + 1);
        notifyDataSetChanged();
    }

    public void addData(List<T> newData)
    {
        if (data.isEmpty())
        {
            if (newData == null || newData.isEmpty())
            {
                if (listener != null)
                {
                    listener.onShowEmpty();
                }

                if (newData == null)
                {
                    return;
                }
            }
            else
            {
                if (listener != null)
                {
                    listener.onShowContent();
                }
            }
        }
        else
        {
            if (listener != null)
            {
                listener.onShowContent();
            }
        }

        if (isShowingProgressBar)
        {
            notifyItemRemoved(getItemCount());
            isShowingProgressBar = false;
        }

        //int initialSize = this.data.size();
        this.data.addAll(newData);
        //notifyItemRangeInserted(initialSize, newData.size());
        //layoutManager.scrollToPosition(initialSize + 1);
        notifyDataSetChanged();
    }

    //============================================================
    // Abstract Methods
    //============================================================

    /**
     * Implement this method to create teh viewholders for the item viewholders and header viewholder
     * @param parent
     * @param viewType
     * @return
     */
    public abstract RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType);

    /**
     * Allows binding of custom viewholders with the provided data
     * @param holder
     * @param position
     * @param data from the list to fill view with
     */
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position, T data);

    //============================================================
    // View Holders
    //============================================================

    public static class ProgressViewHolder extends RecyclerView.ViewHolder
    {
        public ProgressBar progressBar;

        public ProgressViewHolder(View itemView)
        {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}
