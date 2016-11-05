package com.makao.bbs_crawler.util;
public abstract class Constants
{
  public static final String SINGLE = "SINGLE";
  
  public static final String JOB = "JOB";
  
  public static final String IMGBASEURI = "rest/single/img/%s/%s/%s";
  public static final String SJBASEURL = "http://bbs.sjtu.cn/";
  public static final String PKUBASEURL = "http://www.bdwm.net/bbs/";
  public static final String QHBASEURL = "http://www.newsmth.net";
  public static final String FDU_SINGLE_BASE_ITEM_LIST_URl = "http://bbs.fudan.edu.cn/bbs/tdoc?bid=120";
  public static final String FDU_SINGLE_BASE_ITEM_PAGE_URL = "http://bbs.fudan.edu.cn/bbs/tcon?new=1&bid=120&f=";
  
  public static enum BBSTYPE
  {
    FDU("FDU"),  SJ("SJ"),  PKU("PKU"),  QINGHUA("QINGHUA"),  NJU("NJU"),  BYR("BYR"), 
    ECNU("ECNU"),  ECUST("ECUST"),  BJTU("BJTU"), CD("CD"), DZKD("DZKD"), CDLG("CDLG"),
    XNCD("XNCD"),CQ("CQ");
    
    private BBSTYPE(String type) { this.type = type; }
    
    private String type;
    public String getType() { return this.type; }
  }
  
  public static enum BBSPLATE {
    SINGLE("SINGLE"),  JOBS("JOBS");
    
    private BBSPLATE(String type) { this.type = type; }
    
    private String type;
    public String getType() { return this.type; }
  }
  
  public static enum JOBTYPE {
    FULL_TIME("FULL"),  PART_TIME("PART"),  BOTH("BOTH");
    
    private JOBTYPE(String type) {
      this.type = type;
    }
    
    private String type;
    public String toString() { return this.type; }
  }
  
  public static enum JOBURL {
    FDU_ITEMLIST_JOB("http://bbs.fudan.edu.cn/bbs/tdoc?bid=431"), 
    FDU_ITEMLIST_ITJOB("http://bbs.fudan.edu.cn/bbs/tdoc?bid=449"), 
    FDU_ITEMLIST_PARTJOB("http://bbs.fudan.edu.cn/bbs/tdoc?bid=40"), 
    FDU_ITEMLIST_CREATION("http://bbs.fudan.edu.cn/bbs/tdoc?bid=123"), 
    
    FDU_ITEMPAGE_JOB("http://bbs.fudan.edu.cn/bbs/tcon?new=1&bid=431&f="), 
    FDU_ITEMPAGE_ITJOB("http://bbs.fudan.edu.cn/bbs/tcon?new=1&bid=449&f="), 
    FDU_ITEMPAGE_PARTJOB("http://bbs.fudan.edu.cn/bbs/tcon?new=1&bid=40&f="), 
    
    SJ_ITEMLIST_ITJOB("http://bbs.sjtu.cn/bbstdoc,board,ITCareer.html"), 
    SJ_ITEMLIST_PARTJOB("http://bbs.sjtu.cn/bbstdoc,board,PartTime.html"), 
    SJ_ITEMLIST_JOBINFO("http://bbs.sjtu.cn/bbstdoc,board,JobInfo.html"), 
    SJ_ITEMLIST_JOBFORM("http://bbs.sjtu.cn/bbstdoc,board,JobForum.html"), 
    SJ_ITEMLIST_ENTERPRENEUR("http://bbs.sjtu.cn/bbstdoc,board,Entrepreneur.html"), 
    
    PKU_ITEMLIST_JOB("http://www.bdwm.net/bbs/bbstop.php?board=Job"), 
    PKU_ITEMLIST_PARTJOB("http://www.bdwm.net/bbs/bbstop.php?board=ParttimeJob"), 
    PKU_ITEMLIST_JOBIT("http://www.bdwm.net/bbs/bbstop.php?board=Job_IT"), 
    PKU_ITEMLIST_JOBPOST("http://www.bdwm.net/bbs/bbstop.php?board=Job_Post"), 
    PKU_ITEMLIST_INNOVATION("http://www.bdwm.net/bbs/bbstop.php?board=Innovation"), 
    
    QH_ITEMLIST_JOB("http://www.newsmth.net/nForum/board/Career_Campus"), 
    QH_ITEMLIST_CAREER("http://www.newsmth.net/nForum/board/ParttimeJobPost"), 
    QH_ITEMLIST_CAREER_INVEST("http://www.newsmth.net/nForum/board/Career_Investment"), 
    QH_ITEMLIST_CAREER_PHD("http://www.newsmth.net/nForum/board/Career_PHD"), 
    QH_ITEMLIST_CAREER_PLAZA("http://www.newsmth.net/nForum/board/Career_Plaza"), 
    QH_ITEMLIST_CAREER_UPGRADE("http://www.newsmth.net/nForum/board/Career_Upgrade"), 
    QH_ITEMLIST_CAREER_ES("http://www.newsmth.net/nForum/board/ExecutiveSearch"), 
    QH_ITEMLIST_INTERN("http://www.newsmth.net/nForum/board/Intern"), 
    QH_ITEMLIST_ITJOB("http://www.newsmth.net/nForum/board/ITjob"), 
    QH_ITEMLIST_ENTERPRENEUR("http://www.newsmth.net/nForum/board/Entrepreneur");
    
    private String url;
    
    private JOBURL(String url) { this.url = url; }
    
    public String toString() { return this.url; } }
  
  public static final String SJ_SINGLE_BASE_ITEM_LIST_URL = "http://bbs.sjtu.cn/bbstdoc,board,LoveBridge.html";
  public static final String PKU_SINGLE_BASE_ITEM_LIST_URL = "http://bbs.pku.edu.cn/bbs/bbstop.php?board=PieBridge";
  public static final String QH_SINGLE_BASE_ITEM_LIST_URL = "http://www.newsmth.net/nForum/board/PieLove";
  public static final String SJPRE = "上一页";
  public static final int TRYTIME = 3;
  public static final String PIC_YES = "Y"; public static final String PIC_NO = "N"; public static final String PUBLISH_MAN = "M"; public static final String PUBLISH_WOMEN = "M"; public static int SUCCESS = 1;
  public static int FAILURE = -1;
  public static String NOAUTHOR = "发生错误";
  public static String PKU_NOAUTHOR = "北大未名站 -- 错误信息";
  public static final int ITEM_TIME_RANGE = 1;
}
