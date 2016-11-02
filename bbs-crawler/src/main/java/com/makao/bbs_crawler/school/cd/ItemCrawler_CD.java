package com.makao.bbs_crawler.school.cd;

import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.Crawler;
import com.makao.bbs_crawler.ItemCrawler;
import com.makao.bbs_crawler.Logon;
import com.makao.bbs_crawler.util.Constants;
import com.makao.bbs_crawler.util.Utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemCrawler_CD extends ItemCrawler
{
  private DateFormat timeFormatCD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
  
  public ItemCrawler_CD(Queue<ArticleInfo> queue, String bbsUrlXml) {
    super(queue, bbsUrlXml);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.CD.getType();
  }
  
  /* (non-Javadoc)
 * @see com.makao.bbs_crawler.ItemCrawler#getArticleInfos(org.jsoup.nodes.Document, java.lang.String, boolean)
 * 解析一个帖子列表，将里面的id，time等取出
 */
protected List<ArticleInfo> getArticleInfos(Document doc, String articleBaseUrl, boolean isSingle) throws Exception
  {
    List<ArticleInfo> articles = new ArrayList();
    
    Elements scripts = doc.select("script[type]");
	  if(scripts!=null && scripts.size()>1)
	  {
		  Element content =scripts.get(1);
		  System.out.println(content.html());
		  String[] strs = content.html().split("\n");
		  for(String str : strs){
			  if(str.indexOf("c.o(")>-1){
				  String inner = Utils.getContent("c.o",str);
				  if(inner!=null){
					  String[] innerStrs = inner.split(",");
					  String articleId=innerStrs[0];//帖子id
					  String aiticleId2=innerStrs[1];//被回复的帖子的id
					  if(!articleId.equals(aiticleId2))//川大帖子id和被回复帖子id相同说明是原始贴，如果不相同，说明是回复贴，回复贴直接丢掉
						  continue;
					  String articleTime = timeFormatCD.format(new Date((Long.parseLong(innerStrs[4])+8*3600) * 1000));//川大的发帖日期需要转换
					  String articleTitle = innerStrs[5].substring(1, innerStrs[5].length()-1);
					  Date articleDate = this.timeFormatCD.parse(articleTime);
					  System.out.println(articleId+" "+articleTime+" "+articleTitle);
					  if(!Utils.withinTimeRange(new Date(), articleDate))
						  continue;
					  articles.add(new ArticleInfo(articleBaseUrl + articleId, articleDate, articleTitle, articleId, isSingle));
				  }
				 
			  }
		  }
	  }
    
    return articles;
  }
  
  protected String getNextURL(Document doc, String itemBaseUrl)
  {
    String nextUrl = null;
    
    String page = "";
    Elements scripts = doc.select("script[type]");
	if(scripts!=null && scripts.size()>1)
	  {
		  Element content =scripts.get(1);
		  String[] strs = content.html().split("\n");
		  for(String str : strs){
			  if(str.indexOf("var c = new docWriter(")>-1){
				  String inner = Utils.getContent("docWriter",str);
				  page=inner.split(",")[5];
				  break;
			  }
		  }
	  }
	if(!"".equals(page)){
		nextUrl = itemBaseUrl+"&page="+(Integer.parseInt(page)-1);
	}
    return nextUrl;
  }
  
  public static void main(String[] args) throws IOException{
	  DateFormat timeFormatCD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	  System.out.println(timeFormatCD.format(new Date((1478058546L+8*3600) * 1000)));
	  File input = new File("C:/Users/ZYR/Desktop/cd.html");
	  Document doc = Jsoup.parse(input, "UTF-8");
	  Elements scripts = doc.select("script[type]");
	  if(scripts!=null && scripts.size()>1)
	  {
		  Element content =scripts.get(1);
		  System.out.println(content.html());
		  String[] strs = content.html().split("\r\n");
		  for(String str : strs){
			  if(str.indexOf("var c = new docWriter(")>-1){
				  String inner = Utils.getContent("docWriter",str);
				  System.out.println(inner);
			  }
			  if(str.indexOf("c.o(")>-1){
				  String inner = Utils.getContent("c.o",str);
				  if(inner!=null){
					  String[] innerStrs = inner.split(",");
					  String articleId=innerStrs[0];//帖子id
					  String aiticleId2=innerStrs[1];//被回复的帖子的id
					  if(!articleId.equals(aiticleId2))//川大帖子id和被回复帖子id相同说明是原始贴，如果不相同，说明是回复贴，回复贴直接丢掉
						  continue;
					  String articleTime = timeFormatCD.format(new Date((Long.parseLong(innerStrs[4])+8*3600) * 1000));//川大的发帖日期需要转换
					  String articleTitle = innerStrs[5];
					  System.out.println(articleId+" "+articleTime+" "+articleTitle);
				  }
				 
			  }
		  }
	  }
	   
  }
}
