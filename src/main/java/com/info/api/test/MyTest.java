package com.info.api.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyTest {


    static public void main(String[] args) {
        System.out.println(testString());
        dateFormat();
        mapTest();
        nullTest();
    }

    public static void nullTest(){
        String UNKNOWN = "unknown";
        String ip = "ip";
        System.out.println(UNKNOWN.equalsIgnoreCase(ip));
    }

    public static void mapTest(){
        Map<String, String> map = new HashMap<>();
        Map<String, String> map2 = new HashMap<>();
        map.put("1", "One");
        map.put("2", "Two");

        map2.get("3");

        short s = 1;
//        map.put("4", s); // Compilation error
        map.put("4", String.valueOf(s));

        System.out.println("map3 " + map.get("3"));
    }

    public static void dateFormat(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        String instrDate = formatter.format(new Date());
        System.out.println("instrDate: " + instrDate);
    }

    public static String testString(){
        String s = "";
        try {
            s = "a";
            if (true) return s;
        } catch (Exception e){
            s = "Ex";

        } finally {
            s = "c";
        }
        return null;
    }
}
