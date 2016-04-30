package com.oneguygames.statelistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * Created by jonathanmuller on 4/29/16.
 */
public class StateListView extends LinearLayout
{
    private SwipeRefreshLayout contentRefreshLayout;
    private SwipeRefreshLayout emptyRefreshLayout;
    private SwipeRefreshLayout errorRefreshLayout;

    private View progressLayout;
    private ProgressBar progressBar;

    private FrameLayout emptyStateContainer;
    private FrameLayout errorStateContainer;

    private RecyclerView recyclerView;

    private View emptyStateView;
    private View errorStateView;

    public StateListView(Context context)
    {
        super(context);
        init(context, null);
    }

    public StateListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public StateListView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        View view = inflate(getContext(), R.layout.state_list_view, this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        contentRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.content_refresh_layout);
        emptyRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.empty_refresh_layout);
        errorRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.error_refresh_layout);

        progressLayout = view.findViewById(R.id.loading_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        emptyStateContainer = (FrameLayout) view.findViewById(R.id.empty_state_container);
        errorStateContainer = (FrameLayout) view.findViewById(R.id.error_state_container);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.StateListView);

        int layoutId = attributes.getResourceId(R.styleable.StateListView_empty_state_view, R.layout.default_empty_state_view);

        emptyStateView = inflate(getContext(), layoutId, null);
        emptyStateContainer.addView(emptyStateView);

//        errorStateView = inflate(getContext(), layoutId, null);
//        errorRefreshLayout.addView(errorStateView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        contentRefreshLayout.setEnabled(false);
    }

    public void showLoading()
    {
        progressLayout.setVisibility(VISIBLE);
        contentRefreshLayout.setVisibility(GONE);
        emptyRefreshLayout.setVisibility(GONE);
        errorRefreshLayout.setVisibility(GONE);
    }

    public void showContent()
    {
        progressLayout.setVisibility(GONE);
        contentRefreshLayout.setVisibility(VISIBLE);
        emptyRefreshLayout.setVisibility(GONE);
        errorRefreshLayout.setVisibility(GONE);
    }

    public void showEmpty()
    {
        progressLayout.setVisibility(GONE);
        contentRefreshLayout.setVisibility(GONE);
        emptyRefreshLayout.setVisibility(VISIBLE);
        errorRefreshLayout.setVisibility(GONE);
    }

    public void showError()
    {
        progressLayout.setVisibility(GONE);
        contentRefreshLayout.setVisibility(GONE);
        emptyRefreshLayout.setVisibility(GONE);
        errorRefreshLayout.setVisibility(VISIBLE);
    }

    public RecyclerView getRecyclerView()
    {
        return recyclerView;
    }

    public View getEmptyStateView()
    {
        return emptyStateView;
    }

    public View getErrorStateView()
    {
        return errorStateView;
    }
}
