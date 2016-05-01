package com.oneguygames.statelistview.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.oneguygames.statelistview.StateListView;
import com.oneguygames.statelistview.interfaces.OnSetupViewHolderListener;
import com.oneguygames.statelistview.interfaces.Paginate;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public class PaginatedStateListViewAdapter<T> extends PaginatedRecyclerViewAdapter<T>
{
    private OnSetupViewHolderListener listener;

    public PaginatedStateListViewAdapter(Paginate paginate, StateListView stateListView, OnSetupViewHolderListener listener)
    {
        super(paginate, stateListView.getRecyclerView(), stateListView);
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType)
    {
        return listener.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, Object data)
    {
        listener.onBindViewHolder(holder, position, data);
    }
}
