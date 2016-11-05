package com.makao.bbs_crawler.school.xndx;

import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.Crawler;
import com.makao.bbs_crawler.ItemCrawler;
import com.makao.bbs_crawler.Logon;
import com.makao.bbs_crawler.util.Constants;
import com.makao.bbs_crawler.util.Utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class ItemCrawler_XNDX extends ItemCrawler
{
  private DateFormat timeFormatXNDX = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
  private Pattern pattern = Pattern.compile("^\\S+tid=(\\d+)&\\S+$");
  private int day = 5;//帖子允许的天数前后差值
  public ItemCrawler_XNDX(Queue<ArticleInfo> queue, String bbsUrlXml) {
    super(queue, bbsUrlXml);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.XNDX.getType();
  }
  
  /* (non-Javadoc)
 * @see com.makao.bbs_crawler.ItemCrawler#getArticleInfos(org.jsoup.nodes.Document, java.lang.String, boolean)
 * 解析一个帖子列表，将里面的id，time等取出
 */
protected List<ArticleInfo> getArticleInfos(Document doc, String articleBaseUrl, boolean isSingle) throws Exception
  {
    List<ArticleInfo> articles = new ArrayList();
    Elements tables = doc.select("table[summary=forum_41]");
	  if(tables!=null && tables.size()>0){
		  Element table = tables.get(0);
		  Elements tbodyItems = table.select("tbody[id]");
		  for(int i=0; i<tbodyItems.size();i++){
			  String articleId = null;
			  String articleTitle = null;
			  String articleTime = null;
			  String articleUrl = null;
			  Elements thItems = tbodyItems.get(i).select("th[class=new]");
			  if(thItems==null||thItems.size()==0)
				  thItems = tbodyItems.get(i).select("th[class=common]");
			  if(thItems!=null&&thItems.size()>0){
				  Element thItem = thItems.get(0);
				  Elements aItems = thItem.select("a[onclick=atarget(this)]");
				  if(aItems!=null && aItems.size()>0){
					  Element aItem = aItems.get(0);
					  Matcher matcher = pattern.matcher(aItem.attr("href"));
				      if (matcher.find()) {
				    	  //System.out.println(matcher.group(1));
				    	  articleId = matcher.group(1);
				    	  articleUrl = articleBaseUrl+articleId;
				      }
				      else
				    	  continue;
				      articleTitle = aItem.html();
				  }
				  else
					  continue;
			  }
			  else
				  continue;
			  Elements tdItems = tbodyItems.get(i).select("td[class=by]");
			  if(tdItems!=null&&tdItems.size()>0){
				  Element tdItem = tdItems.get(0);
				  Elements spanItems = tdItem.select("span");
				  if(spanItems!=null&&spanItems.size()>0){
					  Element spanItem = spanItems.get(0);
					  Elements innerSpans = spanItem.select("span[title]");
					  if(innerSpans!=null&&innerSpans.size()>0){
						  articleTime = innerSpans.get(0).attr("title")+timeFormatXNDX.format(new Date()).substring(10);//加上当前时间
					  }
					  else{
						  String pubdate = spanItem.html();//只有日期
						  articleTime = pubdate + timeFormatXNDX.format(new Date()).substring(10);//加上当前时间
					  }
				  }
				  else
					  continue;
			  }
			  else
				  continue;
			  System.out.println(articleId+" "+articleTitle+" "+articleTime);
			  Date articleDate = this.timeFormatXNDX.parse(articleTime);
			  if(!Utils.withinTimeRange(new Date(), articleDate, this.day))
				  continue;
              articles.add(new ArticleInfo(articleUrl, articleDate, articleTitle, articleId, isSingle));
		  }
	  }
    return articles;
  }
  
  protected String getNextURL(Document doc, String itemBaseUrl)
  {
    String nextUrl = null;
    
    String pageIndex=null;
	  Elements fenyes = doc.select("div[class=pg]");
	  if(fenyes!=null&&fenyes.size()>0){
		  Element fenye = fenyes.get(0);
		  Elements strongs = fenye.select("strong");
		  if(strongs!=null&&strongs.size()>0){
			  Element strong = strongs.get(0);
			  pageIndex=strong.html();
		  }
	  }
    
	if(pageIndex!=null){
		nextUrl = itemBaseUrl+"&page="+(Integer.parseInt(pageIndex)+1);
	}
    return nextUrl;
  }
 
  
  public static void main(String[] args) throws IOException, ParseException{
	  DateFormat timeFormatDZKD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	  System.out.println(timeFormatDZKD.format(new Date((1478058546L+8*3600) * 1000)));
	  System.out.println(timeFormatDZKD.format(timeFormatDZKD.parse("2016-11-3 02:14:00")));//11-3页满足格式MM-dd
	  String str ="http://cdut.myubbs.com/forum.php?mod=viewthread&amp;tid=136459&amp;extra=page%3D1%26filter%3Dtypeid%26typeid%3D82";
	  Pattern pattern = Pattern.compile("^\\S+tid=(\\d+)&\\S+$");
	  Matcher matcher1 = pattern.matcher(str);
      if (matcher1.find()) {
    	  System.out.println(matcher1.group(1));
    	  System.out.println("ddd");
      }
	  File input = new File("C:/Users/ZYR/Desktop/xndx.html");
	  Document doc = Jsoup.parse(input, "UTF-8");
	  Elements tables = doc.select("table[summary=forum_41]");
	  if(tables!=null && tables.size()>0){
		  Element table = tables.get(0);
		  Elements tbodyItems = table.select("tbody[id]");
		  for(int i=0; i<tbodyItems.size();i++){
			  String articleId = null;
			  String articleTitle = null;
			  String articleTime = null;
			  String articleUrl = null;
			  Elements thItems = tbodyItems.get(i).select("th[class=new]");
			  if(thItems==null||thItems.size()==0)
				  thItems = tbodyItems.get(i).select("th[class=common]");
			  if(thItems!=null&&thItems.size()>0){
				  Element thItem = thItems.get(0);
				  Elements aItems = thItem.select("a[onclick=atarget(this)]");
				  if(aItems!=null && aItems.size()>0){
					  Element aItem = aItems.get(0);
					  Matcher matcher = pattern.matcher(aItem.attr("href"));
				      if (matcher.find()) {
				    	  //System.out.println(matcher.group(1));
				    	  articleId = matcher.group(1);
				   
				      }
				      else
				    	  continue;
				      articleTitle = aItem.html();
				  }
				  else
					  continue;
			  }
			  else
				  continue;
			  Elements tdItems = tbodyItems.get(i).select("td[class=by]");
			  if(tdItems!=null&&tdItems.size()>0){
				  Element tdItem = tdItems.get(0);
				  Elements spanItems = tdItem.select("span");
				  if(spanItems!=null&&spanItems.size()>0){
					  Element spanItem = spanItems.get(0);
					  Elements innerSpans = spanItem.select("span[title]");
					  if(innerSpans!=null&&innerSpans.size()>0){
						  articleTime = innerSpans.get(0).attr("title")+timeFormatDZKD.format(new Date()).substring(10);//加上当前时间
					  }
					  else{
						  String pubdate = spanItem.html();//只有日期
						  articleTime = pubdate + timeFormatDZKD.format(new Date()).substring(10);//加上当前时间
					  }
				  }
				  else
					  continue;
			  }
			  else
				  continue;
			  System.out.println(articleId+" "+articleTitle+" "+articleTime);
		  }
	  }
	  //找当前页坐标
	  Elements fenyes = doc.select("div[class=pg]");
	  if(fenyes!=null&&fenyes.size()>0){
		  Element fenye = fenyes.get(0);
		  Elements strongs = fenye.select("strong");
		  if(strongs!=null&&strongs.size()>0){
			  Element strong = strongs.get(0);
			  System.out.println(strong.html());
		  }
	  }

  }
}
