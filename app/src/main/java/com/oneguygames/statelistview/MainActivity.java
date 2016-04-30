package com.oneguygames.statelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FakeDataPresenter.OnDataListener
{

    private static final String TAG = "MainActivity";
    private StateListView stateListView;
    private static FakeDataPresenter presenter;
    private PaginatedStateListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateListView = (StateListView)findViewById(R.id.stateListView);
        stateListView.showLoading();

        if (presenter == null)
        {
            presenter = new FakeDataPresenter();
        }

        adapter = new PaginatedStateListViewAdapter(presenter, stateListView.getRecyclerView());
        stateListView.getRecyclerView().setAdapter(adapter);
        presenter.onLoadPage();
    }

    @Override
    protected void onResume()
    {
        if (presenter != null)
        {
            presenter.onBindView(this);
        }
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        if (presenter != null)
        {
            presenter.unBindView();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void onUpdateView(List<String> data)
    {
        Log.d(TAG, "onUpdateView() called with: " + "data = [" + data + "]");
        adapter.addData(data);
        stateListView.showContent();
    }

    @Override
    public void onShowError(String error)
    {
        stateListView.showError();
    }
}
