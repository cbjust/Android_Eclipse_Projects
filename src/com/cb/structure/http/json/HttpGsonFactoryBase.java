package com.cb.structure.http.json;

import java.io.IOException;

import com.cb.structure.http.HttpFactoryBase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * use Gson to resolve json code
 * 
 * ps: this class is not useful, you'd better realize a concrete subclass to inherit the AnalysisContent method.
 * 
 * @author binchen
 * @date 2014-6-4
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
        /**
         * if response has not contained "[" and "]", add it, so that we can
         * handle single object or multiple objects in same func. Otherwise,
         * it will throws exception
         */
        if ((responseContent.charAt(0) != '[') && (responseContent.charAt(responseContent.length() - 1) != ']'))
        {
            responseContent = "[" + responseContent + "]";
        }

        /**
         * Question: the statement below will throw java.lang.ClassCastException:
         * com.google.gson.internal.LinkedTreeMap cannot be cast to
         * com.cb.test.XX while using Generics method.
         * 
         * Better solution: Concrete subclass should inherit AnalysisContent func (e.g.GsonFactory).
         */
        java.lang.reflect.Type listType = new TypeToken<T>() {}.getType();
        Gson gson = new Gson();
        T object = gson.fromJson(responseContent, listType);
        return object;
    }
}
