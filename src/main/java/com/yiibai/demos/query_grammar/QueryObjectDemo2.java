package com.yiibai.demos.query_grammar;

import com.yiibai.HibernateUtils;
import com.yiibai.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class QueryObjectDemo2 {
    public static void main(String[] args) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();

        try {
            // 同样地，所有动作要在一个transaction中。
            session.getTransaction().begin();
            String sql = "Select e from " + Employee.class.getName() + " e " + " where e.department.deptNo=:deptNo";
            System.out.println("SQL语句为： Select e from "+ Employee.class.getName() + " e " + "where e.department.deptNo=:deptNo");

            Query<Employee> query = session.createQuery(sql);
            query.setParameter("deptNo", "D10");

            // 执行查询
            List<Employee> employees = query.getResultList();

            for (Employee emp : employees) {
                System.out.println("Emp: " + emp.getEmpNo() + " : " + emp.getEmpName());
            }

            // 提交数据
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }finally {
            session.close();
            factory.close();
        }
    }
}
