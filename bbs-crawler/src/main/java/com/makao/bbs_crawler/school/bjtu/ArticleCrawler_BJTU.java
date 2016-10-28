package com.makao.bbs_crawler.school.bjtu;

import com.makao.bbs_crawler.ArticleCrawler_Discuz;
import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.Configure;
import com.makao.bbs_crawler.util.Constants;
import java.util.Queue;
import org.jsoup.nodes.Document;

public class ArticleCrawler_BJTU
  extends ArticleCrawler_Discuz
{
  public ArticleCrawler_BJTU(Queue<ArticleInfo> queue, Configure.SleepTime sleeptime)
  {
    super(queue, sleeptime);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.BJTU.getType();
  }
  
  protected void modifyArticleDate(ArticleInfo articleInfo, Document document) {}
}
