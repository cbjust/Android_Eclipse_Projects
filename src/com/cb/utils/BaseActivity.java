/**
 *
 */
package com.cb.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

/**
 * 基类
 */
public abstract class BaseActivity extends Activity
{
    /**
     * activityList:所有activity对象，用于退出时全部finish
     */
    private static ArrayList<WeakReference<Activity>> activityList = new ArrayList<WeakReference<Activity>>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        onActivityCreate(this);
    }

    public static void onActivityCreate(Activity activity)
    {
        activityList.add(new WeakReference<Activity>(activity));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        onActivityResume(this);

    }

    public static void onActivityResume(Activity activity)
    {
        if (listener != null)
        {
            listener.onResmue();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        onActivityPause(this);
    }

    public static void onActivityPause(Activity activity)
    {
        if (listener != null)
        {
            listener.onPause();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    /**
     * 关闭多个activity
     */
    public static void closeAll()
    {
        // 关闭所有Activity
        Activity activity;
        for (int i = 0; i < activityList.size(); i++)
        {
            if (null != activityList.get(i))
            {
                activity = activityList.get(i).get();
                if (activity != null)
                {
                    activity.finish();
                }
            }
        }
        activityList.clear();
    }

    public interface onActivityListener
    {
        void onResmue();

        void onPause();
    }

    public static void setOnActivityListener(onActivityListener listener)
    {
        BaseActivity.listener = listener;

    }

    private static onActivityListener listener;

    public static ArrayList<WeakReference<Activity>> getActivityList()
    {
        return activityList;
    }

}
