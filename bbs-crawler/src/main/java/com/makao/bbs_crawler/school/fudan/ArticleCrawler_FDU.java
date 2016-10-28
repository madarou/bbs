package com.makao.bbs_crawler.school.fudan;

import com.makao.bbs_crawler.ArticleCrawler;
import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.Configure;
import com.makao.bbs_crawler.Crawler;
import com.makao.bbs_crawler.ImageInfo;
import com.makao.bbs_crawler.Logon;
import com.makao.bbs_crawler.SingleInfo;
import com.makao.bbs_crawler.util.Constants;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Queue;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class ArticleCrawler_FDU extends ArticleCrawler
{
  public ArticleCrawler_FDU(Queue<ArticleInfo> queue, Configure.SleepTime sleeptime)
  {
    super(queue, sleeptime,true);
  }
  
  private String getHtml(ArticleInfo article)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String html = Logon.fudanCheckLogon(this.crawler, article.getUrl());
    if (html == null) {
      html = this.crawler.getHtml(article.getUrl());
    }
    return html;
  }
  
  protected SingleInfo parseSingleArticle(ArticleInfo article)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    org.jsoup.nodes.Element img = new org.jsoup.nodes.Element(Tag.valueOf("img"), "", new Attributes());
    SingleInfo singleInfo = new SingleInfo();
    String html = getHtml(article);
    Document doc = Jsoup.parse(html);
    org.jsoup.select.Elements items = doc.select("pa[m=t]");
    if ((items != null) && (items.size() > 0)) {
      org.jsoup.nodes.Element content = items.get(0);
      org.jsoup.select.Elements pics = content.select("a[href]");
      for (org.jsoup.nodes.Element pic : pics) {
        String url = pic.attr("href");
        String[] pictmp = url.split("/");
        if (pictmp.length > 1) {
          String url_l = pictmp[(pictmp.length - 1)].toLowerCase();
          if ((url_l.endsWith("jpg")) || (url_l.endsWith("jpeg")) || (url_l.endsWith("bmp")) || 
            (url_l.endsWith("png")) || (url_l.endsWith("gif"))) {
            byte[] piccontent = null;
            int trytimes = 5;
            while ((piccontent == null) && (trytimes > 0)) {
              piccontent = this.crawler.getContent(url);
              System.out.println("Pic url: " + url);
              trytimes--;
            }
            singleInfo.getImages().add(new ImageInfo(url_l, piccontent));
            img.attr("src", String.format("rest/single/img/%s/%s/%s", new Object[] { getBBSType(), article.getId(), url_l }));
            pic.replaceWith(img);
          }
        }
      }
      singleInfo.setContent(content.html());
    } else {
      this.logger.warn("fail:" + article.getUrl());
      this.logger.warn(doc);
      return null;
    }
    return singleInfo;
  }
  
  /* (non-Javadoc)
 * @see com.makao.bbs_crawler.ArticleCrawler#getJobContent(com.makao.bbs_crawler.ArticleInfo)
 * 实际解析一篇帖子的内容
 */
protected String getJobContent(ArticleInfo article)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String content = null;
    String html = getHtml(article);
    Document doc = Jsoup.parse(html);
    org.jsoup.select.Elements items = doc.select("pa[m=t]");
    if ((items != null) && (items.size() > 0)) {
      content = items.get(0).html();
    }
    return content;
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.FDU.toString();
  }
}
