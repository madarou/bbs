package com.makao.bbs_crawler;

import java.util.Date;

public class ArticleInfo {
  private String url;
  private String id;
  private Date date;
  private String title;
  private boolean single;
  
  public ArticleInfo(String url, Date date, String title, String id, boolean single) {
    this.url = url;
    this.date = date;
    this.title = title;
    this.id = id;
    this.single = single;
  }
  
  public String getUrl() {
    return this.url;
  }
  
  public void setUrl(String url) { this.url = url; }
  
  public String getTitle() {
    return this.title;
  }
  
  public void setTitle(String title) { this.title = title; }
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String id) { this.id = id; }
  
  public boolean isSingle()
  {
    return this.single;
  }
  
  public void setSingle(boolean single) { this.single = single; }
  
  public String toString() {
    return this.url + "  " + this.id + "  " + "  " + this.date + "  " + this.title + "  " + this.single;
  }
  
  public Date getDate() { return this.date; }
  
  public void setDate(Date date) {
    this.date = date;
  }
}
