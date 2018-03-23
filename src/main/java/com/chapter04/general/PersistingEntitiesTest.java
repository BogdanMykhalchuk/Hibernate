package com.chapter04.general;

import com.chapter04.model.SimpleObject;
import com.autumncode.hibernate.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class PersistingEntitiesTest {
    SimpleObject obj;
    Long id;

    @Test
    public void testSaveLoad() {
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            SimpleObject obj = new SimpleObject();
            obj.setKey("sl");
            obj.setValue(10L);
            session.save(obj);
            assertNotNull(obj.getId());
            // we should have an id now, set by Session.save()
            id = obj.getId();
            tx.commit();
        }
    }

    @Test
    public void testSavingEntitiesTwice() {
        Long id;
        SimpleObject obj;
        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            obj = new SimpleObject();
            obj.setKey("osas");
            obj.setValue(10L);
            session.save(obj);
            assertNotNull(obj.getId());
            id = obj.getId();
            tx.commit();
        }
        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            obj.setValue(12L);
            session.save(obj);
            tx.commit();
        }
        // note that save() creates a new row in the database!
        // this is wrong behavior. Don't do this!
        assertNotEquals(id, obj.getId());
    }

    @Test
    public void testSaveOrUpdateEntity() {
        Long id;
        SimpleObject obj;
        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            obj = new SimpleObject();
            obj.setKey("osas2");
            obj.setValue(14L);
            session.save(obj);
            assertNotNull(obj.getId());
            id = obj.getId();
            tx.commit();
        }
        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            obj.setValue(12L);
            session.saveOrUpdate(obj);
            tx.commit();
        }
        // saveOrUpdate() will update a row in the database
        // if one matches. This is what one usually expects.
        assertEquals(id, obj.getId());
    }

    @Test
    public void testSaveLoadEquality() {
        Long id = null;
        SimpleObject obj;
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            obj = new SimpleObject();
            obj.setKey("sl");
            obj.setValue(10L);
            session.save(obj);
            assertNotNull(obj.getId());
            // we should have an id now, set by Session.save()
            id = obj.getId();
            tx.commit();
        }
        try (Session session = SessionUtil.getSession()) {
            // we're loading the object by id
            SimpleObject o2 = session.load(SimpleObject.class, id);
            assertEquals(o2.getKey(), "sl");
            assertNotNull(o2.getValue());
            assertEquals(o2.getValue().longValue(), 10L);
            SimpleObject o3 = session.load(SimpleObject.class, id);
            // since o3 and o2 were loaded in the same session, they're not only
            // equivalent - as shown by equals() - but equal, as shown by ==.
            // since obj was NOT loaded in this session, it's equivalent but
            // not ==.
            assertEquals(o2, o3);
            assertEquals(obj, o2);
            assertTrue(o2 == o3);
            assertFalse(o2 == obj);
        }
    }

    @Test
    public void testMerge() {
        Long id;
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            SimpleObject simpleObject = new SimpleObject();
            simpleObject.setKey("testMerge");
            simpleObject.setValue(1L);
            session.save(simpleObject);
            id = simpleObject.getId();
            tx.commit();
        }
        SimpleObject so = validateSimpleObject(id, 1L);
        so.setValue(2L);
        try (Session session = SessionUtil.getSession()) {
            // merge is potentially an update, so we need a TX
            Transaction tx = session.beginTransaction();
            session.merge(so);
            tx.commit();
        }
        validateSimpleObject(id, 2L);
    }
    private SimpleObject validateSimpleObject(Long id, Long value) {
        SimpleObject so = null;
        try (Session session = SessionUtil.getSession()) {
            so = session.load(SimpleObject.class, id);
            assertEquals(so.getKey(), "testMerge");
            assertEquals(so.getValue(), value);
        }
        return so;
    }

    @Test
    public void testRefresh() {
        Long id;
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            SimpleObject simpleObject = new SimpleObject();
            simpleObject.setKey("testMerge");
            simpleObject.setValue(1L);
            session.save(simpleObject);
            id = simpleObject.getId();
            tx.commit();
        }
        SimpleObject so = validateSimpleObject(id, 1L);
        so.setValue(2L);
        System.out.println(so.getValue());
        try (Session session = SessionUtil.getSession()) {
            // note that refresh is a read,
            // so no TX is necessary unless an update occurs later
            session.refresh(so);
        }
        validateSimpleObject(id, 1L);
    }
}
