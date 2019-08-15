package com.caveofprogramming.spring.web.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Component("messageDao")
@Transactional
public class MessageDao {

	private NamedParameterJdbcTemplate jdbc;

	@Autowired
	private SessionFactory sessionFactory;

	public Session session() {
		return sessionFactory.getCurrentSession();
	}

	public List<Offer> getMessages(String username) {
		Criteria crit = session().createCriteria(Message.class);
		crit.add(Restrictions.eq("username", username));
		return crit.list();
	}

	public void saveOrUpdate(Message vMessage) {

		session().saveOrUpdate(vMessage);
	}

	public boolean delete(int id) {
		Query query = session().createQuery("delete from message where id=:id");
		query.setLong(1, id);
		return query.executeUpdate() == 1;
	}

	public Message getMessage(int id) {
		Criteria crit = session().createCriteria(Message.class);
		crit.add(Restrictions.eq("enabled", true));
		crit.add(Restrictions.idEq(id));
		return (Message) crit.uniqueResult();
	}
}
