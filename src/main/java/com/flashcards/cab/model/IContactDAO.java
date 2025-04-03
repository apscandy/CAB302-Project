package com.flashcards.cab.model;

import java.util.List;

public interface IContactDAO {
    public void addContact(Contact contact);
    public void updateContact(Contact contact);
    public void deleteContact(Contact contact);
    public Contact getContact(int id);
    public List<Contact> getAllContacts();
}
