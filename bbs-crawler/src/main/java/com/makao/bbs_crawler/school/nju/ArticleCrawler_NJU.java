package com.makao.bbs_crawler.school.nju;

import com.makao.bbs_crawler.ArticleCrawler;
import com.makao.bbs_crawler.Configure;
import com.makao.bbs_crawler.Crawler;
import com.makao.bbs_crawler.util.Constants;
import java.io.IOException;
import java.util.Queue;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ArticleCrawler_NJU extends ArticleCrawler
{
  public ArticleCrawler_NJU(Queue<com.makao.bbs_crawler.ArticleInfo> queue, Configure.SleepTime sleeptime)
  {
    super(queue, sleeptime);
  }
  
  protected String getJobContent(com.makao.bbs_crawler.ArticleInfo article)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String content = null;
    String html = this.crawler.getHtml(article.getUrl());
    Document document = Jsoup.parse(html);
    org.jsoup.select.Elements items = document.select("textarea");
    if ((items != null) && (items.size() > 0)) {
      content = extractLines(items.get(0).html());
      this.logger.debug(content);
    }
    return content;
  }
  
  private String extractLines(String content) {
    String[] lines = content.split("\n");
    StringBuilder sb = new StringBuilder();
    
    for (String line : lines) {
      line = line.trim();
      sb.append(line);
      sb.append("<br/>");
    }
    return sb.toString();
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.NJU.toString();
  }
}
