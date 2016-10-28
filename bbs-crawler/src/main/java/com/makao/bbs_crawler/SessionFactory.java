package com.makao.bbs_crawler;

import com.makao.bbs_crawler.mapper.SingleMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SessionFactory
{
  private static SqlSessionFactory sqlSessionFactory;
  private static Object lock = new Object();
  
  public static SqlSessionFactory getSessionFactory() {
    String resource = "mybatis-config.xml";
    
    if (sqlSessionFactory == null) {
      synchronized (lock) {
        if (sqlSessionFactory == null) {
          try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
          }
          catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
    
    return sqlSessionFactory;
  }
  
  public static void main(String[] args)
  {
    SqlSession session = getSessionFactory().openSession();
    SingleMapper mapper = (SingleMapper)session.getMapper(SingleMapper.class);
    Map para = new HashMap();
    para.put("id", "1");
    para.put("source", "source");
    para.put("title", "title");
    para.put("content", "dfdfdfd");
    
    mapper.addMessage(para);
    session.commit();
  }
}
