package com.yiibai.demos.query_grammar;

import com.yiibai.HibernateUtils;
import com.yiibai.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class QueryObjectDemo {
    public static void main(String[] args) {

        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();

        try {
            /*
            通过Hibernate进行的所有动作必须存放在同一个事务transaction
             */
            // 开始事务
            session.getTransaction().begin();
            /*
            创建一个HQL陈述，查询对象，相当于mysql的：
            Select e.* from EMPLOYEE e order by e.EMP_NAME, e.EMP_NO
             */
            String sql = "Select e from " + Employee.class.getName() + " e "
                    + "order by e.empName, e.empNo";
            // 创建查询对象
            Query<Employee> query = session.createQuery(sql);

            // 执行查询
            List<Employee> employees = query.getResultList();

            for (Employee emp : employees) {
                System.out.println("Emp: " + emp.getEmpNo() + " : " + emp.getEmpName());
            }
            // 提交数据
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            // 如果错误发生，就回滚
            session.getTransaction().rollback();
        }finally {
            session.close();
            factory.close();
        }
    }
}
