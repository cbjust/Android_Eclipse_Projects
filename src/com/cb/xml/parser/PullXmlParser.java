package com.cb.xml.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.cb.R;

/**
 * Parse res/xml/channels.xml via Pull methods
 * 
 * @author binchen
 * @date 2014.5.13
 */

public class PullXmlParser
{
    public static List<Map<String, String>> getList(Context context)
    {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        XmlResourceParser parser = context.getResources().getXml(R.xml.channels);

        try
        {
            while (parser.getEventType() != XmlResourceParser.END_DOCUMENT)
            {
                if (parser.getEventType() == XmlResourceParser.START_TAG)
                {
                    String tagName = parser.getName(); // get name of tag
                    if (tagName.equals("item"))
                    {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("id", parser.getAttributeValue(null, "id"));
                        map.put("url", parser.getAttributeValue(null, "url"));
                        map.put("content", parser.nextText());

                        list.add(map);
                    }
                }

                parser.next();
            }
        }
        catch (XmlPullParserException e)
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
