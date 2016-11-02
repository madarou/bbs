package com.makao.bbs_crawler.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  
  /**
 * @param type
 * @param expression
 * @return
 * 获取type(ddddd)中括号中的内容，贪婪匹配
 */
public static String getContent(String type,String expression){  
      String regStr = type+"\\((.*)\\)";  
      Pattern pattern = Pattern.compile(regStr);  
      Matcher matcher = pattern.matcher(expression);  
      while(matcher.find()){  
          return matcher.group(1);  
      }  
      return null;  
  }  

/**
* @param now
* @param date
* @return
* 时间差在昨天到明天之间
*/
public static boolean withinTimeRange(Date now, Date date)
{
  Calendar cal = Calendar.getInstance();
  cal.setTime(now);
  cal.add(5, -1); //等同于cal.add(Calendar.DAY_OF_MONTH, -1);即天数减一天
  Date yesterday = cal.getTime();
  cal.setTime(now);
  cal.add(5, 1);
  Date tomorrow = cal.getTime();
  
  return (date.compareTo(yesterday) >= 0) && (
    date.compareTo(tomorrow) <= 0);
}
  
  public static void main(String[] args)
  {
    java.util.Date date = new java.util.Date();
    System.out.println(date);
    System.out.println(toMonthFormat(date));
    System.out.println(isYesterday(new java.util.Date()));
    
    String expression = "<!--[CDATA[formRelated(CreatorUserLogin.(Person).CompanyDepartment.Location.Organization)]]-->";  
    String formRelatedType = "formRelated";  
    System.out.println(getContent(formRelatedType,expression));  
  }
}
