package com.oneguygames.example;

import android.app.Activity;
import android.os.Bundle;

import com.oneguygames.example.interfaces.MVPPresenter;
import com.oneguygames.example.interfaces.MvpView;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public abstract class PresenterActivity<V extends MvpView, P extends MVPPresenter> extends Activity
{
    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        presenter.onUnbindView();
        super.onPause();
    }

    public abstract P onCreatePresenter();
}
