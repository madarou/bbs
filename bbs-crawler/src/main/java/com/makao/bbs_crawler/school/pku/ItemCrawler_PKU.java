package com.makao.bbs_crawler.school.pku;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemCrawler_PKU
  extends ItemCrawler
{
  private DateFormat pkuFormat = new SimpleDateFormat("yyyy MMM d H:mm", Locale.ENGLISH);
  private Pattern pattern = Pattern.compile("[0-9]+");
  private final int delta = 20;
  private int times;
  private int day=1;
  
  public ItemCrawler_PKU(Queue<ArticleInfo> queue, String bbsUrlXml) {
    super(queue, bbsUrlXml);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.PKU.getType();
  }
  
  protected List<ArticleInfo> getArticleInfos(Document doc, String articleBaseUrl, boolean isSingle)
    throws Exception
  {
    List<ArticleInfo> articles = new ArrayList();
    
    Elements tables = doc.getElementsByTag("table");
    for (Element table : tables) {
      Elements jobList = table.getElementsByTag("tr");
      int jobListSize = jobList.size();
      if (jobListSize >= 15)
      {
        for (int i = jobListSize - 1; i >= 1; i--) {
          Elements tdElements = jobList.get(i).getElementsByTag("td");
          if ((tdElements != null) && (tdElements.size() == 6)) {
            String articleId = tdElements.get(0).child(0).html();
            Matcher matcher = this.pattern.matcher(articleId);
            if (matcher.find()) {
              String articleTime = this.yearFormat.format(new Date()) + " " + tdElements.get(2).child(0).html();
              String articleTitle = tdElements.get(3).child(0).html().substring(2);
              String articleUrl = "http://www.bdwm.net/bbs/" + tdElements.get(3).child(0).attr("href");
              Date articleDate = this.pkuFormat.parse(articleTime);
              System.out.println(articleId+" "+articleTime+" "+articleTitle);
    		  if(!Utils.withinTimeRange(new Date(), articleDate, this.day))
    			  continue;
              articles.add(new ArticleInfo(articleUrl, articleDate, articleTitle, articleId, isSingle));
            }
          }
        }
      }
    }
    
    return articles;
  }
  
  protected void extrctItemInit()
  {
    this.times = 0;
  }
  
  protected String getNextURL(Document doc, String itemBaseUrl)
  {
    return itemBaseUrl + "&skip=" + 20 * ++this.times;
  }
}
