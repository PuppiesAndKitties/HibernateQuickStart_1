package com.yiibai.demos.transient_to_persistent;

import com.yiibai.DataUtils;
import com.yiibai.HibernateUtils;
import com.yiibai.entities.Department;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.xml.crypto.Data;

/**
 * 用于演示对象的持久化状态。
 * 当一个对象使用Session的get、load、find方法获取关联数据时，它就处于持久化状态。
 */
public class PersistentDemo {
    public static void main(String[] args) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();
        Department department = null;

        try {
            session.getTransaction().begin();

            System.out.println("- Finding Department deptNo = D10");

            // 持久化对象
            department = DataUtils.findDepartment(session, "D10");

            System.out.println(" - First Change Location");

            // 改变持久化对象的一些数据
            department.setLocation("Chicago" + System.currentTimeMillis());
            System.out.println("- Loaction = " + department.getLocation());
            System.out.println("- Calling flush...");
            /*
            使用Session.flush()方法主动将改动同步到数据库。
            这个方法对所有改动过的对象都有效。
             */
            session.flush();

            System.out.println("- Flush OK");
            System.out.println("- Second Change Location");
            System.out.println("- Calling commit...");

            session.getTransaction().commit();
            System.out.println("- Commit OK");
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session = factory.getCurrentSession();
        try {
            session.getTransaction().begin();
            System.out.println("Finding Department deptNo = D10...");

            // 查询Department D10
            department = DataUtils.findDepartment(session, "D10");
            System.out.println(" - D10 Laction = " + department.getLocation());

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            factory.close();
        }
    }
}
