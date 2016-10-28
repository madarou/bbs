package com.makao.bbs_crawler.school.fudan;

import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.Crawler;
import com.makao.bbs_crawler.ItemCrawler;
import com.makao.bbs_crawler.Logon;
import com.makao.bbs_crawler.util.Constants;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ItemCrawler_FDU extends ItemCrawler
{
  private DateFormat timeFormatFDU = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
  
  public ItemCrawler_FDU(Queue<ArticleInfo> queue, String bbsUrlXml) {
    super(queue, bbsUrlXml, true);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.FDU.getType();
  }
  
  protected String getItemHtml(String url)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String html = Logon.fudanCheckLogon(this.cralwer, url);
    if ((html == null) || (html.isEmpty())) {
      this.logger.info("Sleeping 10 seconds to try to get html");
      Thread.sleep(10000L);
      html = this.cralwer.getHtml(url);
    }
    return this.cralwer.getHtml(url);
  }
  
  /* (non-Javadoc)
 * @see com.makao.bbs_crawler.ItemCrawler#getArticleInfos(org.jsoup.nodes.Document, java.lang.String, boolean)
 * 解析一个帖子列表，将里面的id，time等取出
 */
protected List<ArticleInfo> getArticleInfos(Document doc, String articleBaseUrl, boolean isSingle) throws Exception
  {
    List<ArticleInfo> articles = new ArrayList();
    
    Elements elements = doc.getElementsByTag("po");
    
    for (int i = elements.size() - 1; i >= 0; i--) {
      org.jsoup.nodes.Element itemElem = elements.get(i);
      if ((!itemElem.hasAttr("sticky")) || (!itemElem.attr("sticky").equals("1"))) {
        String articleId = itemElem.attr("id");
        String articleTime = itemElem.attr("time");
        articleTime = articleTime.replace('T', ' ');
        String articleTitle = itemElem.html().trim();
        
        Date articleDate = this.timeFormatFDU.parse(articleTime);
        articles.add(new ArticleInfo(articleBaseUrl + articleId, articleDate, articleTitle, articleId, isSingle));
      }
    }
    return articles;
  }
  
  protected String getNextURL(Document doc, String itemBaseUrl)
  {
    String nextUrl = null;
    String start = null;
    
    Elements elements = doc.getElementsByTag("brd");
    if ((elements != null) && (elements.size() > 0)) {
      start = elements.first().attr("start");
    }
    if ((start != null) && (!start.isEmpty())) {
      int start_int = Integer.parseInt(start) - 20;
      nextUrl = itemBaseUrl + "&start=" + start_int;
    }
    return nextUrl;
  }
}
