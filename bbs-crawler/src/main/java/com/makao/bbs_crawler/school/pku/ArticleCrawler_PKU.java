package com.makao.bbs_crawler.school.pku;

import com.makao.bbs_crawler.ArticleCrawler;
import com.makao.bbs_crawler.ArticleInfo;
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

public class ArticleCrawler_PKU extends ArticleCrawler
{
  public ArticleCrawler_PKU(Queue<ArticleInfo> queue, Configure.SleepTime sleeptime)
  {
    super(queue, sleeptime);
  }
  
  protected String getJobContent(ArticleInfo article)
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
  
  private String extractLines(String content) {
    String[] lines = content.split("\n");
    StringBuilder re = new StringBuilder("");
    this.logger.info(Integer.valueOf(lines.length));
    for (int i = 3; i < lines.length - 1; i++) {
      re.append(lines[i]);
      re.append("\n");
    }
    return re.toString();
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.PKU.toString();
  }
}
