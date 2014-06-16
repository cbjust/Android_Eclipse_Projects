package com.cb.test.json.parser.factory;

import java.io.IOException;
import java.util.ArrayList;

import com.cb.structure.http.HttpFactoryBase;
import com.cb.test.json.parser.model.Person;
import com.cb.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonFactory extends HttpFactoryBase<ArrayList<Person>>
{
    private static final String DEFAULT_URL = "http://192.168.27.53:8080/hello/person.txt";

    @Override
    protected String CreateUri(Object... args)
    {
        return DEFAULT_URL;
    }

    /**
     * you'd better inherit this func in concrete subclass so that you can avoid the exception below:
     * java.lang.ClassCastException: com.google.gson.internal.LinkedTreeMap cannot be cast to com.cb.test.XX
     */
    @Override
    protected ArrayList<Person> AnalysisContent(String responseContent) throws IOException
    {
        if ((responseContent.charAt(0) != '[') && (responseContent.charAt(responseContent.length() - 1) != ']'))
        {
            responseContent = "[" + responseContent + "]";
        }

        java.lang.reflect.Type listType = new TypeToken<ArrayList<Person>>()
        {
        }.getType();
        Gson gson = new Gson();
        ArrayList<Person> list = gson.fromJson(responseContent, listType);

        return list;
    }

    // another sample without using funcs of HttpFactoryBase class
    public static ArrayList<Person> createJsonList()
    {

        ArrayList<Person> list = new ArrayList<Person>();

        Person person1 = new Person();
        person1.setName("Jack");
        person1.setAge(23);
        person1.setAddress("北京");

        Person person2 = new Person();
        person2.setName("Tom");
        person2.setAge(24);
        person2.setAddress("上海");

        Person person3 = new Person();
        person3.setName("Justin");
        person3.setAge(25);
        person3.setAddress("浙江");

        list.add(person1);
        list.add(person2);
        list.add(person3);

        Gson gson = new Gson();
        String str = gson.toJson(list);
        LogUtils.verbose(str);

        java.lang.reflect.Type listType = new TypeToken<ArrayList<Person>>()
        {
        }.getType();

        list = gson.fromJson(str, listType);

        // for (Person person : list)
        // {
        // LogUtils.verbose(person.getName() + ", " + person.getAge() + ", " +
        // person.getAddress());
        // }

        return list;
    }
}
