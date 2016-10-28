package com.makao.bbs_crawler;
public class BBSUrl
{
  private String itemUrl = null;
  private String articleBaseUrl = null;
  private boolean isSingle = false;
  
  public BBSUrl(String url, String itemBaseUrl, boolean isSingle) {
    this.itemUrl = url;
    this.articleBaseUrl = itemBaseUrl;
    this.isSingle = isSingle;
  }
  
  public String getItemUrl() {
    return this.itemUrl;
  }
  
  public String getArticleBaseUrl() {
    return this.articleBaseUrl;
  }
  
  public boolean isSingle() {
    return this.isSingle;
  }
}
