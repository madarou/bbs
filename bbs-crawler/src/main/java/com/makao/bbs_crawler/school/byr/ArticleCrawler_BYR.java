package com.makao.bbs_crawler.school.byr;

import com.makao.bbs_crawler.ArticleCrawler;
import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.Configure;
import com.makao.bbs_crawler.Crawler;
import com.makao.bbs_crawler.util.Constants;
import java.io.IOException;
import java.util.Queue;
import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArticleCrawler_BYR extends ArticleCrawler
{
  public ArticleCrawler_BYR(Queue<ArticleInfo> queue, Configure.SleepTime sleeptime)
  {
    super(queue, sleeptime);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.BYR.getType();
  }
  
  protected String getJobContent(ArticleInfo article)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String content = null;
    String html = this.crawler.getHtml(article.getUrl());
    Document doc = Jsoup.parse(html);
    Elements elements = doc.getElementsByClass("sp");
    for (Element element : elements)
    {
      if (element.className().equals("sp")) {
        content = element.html();
        break;
      }
    }
    return content;
  }
}
