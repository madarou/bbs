package com.makao.bbs_crawler.school.ecust;
import com.makao.bbs_crawler.ArticleCrawler_Discuz;
import com.makao.bbs_crawler.ArticleInfo;
import com.makao.bbs_crawler.Configure;
import com.makao.bbs_crawler.util.Constants;
import java.util.Queue;

public class ArticleCrawler_ECUST extends ArticleCrawler_Discuz
{
  public ArticleCrawler_ECUST(Queue<ArticleInfo> queue, Configure.SleepTime sleeptime)
  {
    super(queue, sleeptime);
  }
  
  protected String getBBSType()
  {
    return Constants.BBSTYPE.ECUST.getType();
  }
}
