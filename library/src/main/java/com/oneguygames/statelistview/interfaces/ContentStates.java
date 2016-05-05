package com.oneguygames.statelistview.interfaces;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public interface ContentStates
{
    void onShowContent();
    void onShowLoading();
    void onShowEmpty();
    void onShowError();
}
