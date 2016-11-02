package com.makao.bbs_crawler.school.cd;

import com.makao.bbs_crawler.ArticleCrawler;
import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.Configure;
import com.makao.bbs_crawler.Crawler;
import com.makao.bbs_crawler.ImageInfo;
import com.makao.bbs_crawler.Logon;
import com.makao.bbs_crawler.SingleInfo;
import com.makao.bbs_crawler.util.Constants;
import com.makao.bbs_crawler.util.Utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class ArticleCrawler_CD extends ArticleCrawler
{
  public ArticleCrawler_CD(Queue<ArticleInfo> queue, Configure.SleepTime sleeptime)
  {
    super(queue, sleeptime);
  }
  
  protected SingleInfo parseSingleArticle(ArticleInfo article)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    org.jsoup.nodes.Element img = new org.jsoup.nodes.Element(Tag.valueOf("img"), "", new Attributes());
    SingleInfo singleInfo = new SingleInfo();
    String html = this.crawler.getHtml(article.getUrl());
    Document doc = Jsoup.parse(html);
    org.jsoup.select.Elements items = doc.select("pa[m=t]");
    if ((items != null) && (items.size() > 0)) {
      org.jsoup.nodes.Element content = items.get(0);
      org.jsoup.select.Elements pics = content.select("a[href]");
      for (org.jsoup.nodes.Element pic : pics) {
        String url = pic.attr("href");
        String[] pictmp = url.split("/");
        if (pictmp.length > 1) {
          String url_l = pictmp[(pictmp.length - 1)].toLowerCase();
          if ((url_l.endsWith("jpg")) || (url_l.endsWith("jpeg")) || (url_l.endsWith("bmp")) || 
            (url_l.endsWith("png")) || (url_l.endsWith("gif"))) {
            byte[] piccontent = null;
            int trytimes = 5;
            while ((piccontent == null) && (trytimes > 0)) {
              piccontent = this.crawler.getContent(url);
              System.out.println("Pic url: " + url);
              trytimes--;
            }
            singleInfo.getImages().add(new ImageInfo(url_l, piccontent));
            img.attr("src", String.format("rest/single/img/%s/%s/%s", new Object[] { getBBSType(), article.getId(), url_l }));
            pic.replaceWith(img);
          }
        }
      }
      singleInfo.setContent(content.html());
    } else {
      this.logger.warn("fail:" + article.getUrl());
      this.logger.warn(doc);
      return null;
    }
    return singleInfo;
  }
  
  /* (non-Javadoc)
 * @see com.makao.bbs_crawler.ArticleCrawler#getJobContent(com.makao.bbs_crawler.ArticleInfo)
 * 实际解析一篇帖子的内容
 */
protected String getJobContent(ArticleInfo article)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String content = null;
    String html = this.crawler.getHtml(article.getUrl());
    Document doc = Jsoup.parse(html);
    Elements scripts = doc.select("script[type]");
    if(scripts!=null && scripts.size()>2)
	  {
		  Element script =scripts.get(2);
		  if(script!=null){
			  String ct = script.html();
			  content = ct.substring(ct.indexOf("prints(")+8, ct.length()-22);
		  }
	  }
    return content;
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.CD.toString();
  }
  
  public static void main(String[] args){
	  DateFormat timeFormatCD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	  System.out.println(timeFormatCD.format(new Date((1478058546L+8*3600) * 1000)));
	  File input = new File("C:/Users/ZYR/Desktop/cd_detail.html");
	  Document doc=null;
		try {
			doc = Jsoup.parse(input, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Elements scripts = doc.select("script[type]");
		 if(scripts!=null && scripts.size()>2)
		  {
			  Element script =scripts.get(2);
			  if(script!=null){
				  String ct = script.html();
				  System.out.println(ct.substring(ct.indexOf("prints(")+7, ct.length()-23));
			  }
		  }
	   
  }
}
