package com.makao.bbs_crawler.mapper;

import java.util.List;
import java.util.Map;

public abstract interface SingleMapper
{
  public abstract int addMessage(Map paramMap);
  
  public abstract int addPic(Map paramMap);
  
  public abstract List<Map> getDbSingleList(Map paramMap);
}
