package com.makao.bbs_crawler.school.qinghua;
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
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemCrawler_QH extends ItemCrawler
{
  private DateFormat qinghuaFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
  private int pageCount;
  
  public ItemCrawler_QH(Queue<ArticleInfo> queue, String bbsUrlXml) {
    super(queue, bbsUrlXml);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.QINGHUA.getType();
  }
  
  protected List<ArticleInfo> getArticleInfos(Document doc, String articleBaseUrl, boolean isSingle)
    throws Exception
  {
    List<ArticleInfo> articles = new ArrayList();
    
    Elements trElements = doc.getElementsByTag("tr");
    for (int i = 1; i < trElements.size(); i++) {
      Element jobElement = trElements.get(i);
      if (!jobElement.hasClass("top")) {
        Elements tdElements = jobElement.getElementsByTag("td");
        String creationTime = tdElements.get(2).html().trim();
        
        if (creationTime.length() <= 9) {
          creationTime = Utils.dayFormat.format(new Date()) + " " + creationTime.substring(0, 8);
          String articleTitle = tdElements.get(1).child(0).html();
          String articleUrl = articleBaseUrl + tdElements.get(1).child(0).attr("href");
          String[] urls = articleUrl.split("/");
          String articleId = urls[(urls.length - 1)];
          Date articleDate = this.qinghuaFormat.parse(creationTime);
          articles.add(new ArticleInfo(articleUrl, articleDate, articleTitle, articleId, isSingle));
        } else {
          this.logger.debug("not today, ignore: " + tdElements.get(1).child(0).html());
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
