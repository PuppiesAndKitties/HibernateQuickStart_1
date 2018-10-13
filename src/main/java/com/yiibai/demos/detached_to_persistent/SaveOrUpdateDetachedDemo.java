package com.yiibai.demos.detached_to_persistent;

import com.yiibai.DataUtils;
import com.yiibai.HibernateUtils;
import com.yiibai.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Random;

public class SaveOrUpdateDetachedDemo {
    public static void main(String[] args) {
        Employee emp = getEmployee_Detached();
        System.out.println(" - GET EMP " + emp.getEmpId());
        boolean delete = deleteOrNotDelete(emp.getEmpId());
        System.out.println(" - DELETE? " + delete);
        saveOrUpdate_test(emp);
        System.out.println(" - EMP ID " + emp.getEmpId());
    }

    private static Employee getEmployee_Detached() {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session1 = factory.getCurrentSession();
        Employee emp = null;
        try {
            session1.getTransaction().begin();

            Long maxEmpId = DataUtils.getMaxEmpId(session1);
            System.out.println(" - Max Emp ID " + maxEmpId);

            Employee emp2 = DataUtils.findEmployee(session1, "E7839");

            Long empId = maxEmpId + 1;
            emp = new Employee();
            emp.setEmpId(empId);
            emp.setEmpNo("E" + empId);

            emp.setDepartment(emp2.getDepartment());
            emp.setEmpName(emp2.getEmpName());

            emp.setHideDate(emp2.getHideDate());
            emp.setJob("Test");
            emp.setSalary(1000F);

            session1.persist(emp);
            session1.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session1.getTransaction().rollback();
        }
        return emp;
    }

    private static boolean deleteOrNotDelete(Long empId) {
        int random = new Random().nextInt(10);
        if (random < 5) {
            return false;
        }
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session2 = factory.getCurrentSession();

        try {
            session2.getTransaction().begin();
            String sql = "Delete " + Employee.class.getName() + " e "
                    + " where e.empId =:empId ";
            Query query = session2.createQuery(sql);
            query.setParameter("empId", empId);

            query.executeUpdate();

            session2.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            session2.getTransaction().rollback();
            return false;
        }
    }

    private static void saveOrUpdate_test(Employee emp) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session3 = factory.getCurrentSession();
        try {
            session3.getTransaction().begin();

            // Check state of emp
            // ==> false
            System.out.println(" - emp Persistent? " + session3.contains(emp));

            System.out.println(" - Emp salary before update: "
                    + emp.getSalary());

            // Set new salary for Detached emp object.
            emp.setSalary(emp.getSalary() + 100);

            /*
            使用saveOrUpdate(emp)方法将emp转换为持久态。
            如果在同一回话中存在相同ID的对象，该方法将抛出一个异常。
             */
            session3.saveOrUpdate(emp);
            session3.flush();
            System.out.println(" - Emp salary after update: " + emp.getSalary());
            session3.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session3.getTransaction().rollback();
        }finally {
            factory.close();
        }
    }
}
