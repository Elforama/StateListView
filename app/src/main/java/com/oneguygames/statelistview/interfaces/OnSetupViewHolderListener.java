package com.oneguygames.statelistview.interfaces;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public interface OnSetupViewHolderListener
{
    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);
    void onBindViewHolder(RecyclerView.ViewHolder holder, int position, Object data);
}
