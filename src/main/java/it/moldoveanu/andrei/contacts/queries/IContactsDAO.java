package it.moldoveanu.andrei.contacts.queries;

import java.util.Vector;

import it.moldoveanu.andrei.contacts.entities.Person;


public interface IContactsDAO {
	
	public boolean logIn(String username, String password);
	
	public int getUserId(String username, String password);
	
	public void addPerson(Person person);
	
	public Vector<Person> showContacts(Integer id);
	
	public void deletePerson(Person person);
	
	public void updatePerson(Person person);
	
}
