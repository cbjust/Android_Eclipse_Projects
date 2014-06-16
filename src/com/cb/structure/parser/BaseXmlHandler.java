package com.cb.structure.parser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpStatus;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.cb.utils.LogUtils;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

public abstract class BaseXmlHandler<Params, Result> extends DefaultHandler
{
    public static final String HEADER_GZIP_VALUE = "gzip";

    public static final int DEFAULT_TIMEOUT = 30 * 1000;

    /** The param. */
    protected Params param;

    /** The result. */
    protected Result result;

    /** The base url. */
    protected String baseUrl;

    public static Context context;

    public static String releaseChannel;

    /** http 消息头 */
    protected Bundle headers;

    /**
     * Instantiates a new base xml hanlder.
     * 
     * @param param the param
     */
    public BaseXmlHandler(Params param)
    {
        this.param = param;
    }

    /**
     * Gets the param.
     * 
     * @return the param
     */
    public Params getParam()
    {
        return param;
    }

    /**
     * Sets the param.
     * 
     * @param param the new param
     */
    public void setParam(Params param)
    {
        this.param = param;
    }

    /**
     * Gets the result.
     * 
     * @return the result
     */
    public Result getResult()
    {
        return result;
    }

    /**
     * generate url
     * 
     * @return the string
     */
    public String generateUrl()
    {
        return baseUrl;
    }

    /**
     * request data using post method and analyze xml
     * 
     * @return
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public Result parseServerXmlWithPostRequest()
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
        InputStream is = null;
        HttpURLConnection con = null;
        URL _url = null;
        String urlString = generateUrl();
        String[] urls = urlString.split("\\?");
        String url = urls[0];
        String params = urls[1];
        byte[] postData;
        try
        {
            postData = params.getBytes("UTF-8");

            sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.setContentHandler(this);

            if (Build.VERSION.SDK_INT < 8)
            {
                System.setProperty("http.keepAlive", "false");
            }

            _url = new URL(url);
            con = (HttpURLConnection) _url.openConnection();
            con.setConnectTimeout(30 * 1000);
            con.setReadTimeout(60 * 1000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            // http消息头
            if (headers != null)
            {
                for (String key : headers.keySet())
                {
                    con.addRequestProperty(key, headers.getString(key));
                }
            }
            // start connect
            con.connect();
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.write(postData);
            out.flush();
            out.close();

            // read response
            int code = con.getResponseCode();
            if (code == 200)
            {
                is = con.getInputStream();
                InputSource inpuitSource = new InputSource(is);
                xr.parse(inpuitSource);
            }
            else
            {
                LogUtils.debug("conn.getResponseCode():" + code);
            }

        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (is != null)
                {
                    is.close();
                    is = null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                if (con != null)
                {
                    con.disconnect();
                    con = null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Parser server xml.
     * 
     * @param handler the handler
     * @return the result
     * @throws javax.xml.parsers.ParserConfigurationException the parser
     *             configuration exception
     * @throws org.xml.sax.SAXException the sAX exception
     * @throws java.io.IOException Signals that an I/O exception has occurred.
     */
    public Result parseServerXml()
    {

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;

        InputStream is = null;
        HttpURLConnection conn = null;
        String urlString = generateUrl();

        URL url = null;

        Result result = null;

        try
        {
            // xml SAX parser initialization
            sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.setContentHandler(this);

            // HttpURLConnection getResponseCode always return -1 at the
            // moment of second request
            // HTTP connection reuse which was buggy pre-froyo
            if (Build.VERSION.SDK_INT < 8)
            {
                System.setProperty("http.keepAlive", "false");
            }

            url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(DEFAULT_TIMEOUT);
            conn.setReadTimeout(DEFAULT_TIMEOUT);

            // http header
            if (headers != null)
            {
                for (String key : headers.keySet())
                {
                    conn.addRequestProperty(key, headers.getString(key));
                }
            }

            conn.connect();

            // http response code
            int code = conn.getResponseCode();

            if (code == HttpStatus.SC_OK)
            {
                is = conn.getInputStream();

                String encoding = conn.getContentEncoding();
                if (!TextUtils.isEmpty(encoding))
                {
                    LogUtils.info("encoding:" + encoding);

                    if (encoding.toLowerCase().contains(HEADER_GZIP_VALUE))
                    {
                        // gzip compression
                        is = new GZIPInputStream(is);
                    }
                }

                InputSource inpuitSource = new InputSource(is);
                xr.parse(inpuitSource);

                result = getResult();
            }
            else
            {
                LogUtils.debug("conn.getResponseCode():" + code);
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
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (is != null)
                {
                    is.close();
                    is = null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                if (conn != null)
                {
                    conn.disconnect();
                    conn = null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String inputStream2String(InputStream is) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1)
        {
            baos.write(i);
        }
        return baos.toString().trim();
    }

    /**
     * parse local xml files via sax
     * 
     * @param stream
     * @return
     */
    public Result parseLocalXml(InputStream stream)
    {
        // 1.Instantiate SAXParserFactory object
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try
        {
            // 2.instantiate a SAXParser object; create XMLReader object and
            // parse
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();

            // 3.set handler as event handler
            reader.setContentHandler(this);

            // 4.read file stream
            // InputStream stream =
            // context.getResources().openRawResource(R.raw.channels);
            InputSource source = new InputSource(stream);

            // 5.parse file
            reader.parse(source);
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

        return result;
    }
}
