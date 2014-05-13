package com.cb.xmlparser.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cb.xmlparser.model.Channel;

/**
 * Parse xml via DOM methods
 * 
 * @author binchen
 * @date 2014.5.13
 */

public class DOMXmlParser
{
    public static List<Channel> getList(InputStream stream)
    {
        List<Channel> list = new ArrayList<Channel>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            // 得到DocumentBuilder对象
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 得到代表整个xml的Document对象
            Document document = builder.parse(stream);
            // 得到 "根节点"
            Element root = document.getDocumentElement();
            // 获取根节点的所有items的节点
            NodeList items = root.getElementsByTagName("item");

            // 遍历所有节点
            for (int i = 0; i < items.getLength(); i++)
            {
                Channel channel = new Channel();
                Element element = (Element) items.item(i);
                channel.setId(element.getAttribute("id"));
                channel.setUrl(element.getAttribute("url"));
                channel.setContent(element.getFirstChild().getNodeValue());

                list.add(channel);
            }

        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return list;
    }
}
