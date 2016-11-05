package com.makao.bbs_crawler.school.dzkd;

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

public class ItemCrawler_DZKD extends ItemCrawler
{
	private Pattern pattern = Pattern.compile("^\\S+id=(\\d+)&\\S+$");
  private DateFormat timeFormatDZKD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
  private int day=4;
  
  public ItemCrawler_DZKD(Queue<ArticleInfo> queue, String bbsUrlXml) {
    super(queue, bbsUrlXml);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.DZKD.getType();
  }
  
  /* (non-Javadoc)
 * @see com.makao.bbs_crawler.ItemCrawler#getArticleInfos(org.jsoup.nodes.Document, java.lang.String, boolean)
 * 解析一个帖子列表，将里面的id，time等取出
 */
protected List<ArticleInfo> getArticleInfos(Document doc, String articleBaseUrl, boolean isSingle) throws Exception
  {
    List<ArticleInfo> articles = new ArrayList();
    
	  Elements tables = doc.select("table[width=520]");
	  //System.out.println(tables);
	  if(tables!=null && tables.size()>0){
		  Element table = tables.get(0);
		  Elements trItems = table.getElementsByTag("tr");
		  for(int i=0; i<trItems.size();i=i+2){
			  //两个连续的tr才为一个帖子的信息
			  String articleId = null;
			  String articleTitle = null;
			  String articleTime = null;
			  String articleUrl = null;
			  Elements tdItems = trItems.get(i).getElementsByTag("td");
			  if(tdItems!=null&&tdItems.size()>0){
				  Element tdItem = tdItems.get(0);
				  Elements aItems = tdItem.getElementsByTag("a");
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
				      //articleTitle = new String(aItem.html().getBytes("GB2312"),"UTF-8");
				  }
				  else
					  continue;
			  }
			  else
				  continue;
			  Elements tdItems2 = trItems.get(i+1).getElementsByTag("td");
			  if(tdItems2!=null&&tdItems2.size()>0){
				  Element tdItem = tdItems2.get(0);
				  Elements divItems = tdItem.select("div[class=ds2]");
				  if(divItems!=null&&divItems.size()>0){
					  Element divItem = divItems.get(0);
					  String ct = divItem.html();
					  articleTime = divItem.html().substring(ct.length()-11, ct.length());
					  String dt = timeFormatDZKD.format(new Date());
					  articleTime = dt.substring(0, 5)+articleTime+":00";//加上年和秒
				  }
				  else
					  continue;
			  }
			  else
				  continue;
			  System.out.println(articleId+" "+articleTitle+" "+articleTime);
			  Date articleDate = this.timeFormatDZKD.parse(articleTime);
			  if(!Utils.withinTimeRange(new Date(), articleDate,this.day))
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
	  Elements fenyes = doc.select("TD[class=fenye]");
	  if(fenyes!=null&&fenyes.size()>0){
		  Element fenye = fenyes.get(0);
		  Elements reds = fenye.select("font[color=red]");
		  if(reds!=null&&reds.size()>0){
			  Element red = reds.get(0);
			  Elements indexs = red.getElementsByTag("b");
			  if(indexs!=null&&indexs.size()>0){
				  Element index = indexs.get(0);
				  pageIndex = index.html().trim();
			  }
		  }
	  }
    
	if(pageIndex!=null){
		nextUrl = itemBaseUrl+"&topage="+(Integer.parseInt(pageIndex)+1);
	}
    return nextUrl;
  }
  
  public static void main(String[] args) throws IOException{
	  DateFormat timeFormatDZKD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	  System.out.println(timeFormatDZKD.format(new Date((1478058546L+8*3600) * 1000)));
	  String str ="showbbs.asp?bd=11&id=22170&totable=1";
	  Pattern pattern = Pattern.compile("^\\S+id=(\\d+)&\\S+$");
	  Matcher matcher1 = pattern.matcher(str);
      if (matcher1.find()) {
    	  System.out.println(matcher1.group(1));
    	  System.out.println("ddd");
      }
	  File input = new File("C:/Users/ZYR/Desktop/dzkd.html");
	  Document doc = Jsoup.parse(input, "UTF-8");
	  Elements tables = doc.select("table[width=520]");
	  //System.out.println(tables);
	  if(tables!=null && tables.size()>0){
		  Element table = tables.get(0);
		  Elements trItems = table.getElementsByTag("tr");
		  for(int i=0; i<trItems.size();i=i+2){
			  //两个连续的tr才为一个帖子的信息
			  String articleId = null;
			  String articleTitle = null;
			  String articleTime = null;
			  Elements tdItems = trItems.get(i).getElementsByTag("td");
			  if(tdItems!=null&&tdItems.size()>0){
				  Element tdItem = tdItems.get(0);
				  Elements aItems = tdItem.getElementsByTag("a");
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
			  Elements tdItems2 = trItems.get(i+1).getElementsByTag("td");
			  if(tdItems2!=null&&tdItems2.size()>0){
				  Element tdItem = tdItems2.get(0);
				  Elements divItems = tdItem.select("div[class=ds2]");
				  if(divItems!=null&&divItems.size()>0){
					  Element divItem = divItems.get(0);
					  String ct = divItem.html();
					  articleTime = divItem.html().substring(ct.length()-11, ct.length());
					  String dt = timeFormatDZKD.format(new Date());
					  articleTime = dt.substring(0, 5)+articleTime+":00";
				  }
				  else
					  continue;
			  }
			  else
				  continue;
			  System.out.println(articleId+" "+articleTitle+" "+articleTime);
		  }
	  }
	  
	  //找页面坐标
	  String pageIndex=null;
	  Elements fenyes = doc.select("TD[class=fenye]");
	  if(fenyes!=null&&fenyes.size()>0){
		  Element fenye = fenyes.get(0);
		  Elements reds = fenye.select("font[color=red]");
		  if(reds!=null&&reds.size()>0){
			  Element red = reds.get(0);
			  Elements indexs = red.getElementsByTag("b");
			  if(indexs!=null&&indexs.size()>0){
				  Element index = indexs.get(0);
				  pageIndex = index.html().trim();
			  }
		  }
	  }
	  System.out.println(pageIndex);
	  str = new String(new String("�ƴ�Ѷ��2017У԰��Ƹ���".getBytes("GB2312"),"GBK").getBytes("GBK"),"UTF-8");
	  System.out.println(str);
  }
}
