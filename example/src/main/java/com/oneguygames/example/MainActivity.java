package com.oneguygames.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneguygames.statelistview.adapters.PaginatedRVAdapter;
import com.oneguygames.statelistview.views.StateListView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FakeDataPresenter.OnDataListener
{
    private static final String TAG = "MainActivity";
    private StateListView stateListView;
    private static FakeDataPresenter presenter;
    private PaginatedRVAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.oneguygames.statelistview.R.layout.activity_main);

        stateListView = (StateListView)findViewById(com.oneguygames.statelistview.R.id.stateListView);
        stateListView.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));

        if (presenter == null)
        {
            presenter = new FakeDataPresenter();
        }

        adapter = new PaginatedRVAdapter<String>(stateListView.getRecyclerView(), presenter)
        {
            @Override
            public RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType)
            {
                if (viewType == PaginatedRVAdapter.TYPE_HEADER)
                {
                    View view = LayoutInflater.from(parent.getContext()).inflate(com.oneguygames.statelistview.R.layout.list_header_simple, parent, false);
                    return new HeaderViewHolder(view);
                }
                else
                {
                    View view = LayoutInflater.from(parent.getContext()).inflate(com.oneguygames.statelistview.R.layout.list_item, parent, false);
                    return new ListItemViewHolder(view);
                }
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, String data)
            {
                if (holder instanceof ListItemViewHolder)
                {
                    ((ListItemViewHolder) holder).load(data);
                }
                else
                {
                    ((HeaderViewHolder) holder).load("Cool Adapter", "Super Cool!");
                }
            }
        };

        adapter.setOnItemSelectionListener(new PaginatedRVAdapter.OnItemSelectionListener()
        {
            @Override
            public void onItemSelected(int position, Object data, RecyclerView.ViewHolder viewHolder)
            {
                Log.d(TAG, "onItemSelected() called with: " + "position = [" + position + "], data = [" + data + "], viewHolder = [" + viewHolder + "]");
            }
        });

        adapter.enableHeader(true);
        stateListView.setEmptyStateView(new CustomView(this));
        stateListView.setAdapter(adapter);
        presenter.onLoadPage();

        stateListView.setEmptyStateMessage("Set in code! Totally custom view!");
        stateListView.setEmptyStateOnClick(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d(TAG, "Clicked!");
            }
        });
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
        stateListView.onShowContent();
    }

    @Override
    public void onShowError(String error)
    {
        stateListView.onShowError();
    }
}
