package com.yiibai.demos.transient_to_persistent;

/**
 * 瞬态转化为持久态方法之一： persist()
 */

import com.yiibai.DataUtils;
import com.yiibai.HibernateUtils;
import com.yiibai.entities.Employee;
import com.yiibai.entities.Timekeeper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersistTransientDemo {
    private static DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    private static Timekeeper persist_Transient(Session session, Employee employee) {
        Timekeeper tk1 = new Timekeeper();

        tk1.setEmployee(employee);
        tk1.setInOut(Timekeeper.IN);
        tk1.setDateTime(new Date());

        // 现在 tk1是瞬态对象
        System.out.println("- tk1 Persistent?" + session.contains(tk1));
        System.out.println("===== CALL persist(tk1)... =====");

        // Hibernate为tk1的Id分配数值，所以此处不再需要执行别的动作。
        session.persist(tk1);
        System.out.println("- tk1.getTimekeeperId() = " + tk1.getTimekeeperId());

        // 现在tk1是持久态对象
        System.out.println("- tk1 Persistent?" + session.contains(tk1));
        System.out.println("- Call flush()...");

        session.flush();

        String timekeeperId = tk1.getTimekeeperId();
        System.out.println("- timekeeperId = " + timekeeperId);
        System.out.println("- inOut = " + tk1.getInOut());
        System.out.println("- dateTime = " + df.format(tk1.getDateTime()));
        System.out.println();
        return tk1;
    }

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();
        Employee emp = null;
        try {
            session.getTransaction().begin();
            emp = DataUtils.findEmployee(session, "E7499");
            persist_Transient(session, emp);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }finally {
            factory.close();
        }
    }
}
