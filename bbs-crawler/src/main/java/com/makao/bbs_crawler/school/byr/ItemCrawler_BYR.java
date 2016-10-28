package com.makao.bbs_crawler.school.byr;

import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.ItemCrawler;
import com.makao.bbs_crawler.util.Constants;
import com.makao.bbs_crawler.util.Utils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemCrawler_BYR
  extends ItemCrawler
{
  private DateFormat byrFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
  private int pageCount;
  
  public ItemCrawler_BYR(Queue<ArticleInfo> queue, String bbsUrlXml) {
    super(queue, bbsUrlXml);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.BYR.getType();
  }
  
  protected List<ArticleInfo> getArticleInfos(Document doc, String articleBaseUrl, boolean isSingle)
    throws Exception
  {
    List<ArticleInfo> articles = new ArrayList();
    
    Elements elements = doc.getElementsByTag("li");
    for (Element liElement : elements) {
      Elements divElements = liElement.getElementsByTag("div");
      if (!divElements.get(0).child(0).hasClass("top")) {
        String timePair = divElements.get(1).html();
        String articleTime = timePair.split("&nbsp;")[0];
        if (articleTime.length() <= 9) {
          String articleTitle = divElements.get(0).child(0).text();
          String articleUrl = articleBaseUrl + divElements.get(0).child(0).attr("href");
          String[] urls = articleUrl.split("/");
          String articleId = urls[(urls.length - 1)];
          articleTime = Utils.dayFormat.format(new Date()) + " " + articleTime;
          Date articleDate = this.byrFormat.parse(articleTime);
          articles.add(new ArticleInfo(articleUrl, articleDate, articleTitle, articleId, isSingle));
        }
      }
    }
    
    return articles;
  }
  
  protected void extrctItemInit()
  {
    this.pageCount = 2;
  }
  
  protected String getNextURL(Document doc, String itemBaseUrl)
  {
    return itemBaseUrl + "?p=" + this.pageCount++;
  }
}
