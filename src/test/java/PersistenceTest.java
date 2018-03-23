import com.chapter02.hibernate.Message;
import com.chapter03.simple.Person;
import com.chapter03.simple.Ranking;
import com.chapter03.simple.Skill;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class PersistenceTest {
    SessionFactory factory;
    @BeforeSuite
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        factory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }
    @Test
    public void saveMessage() {
        Message message = new Message("Hello, world");
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(message);
            tx.commit();
        }
    }
    @Test(dependsOnMethods = "saveMessage")
    public void readMessage() {
        try (Session session = factory.openSession()) {
            List<Message> list = session.createQuery("from Message",
                    Message.class).list();
            assertEquals(list.size(), 1);
            for (Message m : list) {
                System.out.println(m);
            }
        }
    }
    @Test
    public void testModelCreation() {
        Person subject=new Person();
        subject.setName("J. C. Smell");
        Person observer=new Person();
        observer.setName("Drew Lombardo");
        Skill skill=new Skill();
        skill.setName("Java");
        Ranking ranking=new Ranking();
        ranking.setSubject(subject);
        ranking.setObserver(observer);
        ranking.setSkill(skill);
        ranking.setRanking(8);
        // just to give us visual verification
        System.out.println(ranking);
    }
    @Test
    public void testSavePerson() {
        try(Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Person person = new Person();
            person.setName("J. C. Smell");
            session.save(person);
            tx.commit();
        }
    }
    @Test
    public void testSaveSkill() {
        try(Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Skill skill = new Skill();
            skill.setName("Java");
            session.save(skill);
            tx.commit();
        }
    }

    private Person findPerson(Session session, String name) {
        Query<Person> query = session.createQuery("from Person p where p.name=:name",
                Person.class);
        query.setParameter("name", name);
        Person person = query.uniqueResult();
        return person;
    }

    private Person savePerson(Session session, String name) {
        Person person = findPerson(session, name);
        if (person==null) {
            person=new Person();
            person.setName(name);
            session.save(person);
        }
        return person;
    }

    private Skill findSkill(Session session, String name) {
        Query<Skill> query = session.createQuery("from Skill s where s.name=:name",
                Skill.class);
        query.setParameter("name", name);
        Skill skill = query.uniqueResult();
        return skill;
    }

    private Skill saveSkill(Session session, String name) {
        Skill skill = findSkill(session, name);
        if (skill == null) {
            skill = new Skill();
            skill.setName(name);
            session.save(skill);
        }
        return skill;
    }

    @Test
    public void testSaveRanking() {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Person subject = savePerson(session, "J. C. Smell");
            Person observer = savePerson(session, "Drew Lombardo");
            Skill skill = saveSkill(session, "Java");
            Ranking ranking = new Ranking();
            ranking.setSubject(subject);
            ranking.setObserver(observer);
            ranking.setSkill(skill);
            ranking.setRanking(8);
            session.save(ranking);
            tx.commit();
        }
    }
}
