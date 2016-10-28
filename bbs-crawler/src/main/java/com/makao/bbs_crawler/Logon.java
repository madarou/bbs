package com.makao.bbs_crawler;
import com.makao.bbs_crawler.util.Constants;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Logon
{
  public static int fudanLogon(HttpClient httpclient)
  {
    httpclient.getParams().setParameter("http.protocol.version", 
      HttpVersion.HTTP_1_0);
    HttpPost httpPost = new HttpPost("http://bbs.fudan.edu.cn/bbs/login");
    httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
    java.util.List<NameValuePair> nvps = new ArrayList();
    nvps.add(new BasicNameValuePair("id", Configure.configure.getFudan_id()));
    nvps.add(new BasicNameValuePair("pw", Configure.configure.getFudan_pw()));
    try {
      httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
      HttpResponse reponse = httpclient.execute(httpPost);
      StatusLine sl = reponse.getStatusLine();
      StringWriter writer = new StringWriter();
      HttpEntity e = reponse.getEntity();
      IOUtils.copy(e.getContent(), writer, EntityUtils.getContentCharSet(e));
      int code = sl.getStatusCode();
      if (code == 302) {
        return Constants.SUCCESS;
      }
      
      return Constants.FAILURE;
    }
    catch (Exception e1) {
      e1.printStackTrace(); }
    return Constants.FAILURE;
  }
  
  public static String fudanCheckLogon(Crawler cralwer, String url) throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String html = cralwer.getHtml(url);
    Document doc = Jsoup.parse(html);
    Elements items = doc.getElementsByTag("title");
    
    int sleepTime = 10;
    Logger logger = Logger.getLogger(Logon.class);
    for (Element item : items) {
      String title = item.html();
      if (title.equals(Constants.NOAUTHOR)) {
        do {
          logger.debug("Not logged in! Sleeping 10 seconds to retry");
          Thread.sleep(10000L);
        } while (fudanLogon(cralwer.getHttpClient()) != Constants.SUCCESS);
      }
      else {
        return html;
      }
    }
    return html;
  }
  
  public static int pkuLogon(HttpClient httpclient) {
    httpclient.getParams().setParameter("http.protocol.version", 
      HttpVersion.HTTP_1_0);
    HttpPost httpPost = new HttpPost("https://www.bdwm.net/bbs/bbslog2.php");
    httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
    java.util.List<NameValuePair> nvps = new ArrayList();
    nvps.add(new BasicNameValuePair("userid", Configure.configure.getPku_id()));
    nvps.add(new BasicNameValuePair("passwd", Configure.configure.getPku_pw()));
    try {
      httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
      HttpResponse reponse = httpclient.execute(httpPost);
      StringWriter writer = new StringWriter();
      HttpEntity e = reponse.getEntity();
      IOUtils.copy(e.getContent(), writer, "GB2312");
      
      if (writer.toString().contains("alert")) {
        return Constants.FAILURE;
      }
      
      return Constants.SUCCESS;
    }
    catch (Exception e1) {
      e1.printStackTrace(); }
    return Constants.FAILURE;
  }
  
  public static String pkuCheckLogon(Crawler cralwer, String url) throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String html = cralwer.getHtml(url);
    Document doc = Jsoup.parse(html);
    Element table = doc.select("table").first();
    boolean fail = table.html().contains(Constants.PKU_NOAUTHOR);
    
    if (fail) {
      Thread.sleep(6000L);
      while (pkuLogon(cralwer.getHttpClient()) != Constants.SUCCESS) {
        Thread.sleep(6000L);
      }
      Thread.sleep(6000L);
      html = cralwer.getHtml(url);
      return html;
    }
    
    return html;
  }
  
  public static int ecustLogon(Crawler crawler)
  {
    String ECUST_url = "http://bbs.ecust.edu.cn/member.php?mod=logging&action=login";
    
    HttpPost httpPost = new HttpPost("http://bbs.ecust.edu.cn/member.php?mod=logging&action=login");
    httpPost.setHeader("User-Agent", 
      "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.126 Safari/535.1");
    httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
    java.util.List<NameValuePair> nvps = new ArrayList();
    nvps.add(new BasicNameValuePair("referer", "http://bbs.ecust.edu.cn/./"));
    nvps.add(new BasicNameValuePair("loginfield", "username"));
    nvps.add(new BasicNameValuePair("username", Configure.configure.getECUST_id()));
    nvps.add(new BasicNameValuePair("password", Configure.configure.getECUST_pw()));
    nvps.add(new BasicNameValuePair("questionid", "0"));
    nvps.add(new BasicNameValuePair("answer", ""));
    nvps.add(new BasicNameValuePair("loginsubmit", "true"));
    try
    {
      String formHash = null;
      String html = crawler.getHtml("http://bbs.ecust.edu.cn/member.php?mod=logging&action=login");
      Document document = Jsoup.parse(html);
      Elements elements = document.getElementsByTag("input");
      for (Element element : elements) {
        if ((element.hasAttr("name")) && (element.attr("name").equals("formhash"))) {
          formHash = element.attr("value");
          break;
        }
      }
      
      if ((formHash == null) || (formHash.isEmpty())) return Constants.FAILURE;
      nvps.add(new BasicNameValuePair("formhash", formHash));
      httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
      HttpResponse reponse = crawler.getHttpClient().execute(httpPost);
      StringWriter writer = new StringWriter();
      HttpEntity entity = reponse.getEntity();
      String charset = EntityUtils.getContentCharSet(entity);
      System.out.println(charset);
      IOUtils.copy(entity.getContent(), writer, charset);
      
      if (writer.toString().contains("欢迎您回来")) {
        return Constants.SUCCESS;
      }
      return Constants.FAILURE;
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Constants.FAILURE;
  }
  
  public static String ecustCheckLogon(Crawler crawler, String url) throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException {
    String html = crawler.getHtml(url);
    boolean fail = html.contains("尚未登录");
    
    if (fail) {
      Thread.sleep(6000L);
      while (ecustLogon(crawler) != Constants.SUCCESS) {
        Thread.sleep(6000L);
      }
      Thread.sleep(6000L);
      html = crawler.getHtml(url);
      return html;
    }
    return html;
  }
  
  public static void main(String[] args)
    throws ClientProtocolException, IOException, ClassNotFoundException, InterruptedException
  {
    String ECUST_intern_url = "http://bbs.ecust.edu.cn/forum.php?mod=forumdisplay&fid=145";
    Crawler crawler = new Crawler();
    
    String html = ecustCheckLogon(crawler, "http://bbs.ecust.edu.cn/forum.php?mod=forumdisplay&fid=145");
    System.out.println(html);
  }
}
