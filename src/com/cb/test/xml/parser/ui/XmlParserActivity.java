package com.cb.test.xml.parser.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cb.R;
import com.cb.structure.http.HttpEventHandler;
import com.cb.test.json.GsonTest;
import com.cb.test.json.Person;
import com.cb.test.xml.parser.factory.ChannelsFactory;
import com.cb.test.xml.parser.handler.DOMParserXmlHandler;
import com.cb.test.xml.parser.handler.PullParserXmlHandler;
import com.cb.test.xml.parser.handler.SAXParserLocalXmlHandler;
import com.cb.test.xml.parser.handler.SAXParserServerXmlHandler;
import com.cb.test.xml.parser.model.Channel;
import com.cb.utils.LogUtils;

public class XmlParserActivity extends Activity
{

    private Button mLocalSaxBtn, mServerSaxBtn, mPullBtn, mDomBtn, mFactoryBaseBtn, mGsonBtn;

    private TextView mContentView;

    private StringBuilder mData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_parser);

        initViews();
    }

    private void initViews()
    {
        mLocalSaxBtn = (Button) findViewById(R.id.local_sax_btn);
        mServerSaxBtn = (Button) findViewById(R.id.server_sax_btn);
        mPullBtn = (Button) findViewById(R.id.pull_btn);
        mDomBtn = (Button) findViewById(R.id.dom_btn);
        mFactoryBaseBtn = (Button) findViewById(R.id.factory_base_btn);
        mGsonBtn = (Button) findViewById(R.id.gson_test_btn);

        mLocalSaxBtn.setOnClickListener(listener);
        mServerSaxBtn.setOnClickListener(listener);
        mPullBtn.setOnClickListener(listener);
        mDomBtn.setOnClickListener(listener);
        mFactoryBaseBtn.setOnClickListener(listener);
        mGsonBtn.setOnClickListener(listener);

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
                case R.id.local_sax_btn:
                    parseLocalXmlViaSAX();
                    break;
                case R.id.server_sax_btn:
                    parseServerXmlViaSAX();
                    break;

                case R.id.pull_btn:
                    callPull();
                    break;

                case R.id.dom_btn:
                    callDOM();
                    break;

                case R.id.factory_base_btn:
                    callFactoryBase();
                    break;

                case R.id.gson_test_btn:
                    callGson();
                    break;
            }
        }
    };

    public void parseLocalXmlViaSAX()
    {
        try
        {
            InputStream stream = this.getResources().openRawResource(R.raw.channels);

            SAXParserLocalXmlHandler handler = new SAXParserLocalXmlHandler(this, null);
            List<Channel> list = handler.parseLocalXml(stream);

            for (int i = 0; i < list.size(); i++)
            {
                Channel c = list.get(i);
                mData.append("Local_SAX_" + i + ": " + c.getId() + " " + c.getUrl() + " " + c.getContent() + "\n");
            }

            mContentView.setText(mData);
            mData.delete(0, mData.length());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void parseServerXmlViaSAX()
    {
        try
        {
            SAXParserServerXmlHandler handler = new SAXParserServerXmlHandler(this, null);
            List<Channel> list = handler.parseServerXml();
            for (int i = 0; i < list.size(); i++)
            {
                Channel c = list.get(i);
                mData.append("Server_SAX_" + i + ": " + c.getId() + " " + c.getUrl() + " " + c.getContent() + "\n");
            }

            mContentView.setText(mData);
            mData.delete(0, mData.length());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void callPull()
    {
        List<Map<String, String>> list = PullParserXmlHandler.getList(this);

        for (int i = 0; i < list.size(); i++)
        {
            String id = list.get(i).get("id");
            String url = list.get(i).get("url");
            String value = list.get(i).get("content");
            mData.append("Pull_" + i + ": " + id + "/" + url + "/" + value + "\n");
        }

        mContentView.setText(mData);
        mData.delete(0, mData.length());
    }

    public void callDOM()
    {
        List<Channel> list = new ArrayList<Channel>();
        InputStream stream = getResources().openRawResource(R.raw.channels);

        list = DOMParserXmlHandler.getList(stream);

        for (int i = 0; i < list.size(); i++)
        {
            Channel c = list.get(i);
            mData.append("DOM_" + i + ": " + c.getId() + " " + c.getUrl() + " " + c.getContent() + "\n");
        }

        mContentView.setText(mData);
        mData.delete(0, mData.length());
    }

    /**
     * use another base class (HttpFactoryBase) to analyze xml
     */
    protected void callFactoryBase()
    {
        // sub-class of HttpFactoryBase just has to call DownloadDatas func
        // and setHttpEventHandler
        ChannelsFactory factory = new ChannelsFactory();
        factory.DownloadDatas();
        factory.setHttpEventHandler(new HttpEventHandler<ArrayList<Channel>>()
        {

            @Override
            public void HttpSucessHandler(ArrayList<Channel> result)
            {
                for (int i = 0; i < result.size(); i++)
                {
                    Channel c = result.get(i);
                    mData.append("FactoryBase_" + i + ": " + c.getId() + " " + c.getUrl() + " " + c.getContent() + "\n");
                }
                mContentView.setText(mData);
                mData.delete(0, mData.length());
            }

            @Override
            public void HttpFailHandler()
            {
                LogUtils.error("failed");
            }
        });
    }

    public void callGson()
    {
        ArrayList<Person> list = GsonTest.createJsonList();

        for (Person person : list)
        {
            mData.append("Gson_" + person.getName() + ", " + person.getAge() + ", " + person.getAddress() + "\n");
        }
        mContentView.setText(mData);
        mData.delete(0, mData.length());
    }
}
