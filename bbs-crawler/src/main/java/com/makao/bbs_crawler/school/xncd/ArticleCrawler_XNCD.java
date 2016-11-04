package com.makao.bbs_crawler.school.xncd;

import com.makao.bbs_crawler.ArticleCrawler;
import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.Configure;
import com.makao.bbs_crawler.Crawler;
import com.makao.bbs_crawler.ImageInfo;
import com.makao.bbs_crawler.Logon;
import com.makao.bbs_crawler.SingleInfo;
import com.makao.bbs_crawler.util.Constants;
import com.makao.bbs_crawler.util.Utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class ArticleCrawler_XNCD extends ArticleCrawler
{
  public ArticleCrawler_XNCD(Queue<ArticleInfo> queue, Configure.SleepTime sleeptime)
  {
    super(queue, sleeptime);
  }
  
  protected SingleInfo parseSingleArticle(ArticleInfo article)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    org.jsoup.nodes.Element img = new org.jsoup.nodes.Element(Tag.valueOf("img"), "", new Attributes());
    SingleInfo singleInfo = new SingleInfo();
    String html = this.crawler.getHtml(article.getUrl());
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
    String html = this.crawler.getHtml(article.getUrl());
    Document doc = Jsoup.parse(html);
    Elements tds = doc.select("td[class=t_f]");
	 if(tds!=null && tds.size()>0)
	  {
		  Element td =tds.get(0);
		  if(td!=null){
			  Elements pics = td.select("ignore_js_op");
			  if(pics!=null&&pics.size()>0){//里面包含了图片，替换图片
				  for(int i=0; i<pics.size();i++){
					  Element pic = pics.get(i);
					  Elements imgs = pic.select("img[file]");
					  if(imgs!=null&&imgs.size()>0){
						  Element img = imgs.get(0);
						  Element e =  doc.createElement("div");
						  e.appendElement("img").attr("src", img.attr("file"));
						  pic.replaceWith(e);
					  }
				  }
			  }
			 content = td.html()+".";
		  }
	  }
    return content;
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.XNCD.toString();
  }
  
  public static void main(String[] args){
	  File input = new File("C:/Users/ZYR/Desktop/cdlg_detail.html");
	  Document doc=null;
	  String content = null;
		try {
			doc = Jsoup.parse(input, "UTF-8");
//			String replace = "div><img src='aaaaaa'/></div";
//			Element img = doc.createElement(replace);
//			Element body = doc.select("head").get(0);
//			body.replaceWith(img);
//			System.out.println(doc.html());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Elements tds = doc.select("td[class=t_f]");
		 if(tds!=null && tds.size()>0)
		  {
			  Element td =tds.get(0);
			  if(td!=null){
				  Elements pics = td.select("ignore_js_op");
				  if(pics!=null&&pics.size()>0){//里面包含了图片，替换图片
					  //List<Element> picList = new ArrayList<Element>();
					  for(int i=0; i<pics.size();i++){
						  Element pic = pics.get(i);
						  Elements imgs = pic.select("img[file]");
						  if(imgs!=null&&imgs.size()>0){
							  Element img = imgs.get(0);
							  Element e =  doc.createElement("div");
							  e.appendElement("img").attr("src", img.attr("file"));
							  pic.replaceWith(e);
						  }
					  }
					  
//					  for(int j=0; j<replaceimgs.size();j++){
//						  Element rep = doc.createElement(replaceimgs.get(j));
//						  pics.get(j)
//					  }
				  }
				  
				 content = td.html();
				 /* content = ct.substring(ct.indexOf("prints(")+8, ct.length()-22);
				  content = content.replaceAll("\\\\n", "<br>").replaceAll("\\\\/", "\\/");*/
				 // System.out.println(content);
			  }
		  }
		 System.out.println(content);
	   
  }
}
