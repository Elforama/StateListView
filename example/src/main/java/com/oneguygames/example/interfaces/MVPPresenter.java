package com.oneguygames.example.interfaces;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public interface MVPPresenter<V extends MvpView>
{
    void onBindView(V view);
    void onUnbindView();
}
