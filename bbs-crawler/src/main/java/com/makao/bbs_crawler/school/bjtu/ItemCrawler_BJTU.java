package com.makao.bbs_crawler.school.bjtu;
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

public class ItemCrawler_BJTU
  extends ItemCrawler_Discuz
{
  private DateFormat bjtuFormat = new SimpleDateFormat("yyyy-M-d HH:mm", Locale.ENGLISH);
  
  public ItemCrawler_BJTU(Queue<ArticleInfo> queue, String bbsUrlXml) {
    super(queue, bbsUrlXml);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.BJTU.getType();
  }
  
  protected String getNextURL(Document doc, String itemBaseUrl)
  {
    String nextUrl = itemBaseUrl.substring(0, itemBaseUrl.length() - 6) + this.pageCount + ".html";
    this.pageCount += 1;
    return nextUrl;
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
  
  protected Date getArticleDate(Element byElement) throws ParseException
  {
    String articleTime = byElement.getElementsByTag("em").get(0).child(0).text();
    Date date = this.bjtuFormat.parse(articleTime);
    return date;
  }
}
