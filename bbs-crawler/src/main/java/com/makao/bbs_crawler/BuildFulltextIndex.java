package com.makao.bbs_crawler;

import org.apache.log4j.Logger;

public class BuildFulltextIndex implements Runnable {
  private static Logger log = Logger.getLogger(BuidFulltextIndex.class);
  private static String url = "http://127.0.0.1/solr/dataimport?command=delta-import&clean=false&commit=true";
  private static Crawler crawler = new Crawler();
  
  public void run()
  {
    try {
      crawler.buildIndex(url);
      log.info("fulltext index build success!");
    } catch (Exception e) {
      log.error(e);
    }
  }
}
