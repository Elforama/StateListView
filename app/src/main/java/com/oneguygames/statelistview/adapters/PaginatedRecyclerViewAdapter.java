package com.oneguygames.statelistview.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
public abstract class PaginatedRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private int PAGINATION_THRESHOLD = 10;
    private int itemPosition;
    private int totalItems;
    private boolean isShowingProgressBar = false;

    public static final int TYPE_HEADER = 2;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_PROGRESS = 0;

    protected List<T> data = new ArrayList<>();
    private boolean hasHeader = false;
    private ContentStates listener;

    public PaginatedRecyclerViewAdapter(final Paginate paginate, RecyclerView recyclerView, ContentStates listener)
    {
        this.listener = listener;
        recyclerView.setAdapter(this);
        final LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();

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
                if (!paginate.isLoading() && paginate.hasMore())
                {
                    itemPosition = layoutManager.findLastVisibleItemPosition();
                    totalItems = layoutManager.getItemCount();

                    if (itemPosition > totalItems - PAGINATION_THRESHOLD)
                    {
                        paginate.onLoadPage();
                    }
                }
                else if (!isShowingProgressBar && paginate.isLoading()) // show loading spinner
                {
                    isShowingProgressBar = true;
                    data.add(null);
                    notifyItemInserted(data.size() - 1);
                }
            }
        });

        paginate.onLoadPage();

        if (listener != null)
        {
            listener.onShowLoading();
        }
    }

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
            // Nothing to do here
        }
        else
        {
            onBindViewHolder(holder, position, data.get(getAdjustedPosition(position)));
        }
    }

    private boolean isHeader(int position)
    {
        return position == 0 && hasHeader;
    }

    private int getAdjustedPosition(int position)
    {
        return (hasHeader && !isHeader(position)) ? position - 1 : position;
    }

    public void enableHeader(boolean hasHeader)
    {
        this.hasHeader = hasHeader;
    }

    public void insertData(List<T> newData)
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
        }
        else
        {
            if (listener != null)
            {
                listener.onShowContent();
            }
        }

        // If last item is null, remove it as it is used to show the progress spinner
        if (!data.isEmpty() && data.get(data.size() - 1) == null)
        {
            data.remove(data.size() - 1);
            notifyItemRemoved(data.size());
        }

        int initialSize = this.data.size();
        this.data.addAll(newData);
        notifyItemRangeInserted(initialSize, newData.size());

        //notifyDataSetChanged();

        isShowingProgressBar = false;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (isHeader(position))
        {
            return TYPE_HEADER;
        }

        return data.get(getAdjustedPosition(position)) != null ? TYPE_ITEM : TYPE_PROGRESS;
    }

    @Override
    public int getItemCount()
    {
        return hasHeader ? data.size() + 1 : data.size();
    }

    public abstract RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType);
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position, Object data);

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
