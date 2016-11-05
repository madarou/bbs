package com.makao.bbs_crawler.school.nju;
import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.ItemCrawler;
import com.makao.bbs_crawler.util.Constants;
import com.makao.bbs_crawler.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ItemCrawler_NJU extends ItemCrawler
{
  private Pattern pattern = Pattern.compile("\\d+");
  private Pattern startPattern = Pattern.compile("start=(\\d+)");
  private SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.ENGLISH);
  private SimpleDateFormat njuFormat = new SimpleDateFormat("yyyy MMM d H:mm", Locale.ENGLISH);
  private int day=1;
  
  public ItemCrawler_NJU(Queue<ArticleInfo> queue, String bbsUrlXml) {
    super(queue, bbsUrlXml);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.NJU.getType();
  }
  
  protected List<ArticleInfo> getArticleInfos(Document doc, String articleBaseUrl, boolean isSingle)
    throws Exception
  {
    List<ArticleInfo> articles = new ArrayList();
    
    Elements tables = doc.getElementsByTag("table");
    for (Iterator localIterator = tables.iterator(); localIterator.hasNext();)
    {
      org.jsoup.nodes.Element table = (org.jsoup.nodes.Element)localIterator.next();
      Elements trItems = table.getElementsByTag("tr");
      
      for(int i = trItems.size()-1;i>0;i--){
    	  Elements tdItems = trItems.get(i).getElementsByTag("td");
          if ((tdItems != null) && (tdItems.size() == 6)) {
            String articelId = tdItems.get(0).html();
            
            Matcher matcher = this.pattern.matcher(articelId);
            if (matcher.find()) {
              String articleTime = this.year.format(new Date()) + " " + tdItems.get(3).html();
              String articleTitle = tdItems.get(4).child(0).html().substring(2);
              String articleUrl = articleBaseUrl + tdItems.get(4).child(0).attr("href");
              
              Date articleDate = this.njuFormat.parse(articleTime);
              System.out.println(articelId+" "+articleTime+" "+articleTitle);
    		  if(!Utils.withinTimeRange(new Date(), articleDate, this.day))
    			  continue;
              articles.add(new ArticleInfo(articleUrl, articleDate, articleTitle, articelId, isSingle));
            }
          }
      }
    }
    
    return articles;
  }
  
  protected String getNextURL(Document doc, String itemBaseUrl)
  {
    String nextUrl = null;
    Elements linkElements = doc.getElementsByTag("a");
    for (org.jsoup.nodes.Element link : linkElements) {
      if (link.text().contains("上一页")) {
        nextUrl = link.attr("href");
        break;
      }
    }
    int start = 0;
    Matcher matcher = this.startPattern.matcher(nextUrl);
    if (matcher.find()) {
      try {
        start = Integer.parseInt(matcher.group(1));
      } catch (Exception e) {
        this.logger.error("parse NJU start integer exception", e);
      }
    }
    if (start != 0) nextUrl = itemBaseUrl + "&start=" + (start - 1); else {
      nextUrl = itemBaseUrl + nextUrl;
    }
    return nextUrl;
  }
}
