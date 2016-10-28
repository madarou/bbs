package com.makao.bbs_crawler;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class Crawler
{
  private Logger log = Logger.getLogger(Crawler.class);
  private HttpClient httpclient;
  
  public Crawler() { 
	  this.httpclient = new DefaultHttpClient(new ThreadSafeClientConnManager());
    if (Configure.configure.getUseproxy().equals("true")) {
      HttpHost host = new HttpHost(Configure.configure.getProxy(), Configure.configure.getPort(), "http");
      this.httpclient.getParams().setParameter("http.route.default-proxy", host);
    }
    this.httpclient.getParams().setParameter("http.connection.timeout", Integer.valueOf(60000));
    this.httpclient.getParams().setParameter("http.socket.timeout", Integer.valueOf(60000));
    this.httpclient.getParams().setParameter("http.protocol.cookie-policy", "best-match");
  }
  
  public Crawler(boolean ssl) { 
	  //this.httpclient = new DefaultHttpClient(new ThreadSafeClientConnManager());
	  this.httpclient = WebClientDevWrapper.wrapClient(new DefaultHttpClient(new ThreadSafeClientConnManager()));  
    if (Configure.configure.getUseproxy().equals("true")) {
      HttpHost host = new HttpHost(Configure.configure.getProxy(), Configure.configure.getPort(), "http");
      this.httpclient.getParams().setParameter("http.route.default-proxy", host);
    }
    this.httpclient.getParams().setParameter("http.connection.timeout", Integer.valueOf(60000));
    this.httpclient.getParams().setParameter("http.socket.timeout", Integer.valueOf(60000));
    this.httpclient.getParams().setParameter("http.protocol.cookie-policy", "best-match");
  }
  
  public String getHtml(String url) throws ClientProtocolException, IOException, ClassNotFoundException { HttpGet httpget = new HttpGet(url);
    httpget.setHeader("User-Agent", 
      "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.126 Safari/535.1");
    
    InputStream instream = null;
    HttpResponse response = this.httpclient.execute(httpget);
    HttpEntity entity = response.getEntity();
    
    String result = "";
    StringWriter writer = new StringWriter();
    if (entity != null) {
      String charset = EntityUtils.getContentCharSet(entity);
      instream = entity.getContent();
      IOUtils.copy(instream, writer, charset);
      result = writer.toString();
      
      httpget.abort();
    }
    return result;
  }
  
  public void saveToFile(String url, OutputStream output) throws ClientProtocolException, IOException
  {
    HttpGet httpget = new HttpGet(url);
    httpget.setHeader("User-Agent", 
      "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.126 Safari/535.1");
    HttpResponse response = this.httpclient.execute(httpget);
    HttpEntity entity = response.getEntity();
    IOUtils.copy(entity.getContent(), output);
    output.flush();
    output.close();
    httpget.abort();
  }
  
  public byte[] getContent(String url) throws ClientProtocolException, IOException
  {
    HttpGet httpget = new HttpGet(url);
    httpget.setHeader("User-Agent", 
      "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.126 Safari/535.1");
    HttpResponse response = this.httpclient.execute(httpget);
    HttpEntity entity = response.getEntity();
    byte[] piccontent = IOUtils.toByteArray(entity.getContent());
    httpget.abort();
    return piccontent;
  }
  
  public HttpClient getHttpClient() { return this.httpclient; }
  
  public void buildIndex(String url) throws ClientProtocolException, IOException
  {
    HttpGet httpget = new HttpGet(url);
    httpget.setHeader("User-Agent", 
      "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.126 Safari/535.1");
    this.httpclient.execute(httpget);
  }
  
  public static void main(String[] args) {
    Crawler crawler = new Crawler();
    String url = "http://bbs.fudan.edu.cn/upload/Single/1419087350-8579.jpg";
    FileOutputStream fo = null;
    try {
      byte[] picBytes = crawler.getContent(url);
      fo = new FileOutputStream("1.jpg");
      fo.write(picBytes);
    } catch (Exception e) {
      e.printStackTrace();
      
      if (fo != null) {
        try {
          fo.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }
    finally
    {
      if (fo != null) {
        try {
          fo.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
