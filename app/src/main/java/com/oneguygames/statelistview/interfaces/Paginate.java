package com.oneguygames.statelistview.interfaces;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public interface Paginate
{
    void onLoadPage();
    boolean isLoading();
    boolean hasMore();
}
