package com.makao.bbs_crawler.quartz;

import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.BuildFulltextIndex;
import com.makao.bbs_crawler.Configure;
import com.makao.bbs_crawler.school.bjtu.ArticleCrawler_BJTU;
import com.makao.bbs_crawler.school.byr.ArticleCrawler_BYR;
import com.makao.bbs_crawler.school.byr.ItemCrawler_BYR;
import com.makao.bbs_crawler.school.cd.ArticleCrawler_CD;
import com.makao.bbs_crawler.school.cd.ItemCrawler_CD;
import com.makao.bbs_crawler.school.cdlg.ArticleCrawler_CDLG;
import com.makao.bbs_crawler.school.cdlg.ItemCrawler_CDLG;
import com.makao.bbs_crawler.school.dzkd.ArticleCrawler_DZKD;
import com.makao.bbs_crawler.school.dzkd.ItemCrawler_DZKD;
import com.makao.bbs_crawler.school.ecnu.ArticleCrawler_ECNU;
import com.makao.bbs_crawler.school.ecnu.ItemCrawler_ECNU;
import com.makao.bbs_crawler.school.ecust.ArticleCrawler_ECUST;
import com.makao.bbs_crawler.school.ecust.ItemCrawler_ECUST;
import com.makao.bbs_crawler.school.fudan.ArticleCrawler_FDU;
import com.makao.bbs_crawler.school.fudan.ItemCrawler_FDU;
import com.makao.bbs_crawler.school.nju.ArticleCrawler_NJU;
import com.makao.bbs_crawler.school.nju.ItemCrawler_NJU;
import com.makao.bbs_crawler.school.pku.ArticleCrawler_PKU;
import com.makao.bbs_crawler.school.pku.ItemCrawler_PKU;
import com.makao.bbs_crawler.school.qinghua.AritcleCrawler_QH;
import com.makao.bbs_crawler.school.qinghua.ItemCrawler_QH;
import com.makao.bbs_crawler.school.sj.ArticleCrawler_SJ;
import com.makao.bbs_crawler.school.sj.ItemCrawler_SJ;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ItemCrawlerJob
  implements Job
{
  private static AtomicBoolean running = new AtomicBoolean(false);
  
  Logger logger = Logger.getLogger(ItemCrawlerJob.class);
  
  public void execute(JobExecutionContext context)
    throws JobExecutionException
  {
    if (!running.compareAndSet(false, true)) {
      this.logger.info("another item crawler job is still running, exit!");
      return;
    }
    
    this.logger.info("item crawler job start....");
    
    Queue<ArticleInfo> itemqueue_fdu = new LinkedList();
    Queue<ArticleInfo> itemqueue_sj = new LinkedList();
    Queue<ArticleInfo> itemqueue_pku = new LinkedList();
    Queue<ArticleInfo> itemqueue_qinghua = new LinkedList();
    Queue<ArticleInfo> itemqueue_nju = new LinkedList();
    Queue<ArticleInfo> itemqueue_byr = new LinkedList();
    Queue<ArticleInfo> itemqueue_ecnu = new LinkedList();
    Queue<ArticleInfo> itemqueue_ecust = new LinkedList();
    Queue<ArticleInfo> itemqueue_bjtu = new LinkedList();
    Queue<ArticleInfo> itemqueue_cd = new LinkedList();
    Queue<ArticleInfo> itemqueue_dzkd = new LinkedList();
    Queue<ArticleInfo> itemqueue_cdlg = new LinkedList();
    int threadcount = Configure.configure.getThreadcount();
    
    ExecutorService executor = Executors.newFixedThreadPool(threadcount);
//    executor.execute(new ItemCrawler_FDU(itemqueue_fdu, "com/makao/bbs_crawler/xml/BBSUrls_FDU.xml"));
//    executor.execute(new ItemCrawler_SJ(itemqueue_sj, "com/makao/bbs_crawler/xml/BBSUrls_SJ.xml"));
//    executor.execute(new ItemCrawler_PKU(itemqueue_pku, "com/makao/bbs_crawler/xml/BBSUrls_PKU.xml"));
//    executor.execute(new ItemCrawler_QH(itemqueue_qinghua, "com/makao/bbs_crawler/xml/BBSUrls_QH.xml"));
//    executor.execute(new ItemCrawler_NJU(itemqueue_nju, "com/makao/bbs_crawler/xml/BBSUrls_NJU.xml"));
//    executor.execute(new ItemCrawler_CD(itemqueue_cd, "com/makao/bbs_crawler/xml/BBSUrls_CD.xml"));
//    executor.execute(new ItemCrawler_DZKD(itemqueue_dzkd, "com/makao/bbs_crawler/xml/BBSUrls_DZKD.xml"));
    executor.execute(new ItemCrawler_CDLG(itemqueue_cdlg, "com/makao/bbs_crawler/xml/BBSUrls_CDLG.xml"));
//    executor.execute(new ItemCrawler_BYR(itemqueue_byr, "com/makao/bbs_crawler/xml/BBSUrls_BYR.xml"));//没有账号密码
//    executor.execute(new ItemCrawler_ECNU(itemqueue_ecnu, "com/makao/bbs_crawler/xml/BBSUrls_ECNU.xml"));//网络差，connection time out
//    executor.execute(new ItemCrawler_ECUST(itemqueue_ecust, "com/makao/bbs_crawler/xml/BBSUrls_ECUST.xml"));//原网址停用了，新网址http://bbs.myecust.com/需要账号密码
    
    executor.shutdown();
    try {
      executor.awaitTermination(1000L, TimeUnit.DAYS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    this.logger.info("fdu size:" + itemqueue_fdu.size());
    this.logger.info("sj size:" + itemqueue_sj.size());
    this.logger.info("pku size:" + itemqueue_pku.size());
    this.logger.info("qinghua size:" + itemqueue_qinghua.size());
    this.logger.info("nju size:" + itemqueue_nju.size());
    this.logger.info("byr size:" + itemqueue_byr.size());
    this.logger.info("ecnu size:" + itemqueue_ecnu.size());
    this.logger.info("ecust size:" + itemqueue_ecust.size());
    this.logger.info("bjtu size:" + itemqueue_bjtu.size());
    this.logger.info("cd size:" + itemqueue_cd.size());
    this.logger.info("dzkd size:" + itemqueue_dzkd.size());
    this.logger.info("cdlg size:" + itemqueue_cdlg.size());
    this.logger.info("item crawler job stop....");
    
    executor = Executors.newFixedThreadPool(threadcount);
    executor.execute(new ArticleCrawler_FDU(itemqueue_fdu, Configure.configure.getSleeptime()));
    executor.execute(new ArticleCrawler_SJ(itemqueue_sj, Configure.configure.getSleeptime()));
    executor.execute(new ArticleCrawler_PKU(itemqueue_pku, Configure.configure.getSleeptime()));
    executor.execute(new AritcleCrawler_QH(itemqueue_qinghua, Configure.configure.getSleeptime()));
    executor.execute(new ArticleCrawler_NJU(itemqueue_nju, Configure.configure.getSleeptime()));
    executor.execute(new ArticleCrawler_CD(itemqueue_cd, Configure.configure.getSleeptime()));
    executor.execute(new ArticleCrawler_DZKD(itemqueue_dzkd, Configure.configure.getSleeptime()));
    executor.execute(new ArticleCrawler_CDLG(itemqueue_cdlg, Configure.configure.getSleeptime()));
//    executor.execute(new ArticleCrawler_BYR(itemqueue_byr, Configure.configure.getSleeptime()));
//    executor.execute(new ArticleCrawler_ECNU(itemqueue_ecnu, Configure.configure.getSleeptime()));
//    executor.execute(new ArticleCrawler_ECUST(itemqueue_ecust, Configure.configure.getSleeptime()));
//    executor.execute(new ArticleCrawler_BJTU(itemqueue_bjtu, Configure.configure.getSleeptime()));
    executor.shutdown();
    try
    {
      executor.awaitTermination(45L, TimeUnit.MINUTES);
      
     /* Thread fullTextThread = new Thread(new BuildFulltextIndex());
      fullTextThread.start();
      fullTextThread.join();*/
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    this.logger.info("job done!!!!!!!!!!!!!!!");
    running.set(false);
  }
}
