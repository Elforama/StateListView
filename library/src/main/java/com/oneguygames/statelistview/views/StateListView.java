package com.oneguygames.statelistview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.oneguygames.statelistview.R;
import com.oneguygames.statelistview.interfaces.ContentStates;
import com.oneguygames.statelistview.interfaces.Controllable;

/**
 * Created by jonathanmuller on 4/29/16.
 */
public class StateListView extends LinearLayout implements ContentStates
{
    private static final String TAG = "StateListView";

    private SwipeRefreshLayout contentRefreshLayout;
    private SwipeRefreshLayout emptyRefreshLayout;
    private SwipeRefreshLayout errorRefreshLayout;

    private View progressLayout;
    private ProgressBar progressBar;

    private FrameLayout emptyStateContainer;
    private FrameLayout errorStateContainer;

    private RecyclerView recyclerView;

    private View emptyView;
    private View errorView;

    private Controllable emptyStateView;
    private Controllable errorStateView;

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

        setupStates(attributes);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        contentRefreshLayout.setEnabled(false);
    }

    private void setupStates(TypedArray attributes)
    {
        int emptyStateLayoutId = attributes.getResourceId(R.styleable.StateListView_empty_state_view, R.layout.default_empty_state_view);

        if (emptyStateLayoutId != R.layout.default_empty_state_view)
        {
            emptyView = inflate(getContext(), emptyStateLayoutId, null);
            emptyStateContainer.addView(emptyView);

            if (emptyView instanceof Controllable)
            {
                emptyStateView = (Controllable)emptyView;
            }
        }
        else
        {
            emptyStateView = new StateView(getContext());
            emptyStateContainer.addView((View)emptyStateView);
        }

//        int errorStateLayoutId = attributes.getResourceId(R.styleable.StateListView_error_state_view, R.layout.default_error_state_view);
//
//        errorView = inflate(getContext(), errorStateLayoutId, null);
//        errorStateContainer.addView(errorView);
//
//        if (errorStateLayoutId != R.layout.default_error_state_view)
//        {
//            isCustomErrroState = true;
//        }
    }

    public void setEmptyStateMessage(String message)
    {
        if (emptyStateView != null)
        {
            emptyStateView.setMessage(message);
        }
        else
        {
            Log.e(TAG, "Can not set message, empty state view doesn't implement Controllable");
        }
    }

    public void setEmptyStateImage(int drawableRes)
    {
        if (emptyStateView != null)
        {
            emptyStateView.setImage(drawableRes);
        }
        else
        {
            Log.e(TAG, "Can not set image, empty state view doesn't implement Controllable");
        }
    }

    public void setEmptyStateOnClick(OnClickListener onClickListener)
    {
        if (emptyStateView != null)
        {
            emptyStateView.setClickListener(onClickListener);
        }
        else
        {
            Log.e(TAG, "Can not set a click listener, empty state view doesn't implement Controllable");
        }
    }

    public void setEmptyStateView(View view)
    {
        if (emptyStateContainer.getChildCount() != 0)
        {
            emptyStateContainer.removeAllViews();
        }

        emptyStateContainer.addView(view);

        if (view instanceof Controllable)
        {
            emptyStateView = (Controllable)view;
        }
    }

    @Override
    public void onShowLoading()
    {
        progressLayout.setVisibility(VISIBLE);
        contentRefreshLayout.setVisibility(GONE);
        emptyRefreshLayout.setVisibility(GONE);
        errorRefreshLayout.setVisibility(GONE);
    }

    @Override
    public void onShowContent()
    {
        progressLayout.setVisibility(GONE);
        contentRefreshLayout.setVisibility(VISIBLE);
        emptyRefreshLayout.setVisibility(GONE);
        errorRefreshLayout.setVisibility(GONE);
    }

    @Override
    public void onShowEmpty()
    {
        progressLayout.setVisibility(GONE);
        contentRefreshLayout.setVisibility(GONE);
        emptyRefreshLayout.setVisibility(VISIBLE);
        errorRefreshLayout.setVisibility(GONE);
    }

    @Override
    public void onShowError()
    {
        progressLayout.setVisibility(GONE);
        contentRefreshLayout.setVisibility(GONE);
        emptyRefreshLayout.setVisibility(GONE);
        errorRefreshLayout.setVisibility(VISIBLE);
    }

    public void setAdapter(RecyclerView.Adapter adapter)
    {
        recyclerView.setAdapter(adapter);
    }

    public RecyclerView getRecyclerView()
    {
        return recyclerView;
    }

    public View getEmptyStateView()
    {
        if (emptyStateView != null)
        {
            return (View)emptyStateView;
        }
        return emptyView;
    }

    public View getErrorStateView()
    {
        if (errorStateView != null)
        {
            return (View)errorStateView;
        }
        return errorView;
    }
}
