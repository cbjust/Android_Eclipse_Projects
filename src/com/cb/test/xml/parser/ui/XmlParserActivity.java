package com.cb.test.xml.parser.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cb.R;
import com.cb.structure.http.HttpEventHandler;
import com.cb.test.xml.parser.factory.ChannelsFactory;
import com.cb.test.xml.parser.handler.DOMParserXmlHandler;
import com.cb.test.xml.parser.handler.PullParserXmlHandler;
import com.cb.test.xml.parser.handler.SAXParserLocalXmlHandler;
import com.cb.test.xml.parser.handler.SAXParserServerXmlHandler;
import com.cb.test.xml.parser.model.Channel;
import com.cb.utils.LogUtils;

public class XmlParserActivity extends Activity
{
    protected static final int LOCAL_XML = 0;

    protected static final int SERVER_XML = 1;

    private Button mLocalSaxBtn, mServerSaxBtn, mPullBtn, mDomBtn, mFactoryBaseBtn;

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

        mLocalSaxBtn.setOnClickListener(listener);
        mServerSaxBtn.setOnClickListener(listener);
        mPullBtn.setOnClickListener(listener);
        mDomBtn.setOnClickListener(listener);
        mFactoryBaseBtn.setOnClickListener(listener);

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
            }
        }
    };

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case LOCAL_XML:
                case SERVER_XML:
                    StringBuilder str = (StringBuilder) msg.obj;
                    mContentView.setText(str);
                    mData.delete(0, str.length());
                    break;
                default:
                    break;
            }
        }
    };

    public void parseLocalXmlViaSAX()
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    InputStream stream = XmlParserActivity.this.getResources().openRawResource(R.raw.channels);

                    SAXParserLocalXmlHandler handler = new SAXParserLocalXmlHandler(XmlParserActivity.this, null);
                    List<Channel> list = handler.parseLocalXml(stream);

                    for (int i = 0; i < list.size(); i++)
                    {
                        Channel c = list.get(i);
                        mData.append("Local_SAX_" + i + ": " + c.getId() + " " + c.getUrl() + " " + c.getContent()
                                + "\n");
                    }

                    Message msg = new Message();
                    msg.what = LOCAL_XML;
                    msg.obj = mData;
                    mHandler.sendMessage(msg);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            };
        }.start();

    }

    // start a thread to download and analyze xml
    public void parseServerXmlViaSAX()
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    SAXParserServerXmlHandler handler = new SAXParserServerXmlHandler(XmlParserActivity.this, null);
                    List<Channel> list = handler.parseServerXml();
                    for (int i = 0; i < list.size(); i++)
                    {
                        Channel c = list.get(i);
                        mData.append("Server_SAX_" + i + ": " + c.getId() + " " + c.getUrl() + " " + c.getContent()
                                + "\n");
                    }
                    
                    Message msg = new Message();
                    msg.what = SERVER_XML;
                    msg.obj = mData;
                    mHandler.sendMessage(msg);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            };
        }.start();

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
}
