package com.oneguygames.statelistview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

    private final int TYPE_ITEM = 1;
    private final int TYPE_PROGRESS = 0;

    private List<T> data = new ArrayList<>();

    public PaginatedRecyclerViewAdapter(final Paginate paginate, RecyclerView recyclerView)
    {
        final LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                // We only check for for pagination when scrolling down
                if (dy > 0)
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
                    else if (paginate.isLoading()) // show loading spinner
                    {
                        data.add(null);
                        notifyItemInserted(data.size() - 1);
                    }
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_ITEM)
        {
            return onCreateItemViewHolder(parent);
        }
        else if (viewType == TYPE_PROGRESS)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_footer_progress, null);
            return new ProgressViewHolder(view);
        }
        else
        {
            // TODO: 4/30/16 add header logic here
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

    }

    public abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent);

    public void insertData(List<T> newData)
    {
        // If last item is null, remove it as it is used to show the progress spinner
        if (data.get(data.size() - 1) == null)
        {
            int removePos = data.size() - 1;
            data.remove(removePos);
            notifyItemRemoved(removePos);
        }

        int initialSize = this.data.size();
        this.data.addAll(newData);
        notifyItemRangeInserted(initialSize, newData.size());
    }

    @Override
    public int getItemViewType(int position)
    {
        return data.get(position) != null ? TYPE_ITEM : TYPE_PROGRESS;
    }

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
