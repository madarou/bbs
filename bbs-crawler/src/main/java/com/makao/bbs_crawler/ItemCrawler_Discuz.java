package com.makao.bbs_crawler;

import com.makao.bbs_crawler.util.Utils;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

public abstract class ItemCrawler_Discuz extends ItemCrawler
{
  protected int pageCount;
  
  public ItemCrawler_Discuz(Queue<ArticleInfo> queue, String bbsUrlXml)
  {
    super(queue, bbsUrlXml);
  }
  
  protected void extrctItemInit()
  {
    this.pageCount = 2;
  }
  
  protected abstract String getArticleTitle(org.jsoup.nodes.Element paramElement);
  
  protected abstract String getArticleUrl(org.jsoup.nodes.Element paramElement);
  
  protected abstract Date getArticleDate(org.jsoup.nodes.Element paramElement) throws ParseException;
  
  protected List<ArticleInfo> getArticleInfos(Document doc, String articleBaseUrl, boolean isSingle) throws Exception {
    List<ArticleInfo> articles = new ArrayList();
    
    org.jsoup.select.Elements tbodyElems = doc.getElementsByTag("tbody");
    for (org.jsoup.nodes.Element tbody : tbodyElems) {
      String tbodyId = tbody.attr("id");
      String[] tmp = tbodyId.split("_");
      if (("normalthread".equals(tmp[0])) || ("stickthread".equals(tmp[0]))) {
        String articleId = tmp[1];
        org.jsoup.select.Elements thElements = tbody.getElementsByTag("th");
        if ((thElements != null) && (thElements.size() > 0)) {
          org.jsoup.nodes.Element thElement = thElements.get(0);
          String articleUrl = articleBaseUrl + getArticleUrl(thElement);
          String articleTitle = getArticleTitle(thElement);
          if ((articleUrl == null) || (articleTitle == null)) {
            this.logger.warn("articleUrl and title crawl failed: " + thElement);
          }
          else {
            org.jsoup.select.Elements byElements = tbody.getElementsByClass("by");
            Date articleDate = getArticleDate(byElements.get(0));
            if (Utils.isToday(articleDate)) {
              articles.add(new ArticleInfo(articleUrl, articleDate, articleTitle, articleId, isSingle));
            }
            
            System.out.println(articleId + " " + articleDate + " " + articleTitle + articleUrl);
          }
        }
      }
    }
    return articles;
  }
}
