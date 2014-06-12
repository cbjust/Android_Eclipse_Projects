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
import com.cb.test.xml.parser.factory.ChannelsFactory;
import com.cb.test.xml.parser.handler.DOMParserXmlHandler;
import com.cb.test.xml.parser.handler.PullParserXmlHandler;
import com.cb.test.xml.parser.handler.SAXParserLocalXmlHandler;
import com.cb.test.xml.parser.handler.SAXParserServerXmlHandler;
import com.cb.test.xml.parser.model.Channel;
import com.cb.utils.LogUtils;

public class XmlParserActivity extends Activity
{

    private Button mSaxBtn, mSax2Btn, mPullBtn, mDomBtn, mStructureTestBtn;

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
        mSaxBtn = (Button) findViewById(R.id.sax_btn);
        mSax2Btn = (Button) findViewById(R.id.sax2_btn);
        mPullBtn = (Button) findViewById(R.id.pull_btn);
        mDomBtn = (Button) findViewById(R.id.dom_btn);
        mStructureTestBtn = (Button) findViewById(R.id.structure_test_btn);

        mSaxBtn.setOnClickListener(listener);
        mSax2Btn.setOnClickListener(listener);
        mPullBtn.setOnClickListener(listener);
        mDomBtn.setOnClickListener(listener);
        mStructureTestBtn.setOnClickListener(listener);

        mContentView = (TextView) findViewById(R.id.content);

        mData = new StringBuilder();
    }

    private OnClickListener listener = new OnClickListener()
    {

        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.sax_btn:
                    parseLocalXmlViaSAX();
                    break;
                case R.id.sax2_btn:
                    parseServerXmlViaSAX();
                    break;

                case R.id.pull_btn:
                    callPull();
                    break;

                case R.id.dom_btn:
                    callDOM();
                    break;

                case R.id.structure_test_btn:
                    callStructureTest();
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
                // LogUtils.verbose("SAX_" + i + ": " + c.getId() + " " +
                // c.getUrl() + " " + c.getContent());
                mData.append("SAX_" + i + ": " + c.getId() + " " + c.getUrl() + " " + c.getContent() + "\n");
            }

            mContentView.setText(mData);
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
                mData.append("SAX2_" + i + ": " + c.getId() + " " + c.getUrl() + " " + c.getContent() + "\n");
            }

            mContentView.setText(mData);
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
            // LogUtils.verbose("Pull_" + i + ": " + id + "/" + url + "/" +
            // value);
            mData.append("Pull_" + i + ": " + id + "/" + url + "/" + value + "\n");
        }

        mContentView.setText(mData);
    }

    public void callDOM()
    {
        List<Channel> list = new ArrayList<Channel>();
        InputStream stream = getResources().openRawResource(R.raw.channels);

        list = DOMParserXmlHandler.getList(stream);

        for (int i = 0; i < list.size(); i++)
        {
            Channel c = list.get(i);
            // LogUtils.verbose("DOM_" + i + ": " + c.getId() + " " +
            // c.getUrl() + " " + c.getContent());
            mData.append("DOM_" + i + ": " + c.getId() + " " + c.getUrl() + " " + c.getContent() + "\n");
        }

        mContentView.setText(mData);
    }

    protected void callStructureTest()
    {
        ChannelsFactory factory = new ChannelsFactory();
        factory.DownloaDatas();
        factory.setHttpEventHandler(new HttpEventHandler<ArrayList<Channel>>()
        {

            @Override
            public void HttpSucessHandler(ArrayList<Channel> result)
            {
                for (int i = 0; i < result.size(); i++)
                {
                    Channel c = result.get(i);
                    mData.append("Structure_test_" + i + ": " + c.getId() + " " + c.getUrl() + " " + c.getContent()
                            + "\n");
                }
                mContentView.setText(mData);
            }

            @Override
            public void HttpFailHandler()
            {
                LogUtils.error("failed");
            }
        });
    }

}
