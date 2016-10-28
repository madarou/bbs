package com.makao.bbs_crawler.school.ecnu;

import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.ItemCrawler_Discuz;
import com.makao.bbs_crawler.util.Constants;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemCrawler_ECNU
  extends ItemCrawler_Discuz
{
  private DateFormat ecnuFormat = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
  
  public ItemCrawler_ECNU(Queue<ArticleInfo> queue, String bbsUrlXml)
  {
    super(queue, bbsUrlXml);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.ECNU.getType();
  }
  
  protected String getNextURL(Document doc, String itemBaseUrl)
  {
    String nextUrl = itemBaseUrl.substring(0, itemBaseUrl.length() - 6) + this.pageCount + ".html";
    this.pageCount += 1;
    return nextUrl;
  }
  
  protected String getArticleTitle(Element thElement)
  {
    return thElement.child(0).text();
  }
  
  protected String getArticleUrl(Element thElement)
  {
    return thElement.child(0).attr("href");
  }
  
  protected Date getArticleDate(Element byElement) throws ParseException
  {
    String articleTime = byElement.getElementsByTag("span").get(0).text();
    Date date = this.ecnuFormat.parse(articleTime);
    return date;
  }
}
