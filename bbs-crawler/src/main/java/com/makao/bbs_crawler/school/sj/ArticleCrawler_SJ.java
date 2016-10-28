package com.makao.bbs_crawler.school.sj;

import com.makao.bbs_crawler.ArticleCrawler;
import com.makao.bbs_crawler.Configure;
import com.makao.bbs_crawler.Crawler;
import com.makao.bbs_crawler.ImageInfo;
import com.makao.bbs_crawler.util.Constants;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Queue;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class ArticleCrawler_SJ extends ArticleCrawler
{
  public ArticleCrawler_SJ(Queue<com.makao.bbs_crawler.ArticleInfo> queue, Configure.SleepTime sleeptime)
  {
    super(queue, sleeptime);
  }
  
  protected com.makao.bbs_crawler.SingleInfo parseSingleArticle(com.makao.bbs_crawler.ArticleInfo article)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    org.jsoup.nodes.Element img_element = new org.jsoup.nodes.Element(Tag.valueOf("img"), "", new Attributes());
    com.makao.bbs_crawler.SingleInfo singleInfo = new com.makao.bbs_crawler.SingleInfo();
    String html = this.crawler.getHtml(article.getUrl());
    Document doc = Jsoup.parse(html);
    org.jsoup.select.Elements items = doc.select("pre");
    org.jsoup.nodes.Element content = items.get(0);
    if ((items != null) && (items.size() > 0)) {
      content = items.get(0);
      org.jsoup.select.Elements imgs = content.getElementsByTag("IMG");
      for (org.jsoup.nodes.Element img : imgs) {
        String[] imgurls = img.attr("SRC").split("/");
        String img_url = "http://com.makao.bbs_crawler.sjtu.cn/" + img.attr("SRC");
        byte[] piccontent = null;
        int trytimes = 5;
        while ((piccontent == null) && (trytimes > 0)) {
          System.out.println("Pic url: " + img_url);
          piccontent = this.crawler.getContent(img_url);
          trytimes--;
        }
        singleInfo.getImages().add(new ImageInfo(imgurls[(imgurls.length - 1)], piccontent));
        img_element.attr("SRC", String.format("rest/single/img/%s/%s/%s", new Object[] { getBBSType(), article.getId(), imgurls[(imgurls.length - 1)] }));
        img.replaceWith(img_element);
      }
      singleInfo.setContent(extractLines(content.html()));
    } else {
      this.logger.warn("fail:" + article.getUrl());
      this.logger.warn(doc);
      return null;
    }
    return singleInfo;
  }
  
  protected String getJobContent(com.makao.bbs_crawler.ArticleInfo article)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String content = null;
    String html = this.crawler.getHtml(article.getUrl());
    Document doc = Jsoup.parse(html);
    org.jsoup.select.Elements items = doc.select("pre");
    if ((items != null) && (items.size() > 0)) {
      content = extractLines(items.get(0).html());
    }
    return content;
  }
  
  private String extractLines(String content)
  {
    String[] lines = content.split("\n");
    StringBuilder re = new StringBuilder("");
    
    boolean append = false;
    for (String line : lines) {
      if (append) {
        re.append(line);
        re.append("\n");
      }
      if (line.startsWith("发信站:")) {
        append = true;
      }
      if (line.contains("原文链接")) {
        this.logger.info("Do not crawl ZZ page");
        return null;
      }
    }
    return re.toString();
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.SJ.toString();
  }
}
