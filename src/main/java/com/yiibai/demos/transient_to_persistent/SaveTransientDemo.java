package com.yiibai.demos.transient_to_persistent;

import com.yiibai.DataUtils;
import com.yiibai.HibernateUtils;
import com.yiibai.entities.Employee;
import com.yiibai.entities.Timekeeper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveTransientDemo {
    private static DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static Timekeeper persist_Transient(Session session, Employee emp) {
        Timekeeper tk2 = new Timekeeper();
        tk2.setEmployee(emp);
        tk2.setInOut(Timekeeper.IN);
        tk2.setDateTime(new Date());

        // 现在tk2为瞬态
        System.out.println("- tk2 Persistent? " + session.contains(tk2));
        System.out.println("=====CALL save(tk2)=====");
        Serializable id = session.save(tk2);
        System.out.println("- id = " + id);

        System.out.println("- tk2.getTimekeeperId() = " + tk2.getTimekeeperId());

        /*
        现在tk2是持久态，已经在session中被管理。
         */
        System.out.println("- tk2 Persistent? " + session.contains(tk2));
        System.out.println("- Call flush...");
        /*
        为了将数据推入数据库，调用flush()方法。
        如果不调用flush()，那么将在commit()方法时推入数据。
         */
        session.flush();

        String timekeeperId = tk2.getTimekeeperId();
        System.out.println(" - timekeeperId = " + timekeeperId);
        System.out.println(" - dateTime = " + df.format(tk2.getDateTime()));
        System.out.println();
        return tk2;
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
        }
    }
}
