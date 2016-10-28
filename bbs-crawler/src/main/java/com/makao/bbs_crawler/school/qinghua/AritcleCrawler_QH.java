package com.makao.bbs_crawler.school.qinghua;
import com.makao.bbs_crawler.ArticleCrawler;
import com.makao.bbs_crawler.Configure;
import com.makao.bbs_crawler.Crawler;
import com.makao.bbs_crawler.util.Constants;
import java.io.IOException;
import java.util.Queue;
import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AritcleCrawler_QH extends ArticleCrawler
{
  public AritcleCrawler_QH(Queue<com.makao.bbs_crawler.ArticleInfo> queue, Configure.SleepTime sleeptime)
  {
    super(queue, sleeptime);
  }
  
  protected String getJobContent(com.makao.bbs_crawler.ArticleInfo article)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String content = null;
    String html = this.crawler.getHtml(article.getUrl());
    Document doc = Jsoup.parse(html);
    org.jsoup.select.Elements elements = doc.getElementsByClass("a-content");
    if ((elements != null) && (elements.size() > 0)) {
      content = extractLines(elements.get(0).child(0).html());
    }
    return content;
  }
  
  private String extractLines(String content) {
    String[] lines = content.split("<br />");
    StringBuilder re = new StringBuilder("");
    
    boolean append = false;
    if (lines.length == 1) {
      append = true;
    }
    for (String line : lines) {
      line = line.trim();
      if (append) {
        re.append(line);
        re.append("<br/>");
      }
      if (line.startsWith("发信站:")) {
        append = true;
      }
    }
    return re.toString();
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.QINGHUA.toString();
  }
}
