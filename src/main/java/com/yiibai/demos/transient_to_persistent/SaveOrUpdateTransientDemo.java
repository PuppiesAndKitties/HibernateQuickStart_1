package com.yiibai.demos.transient_to_persistent;

import com.yiibai.DataUtils;
import com.yiibai.HibernateUtils;
import com.yiibai.entities.Employee;
import com.yiibai.entities.Timekeeper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveOrUpdateTransientDemo {
    private static DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static Timekeeper saveOrUpdate_Transient(Session session, Employee emp) {
        Timekeeper tk3 = new Timekeeper();
        tk3.setEmployee(emp);
        tk3.setInOut(Timekeeper.IN);
        tk3.setDateTime(new Date());
        System.out.println("- tk3 Persistent? " + session.contains(tk3));

        System.out.println("====== CALL saveOrUpdate(tk).... ===========");

        /*
        在这个部分，hibernate会检查tk3是否有ID(timekeeperId)，
        如果没有，就会自动分配一个。
         */
        session.saveOrUpdate(tk3);

        System.out.println("- tk3.getTimekeeperId() = " + tk3.getTimekeeperId());

        /*
        现在tk3是持久态，已经在session中管理了，但是数据库并没有任何增或改的动作。
         */
        System.out.println("- tk3 Persistent? " + session.contains(tk3));

        System.out.println("- Call flush..");

        /*
        flush()之后，会将数据推入数据库。
         */
        session.flush();
        String timekeeperId = tk3.getTimekeeperId();
        System.out.println("- timekeeperId = " + timekeeperId);
        System.out.println("- inOut = " + tk3.getInOut());
        System.out.println("- dateTime = " + df.format(tk3.getDateTime()));
        System.out.println();
        return tk3;
    }

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.getCurrentSession();
        Employee emp = null;
        try {
            session.getTransaction().begin();
            emp = DataUtils.findEmployee(session, "E7499");
            saveOrUpdate_Transient(session, emp);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }finally {
            factory.close();
        }
    }
}
