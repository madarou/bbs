package com.makao.bbs_crawler;

public class ImageInfo
{
  private String id;
  private byte[] blob;
  
  public ImageInfo(String id, byte[] blob)
  {
    this.id = id;
    this.blob = blob;
  }
  
  public String getId() { return this.id; }
  
  public byte[] getBlob() {
    return this.blob;
  }
}
