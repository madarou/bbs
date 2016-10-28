package com.makao.bbs_crawler.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Utils
{
  public static java.text.DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
  public static java.text.DateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
  
  private static Random random = new Random();
  
  public static int getRandom(int deta) { return random.nextInt(deta); }
  
  public static String getCurrentTime() {
    java.util.Date date = new java.util.Date();
    int year = date.getYear() + 1900;
    int month = date.getMonth() + 1;
    int day = date.getDate();
    int hour = date.getHours();
    if (hour == 0) {
      return year + "-" + intToStr(month) + "-" + intToStr(day - 1);
    }
    
    return year + "-" + intToStr(month) + "-" + intToStr(day);
  }
  
  public static String intToStr(int num)
  {
    return num < 10 ? "0" + num : String.valueOf(num);
  }
  
  public static boolean isToday(java.util.Date date) {
    String dateStr = toDayFormat(date);
    if (dateStr.equals(toDayFormat(new java.util.Date()))) {
      return true;
    }
    return false;
  }
  
  public static boolean isYesterday(java.util.Date date)
  {
    Calendar cal = Calendar.getInstance();
    
    cal.add(5, -1);
    java.util.Date yesterday = cal.getTime();
    System.out.println(yesterday);
    String dateStr = toDayFormat(date);
    if (dateStr.equals(toDayFormat(yesterday))) {
      return true;
    }
    return false;
  }
  
  public static String toDayFormat(java.util.Date date)
  {
    return dayFormat.format(date);
  }
  
  public static boolean thisMonth(java.util.Date date) {
    String monthStr = toMonthFormat(date);
    if (monthStr.equals(toMonthFormat(new java.util.Date()))) {
      return true;
    }
    return false;
  }
  
  public static String toMonthFormat(java.util.Date date)
  {
    return monthFormat.format(date);
  }
  
  public static void main(String[] args)
  {
    java.util.Date date = new java.util.Date();
    System.out.println(date);
    System.out.println(toMonthFormat(date));
    System.out.println(isYesterday(new java.util.Date()));
  }
}
