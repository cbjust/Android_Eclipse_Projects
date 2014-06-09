package com.cb.xml.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;

import com.cb.R;
import com.cb.xml.model.Channel;

/**
 * Parse xml via SAX methods
 * 
 * @author binchen
 * @date 2014.5.13
 */

public class SAXXmlParser
{
    /**
     * SAX解析XML文件格式 <功能详细描述>
     * 
     * @param context
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @see [类、类#方法、类#成员]
     */
    public static List<Channel> getChannelList(Context context) throws ParserConfigurationException, SAXException,
            IOException
    {
        // 1.实例化一个SAXParserFactory对象
        SAXParserFactory factory = SAXParserFactory.newInstance();

        // 2.实例化一个SAXParser对象；创建XMLReader对象,解析器
        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();

        // 3.实例化handler，事件处理器
        SAXParserHandler handler = new SAXParserHandler();
        reader.setContentHandler(handler);

        // 4.读取文件流
        InputStream stream = context.getResources().openRawResource(R.raw.channels);
        InputSource source = new InputSource(stream);

        // 5.解析文件
        reader.parse(source);
        return handler.getList();

    }

    /**
     * SAX解析XML文件格式 another_type_channel.xml文件内容更符合应用开发中的格式
     * 
     * @param context
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @see [类、类#方法、类#成员]
     */
    public static List<Channel> getChannelList2(Context context) throws ParserConfigurationException, SAXException,
            IOException
    {
        // 1.实例化一个SAXParserFactory对象
        SAXParserFactory factory = SAXParserFactory.newInstance();

        // 2.实例化一个SAXParser对象；创建XMLReader对象,解析器
        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();

        // 3.实例化handler，事件处理器
        SAXParserHandler2 handler = new SAXParserHandler2();
        reader.setContentHandler(handler);

        // 4.读取文件流
        InputStream stream = context.getResources().openRawResource(R.raw.another_type_channel);
        InputSource source = new InputSource(stream);

        // 5.解析文件
        reader.parse(source);
        return handler.getList();

    }
}
