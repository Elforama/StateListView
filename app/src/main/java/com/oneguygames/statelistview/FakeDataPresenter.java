package com.oneguygames.statelistview;

import com.oneguygames.statelistview.data.FakeDataSource;

import java.util.List;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public class FakeDataPresenter implements Paginate
{
    private OnDataListener view;
    private FakeDataSource dataSource;
    private boolean isLoadingPage = false;
    private boolean hasMoreData = true;

    public FakeDataPresenter()
    {
        dataSource = new FakeDataSource();
    }

    public void onBindView(OnDataListener view)
    {
        this.view = view;
    }

    public void unBindView()
    {
        view = null;
    }

    @Override
    public void onLoadPage()
    {
        isLoadingPage = true;

        dataSource.getData(new FakeDataSource.OnReceiveDataListener()
        {
            @Override
            public void onReceiveData(List<String> data)
            {
                if (view != null)
                {
                    view.onUpdateView(data);
                }
                hasMoreData = true;
                isLoadingPage = false;
            }
        });
    }

    @Override
    public boolean isLoading()
    {
        return isLoadingPage;
    }

    @Override
    public boolean hasMore()
    {
        return hasMoreData;
    }

    public interface OnDataListener
    {
        void onUpdateView(List<String> data);
        void onShowError(String error);
    }
}