package com.makao.bbs_crawler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class ArticleCrawler_Discuz
  extends ArticleCrawler
{
  private DateFormat ecustFormat = new SimpleDateFormat("yyyy-M-d HH:mm:ss", Locale.ENGLISH);
  
  public ArticleCrawler_Discuz(Queue<ArticleInfo> queue, Configure.SleepTime sleeptime) {
    super(queue, sleeptime);
  }
  
  protected void modifyArticleDate(ArticleInfo articleInfo, Document document)
  {
    Elements divElements = document.getElementsByClass("authi");
    for (Element divElement : divElements) {
      Elements emElements = divElement.getElementsByTag("em");
      if ((emElements != null) && (emElements.size() > 0)) {
        String time = emElements.get(0).child(0).attr("title");
        try {
          Date date = this.ecustFormat.parse(time);
          articleInfo.setDate(date);
        } catch (ParseException e) {
          this.logger.error(getClass() + " article parse date exception", e);
        }
      }
    }
  }
  
  protected String getJobContent(ArticleInfo article)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String content = null;
    String html = this.crawler.getHtml(article.getUrl());
    Document document = Jsoup.parse(html);
    Elements tdElements = document.getElementsByClass("t_f");
    if ((tdElements != null) && (tdElements.size() > 0)) {
      content = tdElements.get(0).html();
      this.logger.debug(content);
      
      modifyArticleDate(article, document);
    }
    
    return content;
  }
}
