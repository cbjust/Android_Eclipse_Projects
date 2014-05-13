package com.cb.fragmenttest.ui;

import com.cb.R;
import com.cb.fragmenttest.listener.OnTitleItemClickListener;
import com.cb.utils.LogUtils;

import android.os.Bundle;
import android.app.Activity;

public class FragmentTestActivity extends Activity implements OnTitleItemClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
        LogUtils.verbose("onCreate");
    }

    @Override
    protected void onDestroy()
    {
        LogUtils.verbose("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        LogUtils.verbose("onPause");
        super.onPause();
    }

    @Override
    protected void onRestart()
    {
        LogUtils.verbose("onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume()
    {
        LogUtils.verbose("onResume");
        super.onResume();
    }

    @Override
    protected void onStart()
    {
        LogUtils.verbose("onStart");
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        LogUtils.verbose("onStop");
        super.onStop();
    }

    @Override
    public void onItemClick(int position)
    {
        LogUtils.verbose("onItemClick: position is " + position);
        ContentFragment contentFragment = (ContentFragment) getFragmentManager().findFragmentById(R.id.content_fragment);
        contentFragment.updateContent(position);
    }
}
