package com.makao.bbs_crawler;

import com.makao.bbs_crawler.mapper.JobsMapper;
import com.makao.bbs_crawler.mapper.SingleMapper;
import com.makao.bbs_crawler.util.Constants;
import com.makao.bbs_crawler.util.Utils;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

public abstract class ArticleCrawler implements Runnable
{
  protected Logger logger = Logger.getLogger(ArticleCrawler.class);
  protected Crawler crawler;
  protected Configure.SleepTime sleeptime;
  protected Queue<ArticleInfo> queue;
  private SqlSession session;
  private static List<java.util.regex.Pattern> titlePart = new ArrayList();
  private static List<java.util.regex.Pattern> titleFull = new ArrayList();
  private static List<java.util.regex.Pattern> contentPart = new ArrayList();
  private static List<java.util.regex.Pattern> contentFull = new ArrayList();
  
  static {
    titlePart.add(java.util.regex.Pattern.compile("实习"));
    titlePart.add(java.util.regex.Pattern.compile("兼职"));
    titlePart.add(java.util.regex.Pattern.compile("家教"));
    titlePart.add(java.util.regex.Pattern.compile("日薪"));
    titlePart.add(java.util.regex.Pattern.compile("\\bintern\\b", 2));
    titlePart.add(java.util.regex.Pattern.compile("\\binternship\\b", 2));
    
    titleFull.add(java.util.regex.Pattern.compile("全职"));
    titleFull.add(java.util.regex.Pattern.compile("社招"));
    titleFull.add(java.util.regex.Pattern.compile("\\bfull time\\b", 2));
    
    contentPart.add(java.util.regex.Pattern.compile("实习生"));
    contentPart.add(java.util.regex.Pattern.compile("兼职"));
    contentPart.add(java.util.regex.Pattern.compile("日薪"));
    contentPart.add(java.util.regex.Pattern.compile("/天"));
    contentPart.add(java.util.regex.Pattern.compile("\\bintern\\b", 2));
    contentPart.add(java.util.regex.Pattern.compile("\\binternship\\b", 2));
    contentPart.add(java.util.regex.Pattern.compile("\\d天以上"));
    contentPart.add(java.util.regex.Pattern.compile("\\d days per week", 2));
    
    contentFull.add(java.util.regex.Pattern.compile("全职"));
    contentFull.add(java.util.regex.Pattern.compile("正式"));
    contentFull.add(java.util.regex.Pattern.compile("校园招聘"));
    contentFull.add(java.util.regex.Pattern.compile("校招"));
    contentFull.add(java.util.regex.Pattern.compile("社招"));
    contentFull.add(java.util.regex.Pattern.compile("\\bfull time\\b", 2));
  }
  
  public ArticleCrawler(Queue<ArticleInfo> queue, Configure.SleepTime sleeptime) {
    this.crawler = new Crawler();
    this.queue = queue;
    this.sleeptime = sleeptime;
  }
  
  public ArticleCrawler(Queue<ArticleInfo> queue, Configure.SleepTime sleeptime, boolean ssl) {
	    this.crawler = new Crawler(ssl);
	    this.queue = queue;
	    this.sleeptime = sleeptime;
	  }
  
