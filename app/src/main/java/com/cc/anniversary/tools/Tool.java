package com.cc.anniversary.tools;

public class Tool {
    //2021 6 1  ==> 20210601
    //2021 11 14
    public static String getString(int month, int day) {
        return (month >= 10 ? String.valueOf(month) : "0" + month) + (day >= 10 ? day : "0" + day);
    }
}
