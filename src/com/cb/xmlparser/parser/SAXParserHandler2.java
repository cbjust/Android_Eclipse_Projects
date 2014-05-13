package com.cb.xmlparser.parser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cb.utils.LogUtils;
import com.cb.xmlparser.model.Channel;

/**
 * Sub class of DefaultHandler to parse res/raw/another_type_channel.xml via
 * SAX
 * 
 * @author binchen
 * @date 2014.5.13
 */

public class SAXParserHandler2 extends DefaultHandler
{

    private static final boolean DEBUG = false;

    private List<Channel> channels;

    private Channel channel;

    private StringBuilder data;

    public List<Channel> getList()
    {
        return channels;
    }

    @Override
    public void startDocument() throws SAXException
    {
        if (DEBUG)
        {
            LogUtils.verbose("startDocument");
        }
        channels = new ArrayList<Channel>();
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
            channels.add(channel);
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
