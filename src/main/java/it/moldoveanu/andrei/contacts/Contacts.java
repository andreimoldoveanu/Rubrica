package it.moldoveanu.andrei.contacts;

import it.moldoveanu.andrei.contacts.entities.Person;

public interface Contacts {

	public void addContact(Person person);
	
	public void removeContact(Person person);
	
	public void updateContact(Person person);
}
