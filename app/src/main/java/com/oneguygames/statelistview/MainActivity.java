package com.oneguygames.statelistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneguygames.statelistview.adapters.PaginatedRVAdapter;
import com.oneguygames.statelistview.adapters.PaginatedSLVAdapter;
import com.oneguygames.statelistview.interfaces.OnSetupVHListener;
import com.oneguygames.statelistview.viewholders.HeaderViewHolder;
import com.oneguygames.statelistview.viewholders.ListItemViewHolder;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FakeDataPresenter.OnDataListener
{
    private static final String TAG = "MainActivity";
    private StateListView stateListView;
    private static FakeDataPresenter presenter;
    private PaginatedSLVAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateListView = (StateListView)findViewById(R.id.stateListView);
        stateListView.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 3));

        if (presenter == null)
        {
            presenter = new FakeDataPresenter();
        }

        adapter = new PaginatedSLVAdapter<>(presenter, stateListView,
                new OnSetupVHListener()
        {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                if (viewType == PaginatedRVAdapter.TYPE_HEADER)
                {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header_simple, parent, false);
                    return new HeaderViewHolder(view);
                }
                else
                {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                    return new ListItemViewHolder(view);
                }
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, Object data)
            {
                if (holder instanceof ListItemViewHolder)
                {
                    ((ListItemViewHolder) holder).load((String) data);
                }
                else
                {
                    ((HeaderViewHolder) holder).load("Cool Adapter", "Super Cool!");
                }
            }
        });
        adapter.enableHeader(true);
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
        adapter.insertData(data);
    }

    @Override
    public void onShowError(String error)
    {
        stateListView.onShowError();
    }
}
