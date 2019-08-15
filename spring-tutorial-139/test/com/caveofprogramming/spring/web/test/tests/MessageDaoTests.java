package com.caveofprogramming.spring.web.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caveofprogramming.spring.web.dao.Message;
import com.caveofprogramming.spring.web.dao.MessageDao;
import com.caveofprogramming.spring.web.dao.Offer;
import com.caveofprogramming.spring.web.dao.OffersDao;
import com.caveofprogramming.spring.web.dao.User;
import com.caveofprogramming.spring.web.dao.UsersDao;

@ActiveProfiles("dev")
@ContextConfiguration(locations = {
		"classpath:com/caveofprogramming/spring/web/config/dao-context.xml",
		"classpath:com/caveofprogramming/spring/web/config/security-context.xml",
		"classpath:com/caveofprogramming/spring/web/test/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class MessageDaoTests {

	@Autowired
	private OffersDao offersDao;
	
	@Autowired
	private MessageDao mssagesDao;

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private DataSource dataSource;
	
	private User user1 = new User("test1test1", "testname", "passpass", "email@email.com", true, "ROLE_ADMIN");
	private Offer offer1 = new Offer(user1, "this is text1");
	
	@Before
	public void init() {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);

		jdbc.execute("delete from offers");
		jdbc.execute("delete from users");
		jdbc.execute("delete from messages");
	}

	@Test
	 public void testSave() {
		usersDao.create(user1);
		System.out.println(user1.getUsername());
		Message message1 = new Message("TEstSubject1","Cont","MyName","MyEmail@gail.clm",user1.getUsername());
		mssagesDao.saveOrUpdate(message1);
	}
	//@Test
	public void getUsername() {
		usersDao.create(user1);
		 offersDao.saveOrUpdate(offer1);
		List<Offer> offer = offersDao.getOffers(user1.getUsername());
		assertEquals("expected size 1",1, offer.size());
	}
//	@Test
	public void testOffers() {
	
		User user = new User("johnwpurcell", "John Purcell", "hellothere",
				"john@caveofprogramming.com", true, "user");

		usersDao.create(user);

		Offer offer = new Offer(user, "This is a test offer.");
		offersDao.saveOrUpdate(offer);

		List<Offer> offers = offersDao.getOffers();

		assertEquals("Should be ** offer in database.", 1, offers.size());

		assertEquals("Retrieved offer should match created offer.", offer,
				offers.get(0));

		// Get the offer with ID filled in.
		offer = offers.get(0);

		offer.setText("Updated offer text.");
		 offersDao.saveOrUpdate(offer);

		Offer updated = offersDao.getOffer(offer.getId());

		assertEquals("Updated offer should match retrieved updated offer",
				offer, updated);

		// Test get by ID ///////
		Offer offer2 = new Offer(user, "This is a test offer.");

	  offersDao.saveOrUpdate(offer2);
		
		List<Offer> userOffers = offersDao.getOffers(user.getUsername());
		System.out.println(userOffers);
		assertEquals("Should be "+userOffers.size()+" offers for user.", 2, userOffers.size());
		
		List<Offer> secondList = offersDao.getOffers();
		
		for(Offer current: secondList) {
			Offer retrieved = offersDao.getOffer(current.getId());
			
			assertEquals("Offer by ID should match offer from list.", current, retrieved);
		}
		
		// Test deletion
		offersDao.delete(offer.getId());

		List<Offer> finalList = offersDao.getOffers();

		assertEquals("Offers lists should contain one offer.", 1, finalList.size());
	}
	//@Test
	public void testDelete() {
		User user = new User("deleteTest", "John Purcell", "hellothere",
				"john@caveofprogramming.com", true, "user");
		User user1 = new User("deleteTest1", "John Purcell", "hellothere",
				"john@caveofprogramming.com", true, "user");
		usersDao.create(user);
		usersDao.create(user1);
		
		Offer offer = new Offer(user, "This is a test offer.");
		Offer offer1 = new Offer(user, "This is a test1 offer.");
		offersDao.saveOrUpdate(offer);
		offersDao.saveOrUpdate(offer1);
		offersDao.delete(offer1.getId());
		
		offer1 = offersDao.getOffer(offer1.getId());
		assertNull("this should be null",offer1);
	}
}
