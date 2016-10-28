package com.makao.bbs_crawler;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

public class BlackList
{
  private static Map<String, java.util.List<java.util.regex.Pattern>> blackListMap = new HashMap();
  
  static {
    loadBlackList();
  }
  
  private static void loadBlackList() {
    InputStream inputstream = null;
    try {
      inputstream = ClassLoader.getSystemClassLoader().getResourceAsStream("blacklist.xml");
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(inputstream);
      org.jdom.Element root = doc.getRootElement();
      java.util.List lists = root.getChildren();
      for (Iterator localIterator = lists.iterator(); localIterator.hasNext();) { Object tmp = localIterator.next();
        org.jdom.Element list = (org.jdom.Element)tmp;
        String name = list.getAttribute("name").getValue();
        buildRules(list.getChild("rules"), name);
      }
    } catch (Exception e) {
      e.printStackTrace();
      try
      {
        if (inputstream != null)
          inputstream.close();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (inputstream != null)
          inputstream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  private static void buildRules(org.jdom.Element rulesElem, String schoolName) {
    java.util.List rulesList = rulesElem.getChildren();
    
    java.util.List<java.util.regex.Pattern> patterns = new ArrayList();
    for (Iterator localIterator = rulesList.iterator(); localIterator.hasNext();) { Object tmp = localIterator.next();
      org.jdom.Element rule = (org.jdom.Element)tmp;
      String ruleText = rule.getText();
      java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(ruleText);
      patterns.add(pattern);
    }
    blackListMap.put(schoolName, patterns);
  }
  
  public static boolean filter(String schoolName, String title)
  {
    Logger logger = Logger.getLogger(BlackList.class);
    java.util.List<java.util.regex.Pattern> patterns = (java.util.List)blackListMap.get(schoolName);
    for (java.util.regex.Pattern pattern : patterns) {
      Matcher matcher = pattern.matcher(title);
      if (matcher.find()) {
        logger.debug("In black list: " + schoolName + "-" + title);
        return true;
      }
    }
    
    patterns = (java.util.List)blackListMap.get("ALL");
    for (java.util.regex.Pattern pattern : patterns) {
      Matcher matcher = pattern.matcher(title);
      if (matcher.find()) {
        logger.debug("In black list: ALL-" + title);
        return true;
      }
    }
    return false;
  }
  
  public static void main(String[] args) {
    System.out.println(blackListMap);
    System.out.println(filter("QINGHUA", "2014年10月自考27391工程数学（线性代数、复变函数）考试真题答"));
    System.out.println(filter("QINGHUA", "2014年湖南成人高考考试真题答案Q.《12870841》"));
    System.out.println(filter("QINGHUA", "[原创]２014年河北造价员考试|荅案《Q4615稳33046》过"));
    System.out.println(filter("PKU", "封禁"));
    System.out.println(filter("QINGHUA", "格成绩如何修改,证券从业资格成绩怎么修改Q[675618870]"));
  }
}
