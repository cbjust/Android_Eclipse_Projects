package com.cb.structure.http;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * use Gson to resolve json code
 * 
 * @author binchen
 * @version [版本号, 2014-6-4]
 */

public abstract class HttpGsonFactoryBase<T> extends HttpFactoryBase<T>
{
    /**
     * use Gson which is a jar file as named gson-2.2.4.jar and should been
     * added.
     * 
     * @param responseContent
     * @return
     * @throws IOException
     */
    @Override
    protected T AnalysisContent(String responseContent)
    {
        //if response has not contained "[" and "]"
        if ((responseContent.charAt(0) != '[') && (responseContent.charAt(responseContent.length()-1) != ']'))
        {
            responseContent = "["+responseContent+"]";
        }
        
        java.lang.reflect.Type listType = new TypeToken<ArrayList<T>>(){}.getType();
        Gson gson = new Gson();
        T object = gson.fromJson(responseContent, listType);
        return object;
    }
}
