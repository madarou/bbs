package com.makao.bbs_crawler;

import com.makao.bbs_crawler.mapper.JobsMapper;
import com.makao.bbs_crawler.mapper.SingleMapper;
import com.makao.bbs_crawler.util.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jsoup.Jsoup;

/**
 * @author ZYR
 *
 */
public abstract class ItemCrawler implements Runnable
{
  protected Crawler cralwer;
  protected Queue<ArticleInfo> queue;
  protected SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  protected SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
  protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ItemCrawler.class);
  private List<BBSUrl> bbsURLs = new ArrayList();
  private String bbsUrlXml;
  
  public ItemCrawler(Queue<ArticleInfo> queue, String bbsUrlXml) {
    this.cralwer = new Crawler();
    this.queue = queue;
    this.bbsUrlXml = bbsUrlXml;
  }
  
  public ItemCrawler(Queue<ArticleInfo> queue, String bbsUrlXml, boolean ssl) {
	    this.cralwer = new Crawler(ssl);
	    this.queue = queue;
	    this.bbsUrlXml = bbsUrlXml;
  }
  
  public HttpClient getHttpClient() {
    return this.cralwer.getHttpClient();
  }
  
  private Set<String> getDatabaseItems(String type) {
    SqlSession session = SessionFactory.getSessionFactory().openSession();
    Set<String> items = new HashSet();
    try {
      Map para = new HashMap();
      para.put("source", getBBSType());
      if ("SINGLE".equals(type)) {
        SingleMapper singlemapper = (SingleMapper)session.getMapper(SingleMapper.class);
        List<Map> idmaps = singlemapper.getDbSingleList(para);
        if ((idmaps != null) && (idmaps.size() > 0)) {
          for (Map map : idmaps) {
            items.add((String)map.get("date") + (String)map.get("title"));
          }
        }
      }
      else {
        JobsMapper jobsmapper = (JobsMapper)session.getMapper(JobsMapper.class);
        List<Map> idmaps = jobsmapper.getDbJobList(para);
        if ((idmaps != null) && (idmaps.size() > 0)) {
          for (Map map : idmaps) {
            items.add((String)map.get("date") + (String)map.get("title"));
          }
        }
      }
      
      this.logger.info(para.get("source") + "-" + type + ":" + items);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      session.close(); }
    return items;
  }
  
  private boolean validate(String title, String bbstype)
  {
    return !BlackList.filter(bbstype, title);
  }
  
  /**
 * @param now
 * @param date
 * @return
 * 时间差在昨天到明天之间
 */
protected boolean withinTimeRange(Date now, Date date)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(now);
    cal.add(5, -1); //等同于cal.add(Calendar.DAY_OF_MONTH, -1);即天数减一天
    Date yesterday = cal.getTime();
    cal.setTime(now);
    cal.add(5, 1);
    Date tomorrow = cal.getTime();
    
    return (date.compareTo(yesterday) >= 0) && (
      date.compareTo(tomorrow) <= 0);
  }
  
  private boolean isDuplicate(Set<String> set, Date date, String title)
  {
    String dayString = Utils.dayFormat.format(date);
    String key = dayString + title;
    if (set.contains(key)) {
      this.logger.debug("duplicate: " + key);
      return true;
    }
    this.logger.debug("not duplicate: " + key);
    return false;
  }
  
  private void parseBBSUrlXml()
  {
    InputStream inputstream = null;
    try
    {
      inputstream = ClassLoader.getSystemClassLoader().getResourceAsStream(this.bbsUrlXml);
      SAXBuilder builder = new SAXBuilder();
      
      org.jdom.Document doc = builder.build(inputstream);
      
      Element root = doc.getRootElement();
      String defaultPageBaseUrl = root.getAttributeValue("page-base-url");
      
      List bbsUrls = root.getChildren("bbs-url");
      for (Iterator localIterator = bbsUrls.iterator(); localIterator.hasNext();) { Object tmp = localIterator.next();
        Element bbsUrl = (Element)tmp;
        String itemUrl = bbsUrl.getChildText("item-url");
        String singleStr = bbsUrl.getChildText("single");
        String pageBaseUrl = null;
        
        Element pageBaseUrlEle = bbsUrl.getChild("page-base-url");
        if (pageBaseUrlEle == null) {
          pageBaseUrl = defaultPageBaseUrl;
        } else {
          pageBaseUrl = pageBaseUrlEle.getText();
        }
        
        boolean single = Boolean.parseBoolean(singleStr);
        
        this.bbsURLs.add(new BBSUrl(itemUrl, pageBaseUrl, single));
      }
    } catch (JDOMException|IOException e) {
      this.logger.error("parse BBS xml exception", e);
      try
      {
        if (inputstream != null)
          inputstream.close();
      } catch (IOException e1) {
        this.logger.error("", e1);
      }
    }
    finally
    {
      try
      {
        if (inputstream != null)
          inputstream.close();
      } catch (IOException e) {
        this.logger.error("", e);
      }
    }
  }
  
  public void run()
  {
    try
    {
      parseBBSUrlXml();//将xml中当前学校的找工作板块的url加入到bbsURLs
      for (BBSUrl item : this.bbsURLs) {
        extrctItem(item);
      }
    } catch (Exception e) {
      this.logger.error("item crawler exception", e);
    }
  }
  
  protected abstract String getBBSType();
  
  protected abstract List<ArticleInfo> getArticleInfos(org.jsoup.nodes.Document paramDocument, String paramString, boolean paramBoolean)
    throws Exception;
  
  protected abstract String getNextURL(org.jsoup.nodes.Document paramDocument, String paramString);
  
  protected String getItemHtml(String url)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    return this.cralwer.getHtml(url);
  }
  
  protected void extrctItemInit() {}
  
  /**
 * @param bbsUrl
 * @throws ClientProtocolException
 * @throws ClassNotFoundException
 * @throws IOException
 * @throws InterruptedException
 * 根据一个bbs-url，通过列表根url，获取文章列表，放进ArticleInfo的列表中，
 * 此时已经根据每个文章的url+id组装好了每个文章的url，后面依此爬取详细内容
 */
