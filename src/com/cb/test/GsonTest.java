package com.cb.test;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonTest
{
    public void createJsonList()
    {

        List<Person> list = new ArrayList<Person>();

        Person person1 = new Person();
        person1.setName("jack1");
        person1.setAge(23);
        person1.setAddress("shanghai");

        Person person2 = new Person();
        person2.setName("jack2");
        person2.setAge(24);
        person2.setAddress("shanghai");

        Person person3 = new Person();
        person3.setName("jack3");
        person3.setAge(25);
        person3.setAddress("shanghai");

        list.add(person1);
        list.add(person2);
        list.add(person3);

        Gson gson = new Gson();
        String str = gson.toJson(list);
        System.out.println(str);

        java.lang.reflect.Type listType = new TypeToken<ArrayList<Person>>(){}.getType();

        ArrayList<Person> list2 = gson.fromJson(str, listType);

        for (Person person : list2)
        {
            System.out.println(person.getName() + ", " + person.getAge() + ", " + person.getAddress());
        }
    }
}
