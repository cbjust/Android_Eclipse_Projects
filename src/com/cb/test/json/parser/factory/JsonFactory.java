package com.cb.test.json.parser.factory;

import android.util.JsonReader;

import com.cb.test.json.parser.model.Person;

import java.io.IOException;
import java.util.ArrayList;

import com.cb.structure.http.json.HttpJsonFactoryBase;

public class JsonFactory extends HttpJsonFactoryBase<ArrayList<Person>>
{
    private static final String DEFAULT_URL = "http://192.168.27.53:8080/hello/person.txt";

    @Override
    protected ArrayList<Person> AnalysisData(JsonReader reader) throws IOException
    {
        ArrayList<Person> result = new ArrayList<Person>();

        reader.beginArray();
        while (reader.hasNext())
        {
            reader.beginObject();

            Person person = new Person();
            while (reader.hasNext())
            {
                String name = reader.nextName();
                if (name.equals("name"))
                {
                    person.setName(reader.nextString());
                }
                else if (name.equals("age"))
                {
                    person.setAge(reader.nextInt());
                }
                else if (name.equals("address"))
                {
                    person.setAddress(reader.nextString());
                }
            }
            result.add(person);
            reader.endObject();
        }
        reader.endArray();

        return result;
    }

    @Override
    protected String CreateUri(Object... args)
    {
        return DEFAULT_URL;
    }

}
