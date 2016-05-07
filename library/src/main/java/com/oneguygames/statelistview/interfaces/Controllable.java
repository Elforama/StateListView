package com.oneguygames.statelistview.interfaces;

import android.view.View;

/**
 * Created by jonathanmuller on 5/7/16.
 */
public interface Controllable
{
    void setImage(int drawableRes);
    void setMessage(String message);
    void setClickListener(View.OnClickListener onClickListener);
}
