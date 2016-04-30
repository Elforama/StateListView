package com.oneguygames.statelistview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public class ListItemViewHolder extends RecyclerView.ViewHolder
{
    private static final String TAG = "ListItemViewHolder";
    public TextView textView;

    public ListItemViewHolder(View itemView)
    {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.textView);
    }

    public void load(String text)
    {
        textView.setText("List item number " + text);
    }
}
