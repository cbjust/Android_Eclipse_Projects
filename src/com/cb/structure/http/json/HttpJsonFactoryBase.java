package com.cb.structure.http.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.protocol.HTTP;

import android.util.JsonReader;

public abstract class HttpJsonFactoryBase<T> extends HttpFactoryBase<T>
{

    /**
     * use JsonReader in Android System
     * 
     * @param responseContent
     * @return
     * @throws IOException
     */
    @Override
    protected T AnalysisContent(String responseContent) throws IOException
    {

        ByteArrayInputStream stream = new ByteArrayInputStream(responseContent.getBytes());
        InputStreamReader reader = new InputStreamReader(stream, HTTP.UTF_8);
        JsonReader jsonReader = new JsonReader(reader);
        try
        {
            return AnalysisData(jsonReader);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            reader.close();
            jsonReader.close();
            stream.close();
        }
    }

    protected abstract T AnalysisData(JsonReader reader) throws IOException;
}
