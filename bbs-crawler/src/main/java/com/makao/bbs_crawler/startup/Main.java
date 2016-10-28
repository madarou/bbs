package com.makao.bbs_crawler.startup;

import com.makao.bbs_crawler.Configure;
import com.makao.bbs_crawler.quartz.ItemCrawlerJob;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class Main
{
  private Configure configure = Configure.configure;
  private Logger logger = Logger.getLogger(Configure.class);
  
  public void start() { Scheduler scheduler = null;
    try
    {
      scheduler = StdSchedulerFactory.getDefaultScheduler();
      JobDetail job = JobBuilder.newJob(ItemCrawlerJob.class)
        .withIdentity("ItemCrawlerJob", "group1")
        .build();
      Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("trigger1", "group1")
         .withSchedule(CronScheduleBuilder.cronSchedule("0 */2 * * * ?"))
       // .withSchedule(CronScheduleBuilder.cronSchedule("0 0,30 0/1 * * ?"))
        .build();
      scheduler.scheduleJob(job, trigger);
      scheduler.start();
    }
    catch (SchedulerException e)
    {
      e.printStackTrace();
      this.logger.info("------- Shutting Down ---------------------");
      try {
        scheduler.shutdown(true);
      } catch (SchedulerException e1) {
        e1.printStackTrace();
      }
      this.logger.info("------- Shutdown Complete -----------------");
    }
  }
  
  public static void main(String[] args)
    throws InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException, IOException
  {
    new Main().start();
  }
}
