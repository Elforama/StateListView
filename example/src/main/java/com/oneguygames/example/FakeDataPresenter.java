package com.oneguygames.example;

import com.oneguygames.example.data.FakeDataSource;
import com.oneguygames.statelistview.interfaces.Paginate;

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
                    view.onUpdateView(null);
                }
                hasMoreData = true;
                isLoadingPage = false;
            }
        });
    }

    @Override
    public boolean isLoadingPage()
    {
        return isLoadingPage;
    }

    @Override
    public boolean hasMorePages()
    {
        return hasMoreData;
    }

    public interface OnDataListener
    {
        void onUpdateView(List<String> data);
        void onShowError(String error);
    }
}
