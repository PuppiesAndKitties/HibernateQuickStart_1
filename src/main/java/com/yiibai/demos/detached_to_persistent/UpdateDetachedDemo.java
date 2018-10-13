package com.yiibai.demos.detached_to_persistent;

import com.yiibai.DataUtils;
import com.yiibai.HibernateUtils;
import com.yiibai.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class UpdateDetachedDemo {
    public static void main(String[] args) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session1 = factory.getCurrentSession();
        Employee emp = null;
        try {
            session1.getTransaction().begin();

            // This is a Persistent object.
            emp = DataUtils.findEmployee(session1, "E7499");

            // session1 was closed after a commit is called.
            session1.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session1.getTransaction().rollback();
        }
        // 创建另一个会话
        Session session2 = factory.getCurrentSession();
        try {
            session2.getTransaction().begin();
            System.out.println("- emp Persistent? " + session2.contains(emp));

            System.out.println("Emp salary: " + emp.getSalary());

            emp.setSalary(emp.getSalary() + 100);

            /*
            update()方法只能用于脱管态的对象，而不能用于瞬态的对象。
            使用update()方法将脱管的emp对象重新持久化。
             */
            session2.update(emp);
            session2.flush();
            System.out.println("Emp salary after update: " + emp.getSalary());

            // session2 was closed after a commit is called.
            session2.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session2.getTransaction().rollback();
        }finally {
            factory.close();
        }
    }
}
