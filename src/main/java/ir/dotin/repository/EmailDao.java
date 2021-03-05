package ir.dotin.repository;

import ir.dotin.entity.Email;
import ir.dotin.entity.Employee;
import ir.dotin.share.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class EmailDao {
    public void addMessages (Email email) {
        Transaction transaction = null;
        SessionFactory sessionFactory;
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure
                ("META-INF/hibernate.cfg.xml").build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
            transaction = session.beginTransaction();
            session.persist(email);
            transaction.commit();
    }

    public List<Email> messagesReceived(Employee employee) {
        List<Email> receivedEmails = new ArrayList<>();
        SessionFactory sessionFactory;
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure
                ("META-INF/hibernate.cfg.xml").build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
            Query query = session.createQuery("select  " +
                    "email from Email email join email.receiverEmployees employee  " +
                    "  where employee.id =: id");
            query.setParameter("id", employee.getId());
            receivedEmails = query.getResultList();
        return receivedEmails;
    }

    public List<Object[]> detailsMessagesReceived(Employee employee) {
        List<Object[]> receivedEmailsInfo = new ArrayList<>();
        SessionFactory sessionFactory;
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure
                ("META-INF/hibernate.cfg.xml").build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
            Query query = session.createQuery("select email.id " +
                    " sender.firstName , sender.lastName , email.context ,email.subject from Email email , " +
                    "Employee sender join email.receiverEmployees receiver join sender.sentEmails se  " +
                    "  where receiver.id =: id and se.id = email.id");
            query.setParameter("id", employee.getId());
            receivedEmailsInfo = query.getResultList();
        return receivedEmailsInfo;
    }

    public List<Object[]> detailsMessagesSent(Employee employee) {
        List<Object[]> sentEmailsInfo = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createSQLQuery("select email.id " +
                    " ,receiver.c_firstName,receiver.c_lastName,email.c_context,email.c_subject, \n" +
                    "from Employee receiver inner join \n" +
                    "mm_EmployeeEmail employee_email on receiver.id = employee_email.c_receiverId " +
                    "inner join Email email on email.id=employee_email.c_receiveEmailId inner join\n" +
                    "Employee sender on sender.id = email.c_emailSenderId where sender.id = :id");
            query.setParameter("id", employee.getId());
            sentEmailsInfo = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sentEmailsInfo;
    }
}

