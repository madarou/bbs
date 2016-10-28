package com.makao.bbs_crawler.school.ecust;

import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.Crawler;
import com.makao.bbs_crawler.ItemCrawler_Discuz;
import com.makao.bbs_crawler.Logon;
import com.makao.bbs_crawler.util.Constants;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemCrawler_ECUST
  extends ItemCrawler_Discuz
{
  private DateFormat ecustFormat = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
  
  public ItemCrawler_ECUST(Queue<ArticleInfo> queue, String bbsUrlXml) {
    super(queue, bbsUrlXml);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.ECUST.getType();
  }
  
  protected String getItemHtml(String url)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String html = Logon.ecustCheckLogon(this.cralwer, url);
    if ((html == null) || (html.isEmpty())) {
      this.logger.debug("Sleeping 10 seconds to try to get html");
      Thread.sleep(10000L);
      html = this.cralwer.getHtml(url);
    } else {
      return html;
    }
    return this.cralwer.getHtml(url);
  }
  
  protected String getNextURL(Document doc, String itemBaseUrl)
  {
    return itemBaseUrl + "&page=" + this.pageCount++;
  }
  
  protected String getArticleTitle(Element thElement)
  {
    String articleTitle = null;
    Elements aElements = thElement.getElementsByClass("xst");
    for (Element aElement : aElements) {
      if (aElement.hasAttr("href")) {
        articleTitle = aElement.text();
        break;
      }
    }
    return articleTitle;
  }
  
  protected String getArticleUrl(Element thElement)
  {
    String articleUrl = null;
    Elements aElements = thElement.getElementsByClass("xst");
    for (Element aElement : aElements) {
      if (aElement.hasAttr("href")) {
        articleUrl = aElement.attr("href");
        break;
      }
    }
    return articleUrl;
  }
  
  protected Date getArticleDate(Element byElement)
    throws ParseException
  {
    String articleTime = byElement.getElementsByTag("em").get(0).child(0).text();
    Date date = this.ecustFormat.parse(articleTime);
    return date;
  }
}
