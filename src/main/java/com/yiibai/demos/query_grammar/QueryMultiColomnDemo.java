package com.yiibai.demos.query_grammar;

import com.yiibai.HibernateUtils;
import com.yiibai.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class QueryMultiColomnDemo {
    public static void main(String[] args) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();

        try {
            session.getTransaction().begin();

            // 查询多个列
            String sql = "Select e.empId, e.empNo, e.empName from " +
                    Employee.class.getName() + " e ";

            Query<Object[]> query = session.createQuery(sql);

            // 执行查询，获得对象的数组的列表
            List<Object[]> datas = query.getResultList();

            for (Object[] emp : datas) {
                System.out.println("Emp ID:" + emp[0]);
                System.out.println("Emp No:" + emp[1]);
                System.out.println("Emp Name:" + emp[2]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }finally {
            session.close();
            factory.close();
        }
    }
}
