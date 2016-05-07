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

    private Controllable emptyStateController;
    private Controllable errorStateController;

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

        contentRefreshLayout.setEnabled(true);
        errorRefreshLayout.setEnabled(true);
        emptyRefreshLayout.setEnabled(true);
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
                emptyStateController = (Controllable)emptyView;
            }
        }
        else
        {
            emptyStateController = new StateView(getContext());
            emptyStateContainer.addView((View) emptyStateController);
        }

        int errorStateLayoutId = attributes.getResourceId(R.styleable.StateListView_error_state_view, R.layout.default_empty_state_view);

        if (errorStateLayoutId != R.layout.default_empty_state_view)
        {
            errorView = inflate(getContext(), emptyStateLayoutId, null);
            errorStateContainer.addView(errorView);

            if (errorView instanceof Controllable)
            {
                errorStateController = (Controllable)errorView;
            }
        }
        else
        {
            errorStateController = new StateView(getContext());
            errorStateContainer.addView((View) errorStateController);
        }
    }

    public void setEmptyStateMessage(String message)
    {
        if (emptyStateController != null)
        {
            emptyStateController.setMessage(message);
        }
        else
        {
            Log.e(TAG, "Can not set message, empty state view doesn't implement Controllable");
        }
    }

    public void setEmptyStateImage(int drawableRes)
    {
        if (emptyStateController != null)
        {
            emptyStateController.setImage(drawableRes);
        }
        else
        {
            Log.e(TAG, "Can not set image, empty state view doesn't implement Controllable");
        }
    }

    public void setEmptyStateOnClick(OnClickListener onClickListener)
    {
        if (emptyStateController != null)
        {
            emptyStateController.setClickListener(onClickListener);
        }
        else
        {
            Log.e(TAG, "Can not set a click listener, empty state view doesn't implement Controllable");
        }
    }

    public void setErrorStateMessage(String message)
    {
        if (errorStateController != null)
        {
            errorStateController.setMessage(message);
        }
        else
        {
            Log.e(TAG, "Can not set message, empty state view doesn't implement Controllable");
        }
    }

    public void setErrorStateImage(int drawableRes)
    {
        if (errorStateController != null)
        {
            errorStateController.setImage(drawableRes);
        }
        else
        {
            Log.e(TAG, "Can not set image, empty state view doesn't implement Controllable");
        }
    }

    public void setErrorStateOnClick(OnClickListener onClickListener)
    {
        if (errorStateController != null)
        {
            errorStateController.setClickListener(onClickListener);
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
            emptyStateController = (Controllable)view;
        }
    }

    public void setErrorStateView(View view)
    {
        if (errorStateContainer.getChildCount() != 0)
        {
            errorStateContainer.removeAllViews();
        }

        errorStateContainer.addView(view);

        if (view instanceof Controllable)
        {
            errorStateController = (Controllable)view;
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

    public View getEmptyStateController()
    {
        if (emptyStateController != null)
        {
            return (View) emptyStateController;
        }
        return emptyView;
    }

    public View getErrorStateController()
    {
        if (errorStateController != null)
        {
            return (View) errorStateController;
        }
        return errorView;
    }
}
