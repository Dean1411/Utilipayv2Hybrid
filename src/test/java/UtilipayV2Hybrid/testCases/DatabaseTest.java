package UtilipayV2Hybrid.testCases;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import UtilipayV2Hybrid.testBase.User;
import UtilipayV2Hybrid.utilities.HibernateUtil;

public class DatabaseTest {

    private Session session;

    @BeforeTest
    public void setup() {
        
    	try {
    		
    		session = HibernateUtil.getSessionFactory().openSession();
    		
    	}catch(HibernateException ex) {
    		
    		System.out.println("Exception: " + ex);
    	}
    }

    @Test
    public void verifyUserExists() {

        session.beginTransaction();

        User user = session.createQuery("FROM User WHERE UserName = :username", User.class)
                           .setParameter("username", "Dean.Aschendorf@inzaloems.co.za") // Example username for testing
                           .uniqueResult();

        Assert.assertNotNull(user, "User should exist in the database");

        System.out.println("User Email: " + user.getEmail());

        session.getTransaction().commit();
    }

    @AfterTest
    public void teardown() {
        session.close();
        HibernateUtil.shutDown();
    }
}
