package com.cab302.cab302project.model;

import java.util.ArrayList;
import java.util.List;

public class MockContactDAO implements IContactDAO {
    public static final ArrayList<Contact> contacts = new ArrayList<>();
    private static int autoIncrementedId  = 1;

    public MockContactDAO(){
        addContact(new Contact("John", "Doe", "johndoe@example.com", "0423423423"));
        addContact(new Contact("Jane", "Doe", "janedoe@example.com", "0423423424"));
        addContact(new Contact("Jay", "Doe", "jaydoe@example.com", "0423423425"));
    }

    @Override
    public void addContact(Contact contact) {
        contact.setId(autoIncrementedId);
        autoIncrementedId++;
        contacts.add(contact);

    }

    @Override
    public void updateContact(Contact contact) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId() == contact.getId()) {
                contacts.set(i, contact);
                break;
            }
        }
    }

    @Override
    public void deleteContact(Contact contact) {
        contacts.remove(contact);
    }

    @Override
    public Contact getContact(int id) {
        for (Contact contact : contacts) {
            if (contact.getId() == id) {
                return contact;
            }
        }
        return null;
    }

    @Override
    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    public void resetMockDatabase(){
        contacts.clear();
        autoIncrementedId  = 1;
        new MockContactDAO();
    }
}
