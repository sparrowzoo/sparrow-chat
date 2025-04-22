package com.sparrow.chat.datetime;

import com.sparrow.constant.DateTime;
import com.sparrow.enums.DateTimeUnit;
import com.sparrow.utility.DateTimeUtility;

import java.util.Calendar;

public class DateTimeTest {
    public static void main(String[] args) {
        long date = DateTimeUtility.parse("2021-08-02 23:59:59", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        DateTimeUtility.floor(calendar, DateTimeUnit.DAY);
        long hourBefore24=calendar.getTimeInMillis();
        System.out.println(DateTimeUtility.getFormatTime(hourBefore24, DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS));


        hourBefore24=System.currentTimeMillis()-8*60*60*1000;
        System.out.println(DateTimeUtility.getFormatTime(hourBefore24, DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS));
    }
}
