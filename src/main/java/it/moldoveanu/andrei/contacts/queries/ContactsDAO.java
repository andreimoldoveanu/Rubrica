package it.moldoveanu.andrei.contacts.queries;

import java.math.BigInteger;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import it.moldoveanu.andrei.contacts.entities.LogIn;
import it.moldoveanu.andrei.contacts.entities.Person;


public class ContactsDAO implements IContactsDAO{
	

	static Session sessionObj;
	static SessionFactory sessionFactoryObj;

	public final static Logger logger = Logger.getLogger(ContactsDAO.class);

	private static SessionFactory buildSessionFactory() {
		Configuration configObj = new Configuration();
		configObj.configure("hibernate.cfg.xml");

		ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build(); 

		sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
		return sessionFactoryObj;
	}

	public boolean logIn(String username, String password) {
		try {
			sessionObj = buildSessionFactory().openSession();
			sessionObj.beginTransaction();
			
			Query query = sessionObj.createQuery("from LogIn li where li.userName = :usr and li.password=:pwd");
			query.setParameter("usr", username);
			query.setParameter("pwd", password);
			List<LogIn> list = query.list();
			System.out.println(list.get(0).getUserName());
			return true;
		} catch(Exception sqlException) {
			if(null != sessionObj.getTransaction()) {
				sessionObj.getTransaction().rollback();
			}
			sqlException.printStackTrace();
			return false;
		} finally {
			if(sessionObj != null) {
				sessionObj.close();
			}
		}
	}
	

	public int getUserId(String username, String password) {
		try {
			sessionObj = buildSessionFactory().openSession();
			sessionObj.beginTransaction();
			
			Query query = sessionObj.createQuery("select li.id from LogIn li where li.userName = :usr and li.password = :pwd");
			query.setParameter("usr", username);
			query.setParameter("pwd", password);
			Integer userId = (Integer)query.uniqueResult();
			return userId;
		} catch(Exception sqlException) {
			if(null != sessionObj.getTransaction()) {
				sessionObj.getTransaction().rollback();
			}
			sqlException.printStackTrace();
			return (Integer) null;
		} finally {
			if(sessionObj != null) {
				sessionObj.close();
			}
		}
	}
	
	
		public void addPerson(Person person) {
			int count = 0;
			try {
				sessionObj = buildSessionFactory().openSession();
				sessionObj.beginTransaction();
				sessionObj.save(person);
				sessionObj.getTransaction().commit();
			} catch(Exception sqlException) {
				System.out.println(sqlException);
				if(null != sessionObj.getTransaction()) {
					sessionObj.getTransaction().rollback();
				}
				sqlException.printStackTrace();
			} finally {
				if(sessionObj != null) {
					sessionObj.close();
				}
			}
		}
	
	

		public Vector<Person> showContacts(Integer id){
			try {
				sessionObj = buildSessionFactory().openSession();
				sessionObj.beginTransaction();
				
				Query query = sessionObj.createQuery("from Person where userId = :userId order by id");
				query.setParameter("userId", id);
				List<Person> list = query.list();
				Vector<Person> vector = list.stream()
						.map(p -> new Person(p.getName(), p.getSurname(), p.getAddress(), p.getPhone(), p.getAge(), p.getId(), p.getUserId()))
						.collect(Collectors.toCollection(Vector::new));
				return vector;
			} catch(Exception sqlException) {
				if(null != sessionObj.getTransaction()) {
					sessionObj.getTransaction().rollback();
				}
				sqlException.printStackTrace();
				return null;
			} finally {
				if(sessionObj != null) {
					sessionObj.close();
				}
			}
			
		}
	

		
		public void deletePerson(Person person) {
			try {
				System.out.println("XXXXX DELETE " + person.getId());
				sessionObj = buildSessionFactory().openSession();
				sessionObj.beginTransaction();

				sessionObj.delete(person);

				sessionObj.getTransaction().commit();
			} catch(Exception sqlException) {
				if(null != sessionObj.getTransaction()) {
					logger.info("\n.......Transaction Is Being Rolled Back.......\n");
					sessionObj.getTransaction().rollback();
				}
				sqlException.printStackTrace();
			} finally {
				if(sessionObj != null) {
					sessionObj.close();
				}
			}
			
		}
	

		public void updatePerson(Person person) {
			
			try {
				// Getting Session Object From SessionFactory
				sessionObj = buildSessionFactory().openSession();
				// Getting Transaction Object From Session Object
				sessionObj.beginTransaction();

				// Creating Transaction Entity
				System.out.println("XXXXX UPDATE " + person.getId());
				Person personModify = (Person) sessionObj.get(Person.class, person.getId());
				personModify.setName(person.getName());
				personModify.setSurname(person.getSurname());
				personModify.setAddress(person.getAddress());
				personModify.setPhone(person.getPhone());
				personModify.setAge(person.getAge());

				// Committing The Transactions To The Database
				sessionObj.getTransaction().commit();
				sessionObj.clear();
			} catch(Exception sqlException) {
				if(null != sessionObj.getTransaction()) {
					sessionObj.getTransaction().rollback();
				}
				sqlException.printStackTrace();
			} finally {
				if(sessionObj != null) {
					sessionObj.close();
				}
			}
			
		}
	
	
	

}
