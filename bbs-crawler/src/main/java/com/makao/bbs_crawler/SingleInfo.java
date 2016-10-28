package com.makao.bbs_crawler;
import java.util.ArrayList;
import java.util.List;

public class SingleInfo
{
  private String content;
  private List<ImageInfo> images;
  
  public SingleInfo()
  {
    this.images = new ArrayList();
  }
  
  public SingleInfo(String content, List<ImageInfo> images) {
    this.content = content;
    this.images = images;
  }
  
  public String getContent() {
    return this.content;
  }
  
  public void setContent(String content) {
    this.content = content;
  }
  
  public List<ImageInfo> getImages() {
    return this.images;
  }
  
  public void setImages(List<ImageInfo> images) {
    this.images = images;
  }
}
