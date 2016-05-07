package com.oneguygames.statelistview.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneguygames.statelistview.R;
import com.oneguygames.statelistview.interfaces.Controllable;

/**
 * Created by jonathanmuller on 5/7/16.
 */
public class StateView extends LinearLayout implements Controllable
{
    public ImageView iconImageView;
    public TextView descriptionTextView;

    public StateView(Context context)
    {
        super(context);
        init(context);
    }

    public StateView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.default_empty_state_view, this, true);

        iconImageView = (ImageView) view.findViewById(R.id.state_imageView);
        descriptionTextView = (TextView) view.findViewById(R.id.state_textView);
    }

    @Override
    public void setImage(int drawableRes)
    {
        iconImageView.setImageResource(drawableRes);
    }

    @Override
    public void setMessage(String message)
    {
        descriptionTextView.setText(message);
    }

    @Override
    public void setClickListener(OnClickListener onClickListener)
    {
        descriptionTextView.setOnClickListener(onClickListener);
    }
}
