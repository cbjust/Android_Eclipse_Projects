package com.cb.test.xml.parser.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.content.Context;

import com.cb.structure.parser.BaseXmlHandler;
import com.cb.test.xml.parser.model.Channel;
import com.cb.utils.LogUtils;

/**
 * Sub class of BaseXmlHandler to parse another_type_channel.xml via SAX from
 * net
 * 
 * @author binchen
 * @date 2014.5.13
 */

public class SAXParserServerXmlHandler extends BaseXmlHandler<Void, List<Channel>>
{

    public SAXParserServerXmlHandler(Context context, Void param)
    {
        super(param);
        baseUrl = "http://192.168.27.62:8080/hello/another_type_channel.xml";
    }

    private static final boolean DEBUG = false;

    private Channel channel;

    private StringBuilder data;

    @Override
    public void startDocument() throws SAXException
    {
        if (DEBUG)
        {
            LogUtils.verbose("startDocument");
        }
        result = new ArrayList<Channel>();
    }

    @Override
    public void endDocument() throws SAXException
    {
        if (DEBUG)
        {
            LogUtils.verbose("endDocument");
        }
        super.endDocument();
        data = null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if (DEBUG)
        {
            LogUtils.verbose("startElement: localName=" + localName + ", qName=" + qName);
        }

        String name = localName;
        if (name.length() <= 0)
        {
            name = qName;
        }

        if (name.equals("item"))
        {
            channel = new Channel();
        }

        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (DEBUG)
        {
            LogUtils.verbose("endElement");
        }

        String name = localName;
        if (name.length() <= 0)
        {
            name = qName;
        }

        if (name.equals("item"))
        {
            result.add(channel);
        }
        else if (name.equals("id"))
        {
            channel.setId(data.toString().trim());
        }
        else if (name.equals("url"))
        {
            channel.setUrl(data.toString().trim());
        }
        else if (name.equals("content"))
        {
            channel.setContent(data.toString().trim());
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (DEBUG)
        {
            LogUtils.verbose("characters");
        }
        data.append(ch, start, length);
    }
}
