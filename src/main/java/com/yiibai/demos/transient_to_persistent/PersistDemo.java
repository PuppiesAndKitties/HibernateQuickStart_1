package com.yiibai.demos.transient_to_persistent;

import com.yiibai.DataUtils;
import com.yiibai.HibernateUtils;
import com.yiibai.entities.Department;
import com.yiibai.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Date;

public class PersistDemo {
    public static void main(String[] args) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();
        Department department = null;
        Employee emp = null;

        try {
            session.getTransaction().begin();
            Long maxEmpId = DataUtils.getMaxEmpId(session);
            Long empId = maxEmpId + 1;

            // 获取持久化对象
            department = DataUtils.findDepartment(session, "D10");

            // 创建瞬态对象
            emp = new Employee();
            emp.setEmpId(empId);
            emp.setEmpNo("E" + empId);
            emp.setEmpName("Name " + empId);
            emp.setJob("Coder");
            emp.setSalary(1000f);
            emp.setManager(null);
            emp.setHideDate(new Date());
            emp.setDepartment(department);

            /*
            使用persist()方法。
            现在emp由hibernate管理，其有持久化状态，此时对于数据库没有动作。
             */
            session.persist(emp);

            //在这一步，数据被推入数据库，执行插入陈述语句。
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }finally {
            session.close();
            factory.close();
        }
        // 当session关闭/回滚/提交之后，对象emp和dept成为了分离态，不再受到这一个session的管理。
        System.out.println("Emp No:" + emp.getEmpNo());
    }
}
