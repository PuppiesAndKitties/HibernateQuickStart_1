package com.yiibai.demos.persistent_to_detached;

/**
 * evict()方法用于将单个对象从session中剔除，
 * 剔除后的对象，如果不重新加入session中，
 * 则对该对象所做的改动都不会被更新到数据库。
 */

import com.yiibai.DataUtils;
import com.yiibai.HibernateUtils;
import com.yiibai.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class EvictDemo {
    public static void main(String[] args) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();
        Employee emp = null;
        try {
            session.getTransaction().begin();

            emp = DataUtils.findEmployee(session, "E7499");

            // ==> true
            System.out.println("- emp Persistent? " + session.contains(emp));

            //使用evict方法将单个对象从session中驱逐
            session.evict(emp);
            System.out.println("- emp Persistent? " + session.contains(emp));

            // 如果不重新将emp加入到session中，那么所有emp的改动都不会更新到数据库。
            emp.setEmpNo("NEW");

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }finally {
            factory.close();
        }
    }
}
