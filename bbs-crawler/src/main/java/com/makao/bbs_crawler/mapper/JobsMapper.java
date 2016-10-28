package com.makao.bbs_crawler.mapper;

import java.util.List;
import java.util.Map;

public abstract interface JobsMapper
{
  public abstract int addJobs(Map paramMap);
  
  public abstract List<Map> getDbJobList(Map paramMap);
}
