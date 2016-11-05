package com.makao.bbs_crawler.school.sj;

import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.ItemCrawler;
import com.makao.bbs_crawler.util.Constants;
import com.makao.bbs_crawler.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemCrawler_SJ
  extends ItemCrawler
{
  private Set<String> urlset = new HashSet();
  
  private SimpleDateFormat sjFormat = new SimpleDateFormat("yyyy MMM d H:mm", Locale.ENGLISH);
  private Pattern pattern = Pattern.compile("[0-9]+");
  private int day=1;
  
  public ItemCrawler_SJ(Queue<ArticleInfo> queue, String bbsUrlXml) {
    super(queue, bbsUrlXml);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.SJ.getType();
  }
  
  protected List<ArticleInfo> getArticleInfos(Document doc, String articleBaseUrl, boolean isSingle)
    throws Exception
  {
    List<ArticleInfo> articles = new ArrayList();
    
    Elements elements = doc.getElementsByTag("tr");
    
    for (int i = elements.size() - 1; i >= 0; i--) {
      Element trElem = elements.get(i);
      Elements tdElements = trElem.children();
      if (tdElements.size() == 5) {
        String id = tdElements.get(0).html();
        Matcher matcher = this.pattern.matcher(id);
        Element urlElement = null;
        if (tdElements.get(4).children().size() != 0) {
          urlElement = tdElements.get(4).child(0);
          
          String articleUrl = articleBaseUrl + urlElement.attr("href");
          if ((matcher.matches()) && (!this.urlset.contains(articleUrl))) {
            String title = urlElement.html().substring(2);
            String time = this.yearFormat.format(new Date()) + " " + tdElements.get(3).html();
            Date date = this.sjFormat.parse(time);
  		  	if(!Utils.withinTimeRange(new Date(), date, this.day))
  			  continue;
            articles.add(new ArticleInfo(articleUrl, date, title, id, isSingle));
          }
        }
      }
    }
    return articles;
  }
  
  protected String getNextURL(Document doc, String itemBaseUrl)
  {
    String nextUrl = null;
    
    Elements elements = doc.select("a[href~=bbstdoc,board,ITCareer,page*]");
    if ((elements != null) && (elements.size() > 0)) {
      for (Element elem : elements) {
        if (elem.html().equals("上一页")) {
          nextUrl = itemBaseUrl + elem.attr("href");
        }
      }
    }
    return nextUrl;
  }
}