  public void run()
  {
    int queueSize = this.queue.size();
    Set<String> articleSet = new HashSet();
    while (!this.queue.isEmpty()) {
      try {
        ArticleInfo articleInfo = (ArticleInfo)this.queue.remove();
        String day = Utils.dayFormat.format(articleInfo.getDate());
        String key = day + articleInfo.getTitle();
        if (articleSet.contains(key)) {
          this.logger.info("article crawler ignore duplicate article: " + key);
        }
        else {
          articleSet.add(key);
          
          int time = this.sleeptime.getMin() + Utils.getRandom(this.sleeptime.getMax() - this.sleeptime.getMin());
          this.logger.info("sleep seconds " + time);
          Thread.sleep(time * 1000);
          int tryCount = 3;
          boolean fail = true;
          do {
            if (articleInfo.isSingle()) {
              fail = singlePageCrawler(articleInfo);
            }
            else {
              fail = jobPageCrawler(articleInfo);
            }
            if (!fail) break; } while (tryCount-- > 0);
        }
        
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    
    this.logger.info(getBBSType() + " given crawl size: " + queueSize);
    this.logger.info(getBBSType() + " actual crawl size: " + articleSet.size());
  }
  
  private boolean singlePageCrawler(ArticleInfo article)
  {
    boolean fail = false;
    try {
      this.session = SessionFactory.getSessionFactory().openSession();
      SingleMapper mapper = (SingleMapper)this.session.getMapper(SingleMapper.class);
      this.logger.info("crawling##" + article.getTitle());
      SingleInfo singleInfo = parseSingleArticle(article);
      if (singleInfo == null)
      {
        fail = true;
      } else {
        for (ImageInfo imageInfo : singleInfo.getImages()) {
          Map picMap = new HashMap();
          picMap.put("id", article.getId());
          picMap.put("source", getBBSType());
          picMap.put("pic_id", imageInfo.getId());
          picMap.put("content", imageInfo.getBlob());
          mapper.addPic(picMap);
        }
        Map para = new HashMap();
        para.put("id", article.getId());
        para.put("source", getBBSType());
        para.put("title", article.getTitle());
        para.put("content", singleInfo.getContent());
        para.put("time", article.getDate());
        para.put("type", "M");
        para.put("pic_tag", (singleInfo.getImages() == null) || (singleInfo.getImages().isEmpty()) ? 
          "N" : "Y");
        mapper.addMessage(para);
        this.session.commit();
        this.logger.info("success:" + article.getUrl());
        fail = false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      this.session.rollback();
      fail = true;
    } finally {
      this.session.close();
    }
    return fail;
  }
  
  protected SingleInfo parseSingleArticle(ArticleInfo article) throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException
  {
    this.logger.info("single article not supported for this school");
    return null;
  }
  
  protected abstract String getBBSType();
  
  protected abstract String getJobContent(ArticleInfo paramArticleInfo) throws ClientProtocolException, ClassNotFoundException, IOException, InterruptedException;
  
  /**
 * @param article
 * @return
 * 
 */
private boolean jobPageCrawler(ArticleInfo article)
  {
    boolean fail = false;
    try {
      this.session = SessionFactory.getSessionFactory().openSession(false);
      JobsMapper mapper = (JobsMapper)this.session.getMapper(JobsMapper.class);
      String title = article.getTitle();
      this.logger.info("crawling##" + title);
      //实际解析一篇帖子的内容
      String content = getJobContent(article);
      if ((content != null) && (!content.isEmpty())) {
        Map para = new HashMap();
        para.put("id", article.getId());
        para.put("source", getBBSType());
        para.put("title", title);
        para.put("content", content);
        para.put("date", article.getDate());
        para.put("jobType", getJobType(title, content));
        mapper.addJobs(para);
        this.session.commit();
        this.logger.info("success:" + article.getUrl());
        fail = false;
      } else {
        this.logger.warn("fail:" + article.getUrl());
        this.logger.warn(article.getUrl());
        this.logger.warn(this.crawler.getHtml(article.getUrl()));
        fail = true;
      }
    } catch (Exception e) {
      this.logger.error("Article crawler exception", e);
      this.session.rollback();
      fail = true;
    } finally {
      if (this.session != null) {
        this.session.close();
        this.session = null;
      }
    }
    return fail;
  }
  
  public HttpClient getHttpClient() {
    return this.crawler.getHttpClient();
  }
  
  private static String getJobType(String title, String content) {
    if ((title == null) || (title.isEmpty()) || (content == null) || (content.isEmpty())) {
      return Constants.JOBTYPE.BOTH.toString();
    }
    boolean partTimeTitle = false;
    boolean fullTimeTitle = false;
    
    for (java.util.regex.Pattern pattern : titlePart) {
      Matcher matcher = pattern.matcher(title);
      if (matcher.find()) {
        partTimeTitle = true;
        break;
      }
    }
    Matcher matcher;
    for (java.util.regex.Pattern pattern : titleFull) {
      matcher = pattern.matcher(title);
      if (matcher.find()) {
        fullTimeTitle = true;
        break;
      }
    }
    
    if ((partTimeTitle) && (fullTimeTitle)) return Constants.JOBTYPE.BOTH.toString();
    if (partTimeTitle) return Constants.JOBTYPE.PART_TIME.toString();
    if (fullTimeTitle) { return Constants.JOBTYPE.FULL_TIME.toString();
    }
    
    boolean partTimeCandidate = false;
    for (java.util.regex.Pattern pattern : contentPart) {
      Matcher matcher1 = pattern.matcher(content);
      if (matcher1.find()) {
        partTimeCandidate = true;
        break;
      }
    }
    
    if (!partTimeCandidate)
    {
      if ((!content.contains("实习")) || (content.contains("实习经")))
      {
        return Constants.JOBTYPE.FULL_TIME.toString();
      }
    }
    for (java.util.regex.Pattern pattern : contentFull) {
      Matcher matcher2 = pattern.matcher(content);
      if (matcher2.find()) {
        return Constants.JOBTYPE.BOTH.toString();
      }
    }
    return Constants.JOBTYPE.PART_TIME.toString();
  }
  
  public static void main(String[] args)
  {
    System.out.println(getJobType(" ", "实习经历优先"));
    System.out.println(getJobType(" ", "没有实习经验怎么办"));
    System.out.println(getJobType(" ", "实习招聘"));
    System.out.println(getJobType(" ", "校园招聘，以及实习生"));
    
    System.out.println(getJobType(" ", "/天"));
    System.out.println(getJobType(" ", "日薪"));
  }
}
