package com.oneguygames.statelistview.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.oneguygames.statelistview.R;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder
{
    public TextView titleTextView;
    public TextView descriptionTextView;

    public HeaderViewHolder(View itemView)
    {
        super(itemView);

        titleTextView = (TextView) itemView.findViewById(R.id.title_textView);
        descriptionTextView = (TextView) itemView.findViewById(R.id.description_textView);
    }

    public void load(String title, String desc)
    {
        titleTextView.setText(title);
        descriptionTextView.setText(desc);
    }
}
