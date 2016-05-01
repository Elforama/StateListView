package com.oneguygames.statelistview.interfaces;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public interface MvpPresenter<V extends MvpView>
{
    void onBindView(V view);
    void onUnbindView();
}