private void extrctItem(BBSUrl bbsUrl)
    throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    String itemUrl = bbsUrl.getItemUrl();
    String itemBaseUrl = itemUrl;
    String articleBaseUrl = bbsUrl.getArticleBaseUrl();
    boolean isSingle = bbsUrl.isSingle();
    this.logger.debug(getClass() + " extracting url: " + itemUrl);
    
    extrctItemInit();
    Set<String> dbItemsSet = null;
    if (isSingle) {
      dbItemsSet = getDatabaseItems("SINGLE");
    }
    else {
      dbItemsSet = getDatabaseItems("JOB");
    }
    
    Date now = new Date();
    try
    {
      boolean hasItems = true;
      while (hasItems)
      {
    	/*
    	 * getItemHtml(itemUrl)请求获取到一页的文章列表，如下。然后获取到其中的满足近期发布的且没有被爬过的帖子的id放进去，后面用page-base-url+id号怕取帖子详情
    	 * <?xml version="1.0" encoding="gb18030"?>
			<?xml-stylesheet type="text/xsl" href="../xsl/bbs.xsl?v20150923"?>
			<bbsdoc><session m='t'><p>lt  </p><u>maxiaohong</u><f></f></session><po nore='1' m='+' owner='qcwysh' time= '2016-10-28T14:13:17' id='3098825608663138779'>拜尔斯道夫中国区2017校园招募计划</po>
			<po nore='1' m='+' owner='qcwysh' time= '2016-10-28T14:13:45' id='3098825668306141660'>爱财集团公司诚邀 2017届校园应届毕业生加盟！</po>
			<po nore='1' m='+' owner='vivien' time= '2016-10-28T14:20:32' id='3098826522432111080'>【招聘】美国上市公司 消费者市场研究部</po>
			<brd title='Job_Plaza' desc='求职广场' bm='' total='35825' start='35806' bid='431' page='20' link='t'/>
			</bbsdoc>
    	 * 
    	 * */
        String html = getItemHtml(itemUrl);
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        this.logger.debug(doc.html());
        List<ArticleInfo> allNodes = getArticleInfos(doc, articleBaseUrl, isSingle);
        
        if (allNodes.size() != 0) {
          for (ArticleInfo node : allNodes) {
        	  //帖子发布日期在昨天到明天的一天以内则可以
            if (withinTimeRange(now, node.getDate())) {
              if ((!isDuplicate(dbItemsSet, node.getDate(), node.getTitle())) && 
                (validate(node.getTitle(), getBBSType()))) {
                this.queue.add(node);
              }
            } else {
              this.logger.info("time out of range: " + node.getDate() + ", " + node.getTitle());
              hasItems = false;
              break;
            }
          }
        } else {
          this.logger.info("no items");
          hasItems = false;
        }
        
        //获取进入下一个列表页的url，各个网站情况不同
        String nextUrl = getNextURL(doc, itemBaseUrl);
        //如果当前页面列表所有文章都加进了this.queue，即hasItems为真，则说明下一个列表页可能还有，再进入下一个列表页
        if ((hasItems) && (nextUrl != null) && (!nextUrl.isEmpty())) {
          itemUrl = nextUrl;
          this.logger.info("prepare to go to next item list url: " + itemUrl);
        } else {
          this.logger.info(getClass() + " job item list analysis finished...no more item list currently");
          break;
        }
        
        this.logger.info("Sleep 10 seds.......");
        Thread.sleep(10000L);
      }
    }
    catch (Exception e) {
      this.logger.error("Item crawler exception", e);
    }
  }
}
