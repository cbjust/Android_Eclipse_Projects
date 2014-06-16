package com.cb.test.json;

import java.util.ArrayList;

import com.cb.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonTest
{
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
