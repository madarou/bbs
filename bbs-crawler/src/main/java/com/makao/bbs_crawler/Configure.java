package com.makao.bbs_crawler;

import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

public class Configure
{
  private Logger logger = Logger.getLogger(Configure.class);
  private int threadcount;
  private String useproxy;
  private String proxy;
  private int port;
  private String charset;
  private int timeschedule;
  private String fudan_id;
  private String fudan_pw;
  private String pku_id;
  private String pku_pw;
  private String ecust_id;
  private String ecust_pw;
  private SleepTime sleeptime;
  public static final Configure configure = new Configure();
  
  private Configure() { InputStream inputstream = null;
    java.util.Properties p = null;
    try
    {
      inputstream = ClassLoader.getSystemClassLoader().getResourceAsStream("configure.properties");
      p = new java.util.Properties();
      p.load(inputstream);
      
      this.charset = objtostr(p.get("charset"));
      
      Object tmp = p.get("threadcount");
      this.threadcount = (tmp == null ? 1 : Integer.parseInt(tmp.toString()));
      
      this.useproxy = objtostr(p.get("useproxy"));
      this.proxy = objtostr(p.get("proxylist"));
      
      tmp = p.get("port");
      this.port = (tmp == null ? 8080 : Integer.parseInt(tmp.toString()));
      
      tmp = p.get("timeschedule");
      this.timeschedule = (tmp == null ? 8080 : Integer.parseInt(tmp.toString()));
      
      tmp = p.get("crawlerpages");
      this.fudan_id = objtostr(p.get("fudan_id"));
      this.fudan_pw = objtostr(p.get("fudan_pw"));
      this.pku_id = objtostr(p.get("pku_id"));
      this.pku_pw = objtostr(p.get("pku_pw"));
      this.ecust_id = objtostr(p.get("ecust_id"));
      this.ecust_pw = objtostr(p.get("ecust_pw"));
      this.sleeptime = new SleepTime(objtostr(p.get("timesleep")));
      this.logger.info(this.useproxy);
      this.logger.info(this.proxy);
      this.logger.info(Integer.valueOf(this.port));
      this.logger.info(this.charset);
      this.logger.info(Integer.valueOf(this.threadcount));
      this.logger.info(Integer.valueOf(this.timeschedule));
      this.logger.info(this.fudan_id);
      this.logger.info(this.fudan_pw);
      this.logger.info(this.pku_id);
      this.logger.info(this.pku_pw);
      this.logger.info(this.ecust_id);
      this.logger.info(this.ecust_pw);
      this.logger.info("configure load success.....");
    }
    catch (IOException e) {
      e.printStackTrace();
      try
      {
        if (inputstream != null) {
          inputstream.close();
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (inputstream != null) {
          inputstream.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  public Logger getLogger()
  {
    return this.logger;
  }
  
  public int getThreadcount()
  {
    return this.threadcount;
  }
  
  public String getUseproxy()
  {
    return this.useproxy;
  }
  
  public String getProxy()
  {
    return this.proxy;
  }
  
  public int getPort()
  {
    return this.port;
  }
  
  public String getCharset()
  {
    return this.charset;
  }
  
  private String objtostr(Object obj) { return obj == null ? "" : obj.toString(); }
  
  public int getTimeschedule()
  {
    return this.timeschedule;
  }
  
  public String getFudan_id() {
    return this.fudan_id;
  }
  
  public String getFudan_pw() {
    return this.fudan_pw;
  }
  
  public String getECUST_id() {
    return this.ecust_id;
  }
  
  public String getECUST_pw() {
    return this.ecust_pw;
  }
  
  public SleepTime getSleeptime() {
    return this.sleeptime;
  }
  
  public String getPku_id() {
    return this.pku_id;
  }
  
  public String getPku_pw() {
    return this.pku_pw;
  }
  
  public SleepTime newSleepTime(String time) { return new SleepTime(time); }
  
  public static void main(String[] args) {}
  
  public class SleepTime { private int min;
    
    public SleepTime(String timeschedule) { String[] tmp = timeschedule.split(",");
      if (tmp.length > 1) {
        int a = Integer.parseInt(tmp[0]);
        int b = Integer.parseInt(tmp[1]);
        this.min = (a < b ? a : b);
        this.max = (a > b ? a : b);
      } }
    
    private int max;
    public int getMin() { return this.min; }
    
    public int getMax() {
      return this.max;
    }
  }
}
