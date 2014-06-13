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
 * Sub class of BaseXmlHandler to parse res/raw/channels.xml via SAX
 * 
 * @author binchen
 * @date 2014.5.13
 */

public class SAXParserLocalXmlHandler extends BaseXmlHandler<Void, List<Channel>>
{
    public SAXParserLocalXmlHandler(Context context, Void param)
    {
        super(param);
    }

    private static final boolean DEBUG = false;

    final int ITEM = 0x0005;

    private Channel channel;

    private int currentState = 0;

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (DEBUG)
            LogUtils.verbose("characters");

        String theString = String.valueOf(ch, start, length);
        if (currentState != 0)
        {
            channel.setContent(theString);
            currentState = 0;
        }
    }

    @Override
    public void startDocument() throws SAXException
    {
        if (DEBUG)
            LogUtils.verbose("startDocument");

        result = new ArrayList<Channel>();
    }

    @Override
    public void endDocument() throws SAXException
    {
        if (DEBUG)
            LogUtils.verbose("endDocument");

        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if (DEBUG)
            LogUtils.verbose("startElement: localName=" + localName);

        channel = new Channel();

        if (localName.equals("item"))
        {
            for (int i = 0; i < attributes.getLength(); i++)
            {
                if (attributes.getLocalName(i).equals("id"))
                {
                    channel.setId(attributes.getValue(i));
                }
                else if (attributes.getLocalName(i).equals("url"))
                {
                    channel.setUrl(attributes.getValue(i));
                }

            }
            currentState = ITEM;
            return;
        }
        currentState = 0;
        return;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (DEBUG)
            LogUtils.verbose("endElement");

        if (localName.equals("item"))
        {
            result.add(channel);
        }
    }

}
