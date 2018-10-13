package com.yiibai;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class HibernateUtils {

    private static Configuration config;
    private static SessionFactory sessionFactory;

    static {
        config = new Configuration();
        config.configure(new File("hibernate.cfg.xml"));
        sessionFactory = config.buildSessionFactory();
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
