package com.chatchat.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *
 * @author Li
 */
public class SessionFactory {

    private static SqlSessionFactory sessionFactory;

    static {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException ex) {
            Logger.getLogger(SessionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static SqlSessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
