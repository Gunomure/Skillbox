import ORM.Course;
import ORM.Student;
import ORM.Teacher;
import ORM.notifications.AddCourseAlert;
import ORM.notifications.CommentAlert;
import ORM.notifications.Notification;
import ORM.notifications.SendWorkAlert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.usertype.CompositeUserType;

import javax.swing.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        try (SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
             Session session = sessionFactory.openSession()) {

            //=============AddCourseAlert=====================
            session.beginTransaction();
            for (int i = 1; i < 10; i++) {
                Teacher teacher = session.load(Teacher.class, i);
                Course course = session.load(Course.class, i);
                Notification courseAlert = new AddCourseAlert(teacher, "message", "title", course);
                session.persist(courseAlert);
            }
            session.getTransaction().commit();

            List<Notification> courseAlerts = session.createQuery("from AddCourseAlert").getResultList();
            for (Notification item : courseAlerts) {
                System.out.println(item);
            }
            System.out.println(courseAlerts.size());

            //=============CommentAlert=====================
            session.beginTransaction();
            for (int i = 1; i < 10; i++) {
                Teacher teacher = session.load(Teacher.class, i);
                Student student = session.load(Student.class, i);
                Notification commentAlert = new CommentAlert(teacher, "message", "title", student);
                session.persist(commentAlert);
            }
            session.getTransaction().commit();

            List<Notification> commentAlertList = session.createQuery("from CommentAlert").getResultList();
            for (Notification item : commentAlertList) {
                System.out.println(item);
            }

            //=============SendWorkAlert=====================
            session.beginTransaction();
            for (int i = 1; i < 10; i++) {
                Teacher teacher = session.load(Teacher.class, i);
                Student student = session.load(Student.class, i);
                Notification sendWorkAlert = new SendWorkAlert(teacher, "message", "title", student);
                session.persist(sendWorkAlert);
            }
            session.getTransaction().commit();

            List<Notification> sendWorkAlerts = session.createQuery("from SendWorkAlert").getResultList();
            for (Notification item : sendWorkAlerts) {
                System.out.println(item);
            }

            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long rnd(Long min, Long max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}