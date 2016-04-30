package com.oneguygames.statelistview;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public interface Paginate
{
    void onLoadPage();
    boolean isLoading();
    boolean hasMore();
}
