package com.cb.xml.test.factory;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.cb.structure.xml.HttpXmlFactoryBase;
import com.cb.xml.model.Channel;

/**
 * tomcat搭建服务器，测试
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  binchen
 * @version  [版本号, 2014-6-9]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ChannelsFactory extends HttpXmlFactoryBase<ArrayList<Channel>>
{

    Channel channel;

    @Override
    protected void xmlStartElement(String nodeName, ArrayList<Channel> content, Attributes attributes) throws SAXException
    {

        if (nodeName.equals("item"))
        {
            channel = new Channel();
        }
    }

    @Override
    protected void xmlEndElement(String nodeName, String value, ArrayList<Channel> content) throws SAXException
    {
        if (nodeName.equals("item"))
        {
            content.add(channel);
        }
        else if (nodeName.equals("id"))
        {
            channel.setId(value.toString().trim());
        }
        else if (nodeName.equals("url"))
        {
            channel.setUrl(value.toString().trim());
        }
        else if (nodeName.equals("content"))
        {
            channel.setContent(value.toString().trim());
        }
    }

    @Override
    protected ArrayList<Channel> createContent()
    {
        return new ArrayList<Channel>();
    }

    /**
     * 服务器地址
     * @param args
     * @return
     */
    @Override
    protected String CreateUri(Object... args)
    {
        return "http://192.168.27.62:8080/hello/another_type_channel.xml";
    }

}
