package com.shop.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class SqlSessionFactoryBean {

    private static SqlSessionFactory sqlSessionFactory = null;

    static {
        if(sqlSessionFactory == null){
            try(Reader reader = Resources.getResourceAsReader("static/dbConfig.xml")){
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            }catch (IOException e){
                e.printStackTrace();
            }//end catch
        }//end if
    }//end static

    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }
}
