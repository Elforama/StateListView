package com.oneguygames.statelistview.data;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanmuller on 4/30/16.
 */
public class FakeDataSource
{
    private Handler mainThreadHandler;
    private int dataCount = 0;
    private final int DATA_CHUNK_SIZE = 20;

    public FakeDataSource()
    {
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public void getData(final OnReceiveDataListener listener)
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    synchronized (this)
                    {
                        wait(1000);
                        mainThreadHandler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                listener.onReceiveData(genData());
                            }
                        });
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private List<String> genData()
    {
        List<String> data = new ArrayList<>();

        int newTotalData = dataCount + DATA_CHUNK_SIZE;

        for(int i = dataCount; i < newTotalData; i++)
        {
            dataCount++;
            data.add(String.valueOf(dataCount));
        }

        return data;
    }

    public interface OnReceiveDataListener
    {
        void onReceiveData(List<String> data);
    }
}
