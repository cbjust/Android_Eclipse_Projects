package com.cb.test.xml.parser.factory;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.cb.structure.http.xml.HttpXmlFactoryBase;
import com.cb.test.xml.parser.model.Channel;

/**
 * build tomcat server and test
 * 
 * @author binchen
 * @date 2014-6-9
 */
public class ChannelsFactory extends HttpXmlFactoryBase<ArrayList<Channel>>
{

    private static final String DEFAULT_URL = "http://192.168.27.53:8080/hello/another_type_channel.xml";

    Channel mChannel;

    @Override
    protected void xmlStartElement(String nodeName, ArrayList<Channel> content, Attributes attributes)
            throws SAXException
    {

        if (nodeName.equals("item"))
        {
            mChannel = new Channel();
        }
    }

    @Override
    protected void xmlEndElement(String nodeName, String value, ArrayList<Channel> content) throws SAXException
    {
        if (nodeName.equals("item"))
        {
            content.add(mChannel);
        }
        else if (nodeName.equals("id"))
        {
            mChannel.setId(value.toString().trim());
        }
        else if (nodeName.equals("url"))
        {
            mChannel.setUrl(value.toString().trim());
        }
        else if (nodeName.equals("content"))
        {
            mChannel.setContent(value.toString().trim());
        }
    }

    @Override
    protected ArrayList<Channel> createContent()
    {
        return new ArrayList<Channel>();
    }

    /**
     * 服务器地址
     * 
     * @param args
     * @return
     */
    @Override
    protected String CreateUri(Object... args)
    {
        return DEFAULT_URL;
    }

}
