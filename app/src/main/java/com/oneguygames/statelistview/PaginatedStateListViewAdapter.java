package com.oneguygames.statelistview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public class PaginatedStateListViewAdapter extends PaginatedRecyclerViewAdapter<ListItemViewHolder>
{
    private static final String TAG = "PaginatedStateListViewAdapter";
    private List<String> data = new ArrayList<>();

    public PaginatedStateListViewAdapter(Paginate paginate, RecyclerView recyclerView)
    {
        super(paginate, recyclerView);
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position)
    {
        holder.load(data.get(position));
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public void addData(List<String> data)
    {
        this.data.addAll(data);
        notifyDataSetChanged();
    }
}
