package com.cb.test.json.parser.ui;

import java.util.ArrayList;

import com.cb.R;
import com.cb.structure.http.HttpEventHandler;
import com.cb.test.json.parser.factory.GsonFactory;
import com.cb.test.json.parser.factory.JsonFactory;
import com.cb.test.json.parser.model.Person;
import com.cb.utils.LogUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class JsonParserActivity extends Activity
{
    private Button mGsonBtn, mJsonBtn;

    private TextView mContentView;

    private StringBuilder mData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_parser);

        initViews();
    }

    private void initViews()
    {
        mGsonBtn = (Button) findViewById(R.id.gson_btn);
        mJsonBtn = (Button) findViewById(R.id.json_btn);

        mGsonBtn.setOnClickListener(listener);
        mJsonBtn.setOnClickListener(listener);

        mContentView = (TextView) findViewById(R.id.content);

        mData = new StringBuilder();
    }

    private OnClickListener listener = new OnClickListener()
    {

        @Override
        public void onClick(View view)
        {
            mContentView.setText("");

            switch (view.getId())
            {
                case R.id.gson_btn:
                    callGson();
                    break;
                case R.id.json_btn:
                    callJson();
                    break;
            }
        }
    };

    public void callGson()
    {
        GsonFactory factory = new GsonFactory();
        factory.DownloadDatas();
        factory.setHttpEventHandler(new HttpEventHandler<ArrayList<Person>>()
        {

            @Override
            public void HttpSucessHandler(ArrayList<Person> result)
            {
                for (Person person : result)
                {
                    mData.append("Gson_" + person.getName() + ", " + person.getAge() + ", " + person.getAddress()
                            + "\n");
                }
                mContentView.setText(mData);
                mData.delete(0, mData.length());
            }

            @Override
            public void HttpFailHandler()
            {
                LogUtils.error("http failed");
            }
        });

        // another sample
        // ArrayList<Person> list = GsonFactory.createJsonList();
        //
        // for (Person person : list)
        // {
        // mData.append("Gson_" + person.getName() + ", " + person.getAge() +
        // ", " + person.getAddress() + "\n");
        // }
        // mContentView.setText(mData);
        // mData.delete(0, mData.length());
    }

    public void callJson()
    {
        JsonFactory factory = new JsonFactory();
        factory.DownloadDatas();
        factory.setHttpEventHandler(new HttpEventHandler<ArrayList<Person>>()
        {

            @Override
            public void HttpSucessHandler(ArrayList<Person> result)
            {
                for (Person person : result)
                {
                    mData.append("Json_" + person.getName() + ", " + person.getAge() + ", " + person.getAddress()
                            + "\n");
                }
                mContentView.setText(mData);
                mData.delete(0, mData.length());
            }

            @Override
            public void HttpFailHandler()
            {
                LogUtils.error("http failed");
            }
        });
    }
}
