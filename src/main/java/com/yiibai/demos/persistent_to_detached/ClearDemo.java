package com.yiibai.demos.persistent_to_detached;

import com.yiibai.DataUtils;
import com.yiibai.HibernateUtils;
import com.yiibai.entities.Department;
import com.yiibai.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ClearDemo {
    public static void main(String[] args) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();
        Employee emp = null;
        Department dept = null;
        try {
            session.getTransaction().begin();

            /*
            下面是两个具有持久态的对象。
             */
            emp = DataUtils.findEmployee(session, "E7499");
            dept = DataUtils.findDepartment(session, "D10");

            session.clear();

            /*
            现在emp和dept不再是持久态而是脱管态。
             */
            System.out.println("- emp Persistent? " + session.contains(emp));
            System.out.println("- dept Persistent? " + session.contains(dept));

            /*
            对于emp和dept的所有改动将不会被更新到数据库。
             */
            emp.setEmpNo("NEW");

            dept = DataUtils.findDepartment(session, "D20");
            System.out.println("Dept Name = " + dept.getDeptName());

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }finally {
            factory.close();
        }
    }
}
