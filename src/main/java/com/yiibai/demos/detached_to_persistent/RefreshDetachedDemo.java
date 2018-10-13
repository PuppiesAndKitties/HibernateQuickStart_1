package com.yiibai.demos.detached_to_persistent;

/**
 * refresh()方法将会忽略对象从脱管态到重新变成持久态的过程中所作的改动，而是将对象恢复到变成脱管态之前的状态。
 * 会强制发送select语句，以使session缓存中对象的状态和数据表中对应的记录保持一致。
 */

import com.yiibai.DataUtils;
import com.yiibai.HibernateUtils;
import com.yiibai.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class RefreshDetachedDemo {

    public static void main(String[] args) {

        // an Object with Detached status
        Employee emp = getEmployee_Detached();

        System.out.println(" - GET EMP " + emp.getEmpId());

        // Refresh Object
        refresh_test(emp);
    }


    // Return Employee object has Detached state
    private static Employee getEmployee_Detached() {
        SessionFactory factory = HibernateUtils.getSessionFactory();

        Session session1 = factory.getCurrentSession();
        Employee emp = null;
        try {
            session1.getTransaction().begin();

            emp = DataUtils.findEmployee(session1, "E7839");

            // session1 was closed after a commit is called.
            // An Employee record are insert into DB.
            session1.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session1.getTransaction().rollback();
        }
        // Session1 closed 'emp' switch to Detached state.
        return emp;
    }

    private static void refresh_test(Employee emp) {
        SessionFactory factory = HibernateUtils.getSessionFactory();

        // Open other session
        Session session2 = factory.getCurrentSession();

        try {
            session2.getTransaction().begin();


            // Check the status of 'emp' (Detached)
            // ==> false
            System.out.println(" - emp Persistent? " + session2.contains(emp));

            System.out.println(" - Emp salary before update: "
                    + emp.getSalary());

            // Set new salary for 'emp'.
            emp.setSalary(emp.getSalary() + 100);


            // refresh: make a query statement
            // and switch 'emp' to Persistent state
            // The changes are ignored
            session2.refresh(emp);

            // ==> true
            System.out.println(" - emp Persistent? " + session2.contains(emp));

            System.out.println(" - Emp salary after refresh: "
                    + emp.getSalary());

            session2.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session2.getTransaction().rollback();
        }finally {
            factory.close();
        }
    }
}