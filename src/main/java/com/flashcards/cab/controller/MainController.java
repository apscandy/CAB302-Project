package com.flashcards.cab.controller;

import com.flashcards.cab.model.Contact;
import com.flashcards.cab.model.IContactDAO;
import com.flashcards.cab.model.SqliteContactDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.List;

public class MainController {

    public VBox contactContainer;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private ListView<Contact> contactsListView;
    private final IContactDAO contactDAO;

    public MainController() {
        contactDAO = new SqliteContactDAO();
//        contactDAO.addContact(new Contact("Jerry", "Doe", "jerrydoe@example.com", "0423423426"));
    }

    private ListCell<Contact> renderCell(ListView<Contact> contactsListView) {
        return new ListCell<Contact>() {

            private void onContactSelected(MouseEvent mouseEvent) {
                ListCell<Contact> clickedCell = (ListCell<Contact>) mouseEvent.getSource();
                // Get the selected contact from the list view
                Contact selectedContact = clickedCell.getItem();
                if (selectedContact != null) selectContact(selectedContact);
            }

            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                if (empty || contact == null || contact.getFullName() == null) {
                    setText(null);
                    super.setOnMouseClicked(this::onContactSelected);
                }else{
                    setText(contact.getFullName());
                }
            }
        };
    };

    @FXML
    public void initialize() {
        contactsListView.setCellFactory(this::renderCell);
        syncContacts();
        contactsListView.getSelectionModel().selectFirst();
        Contact firstConcat = contactsListView.getSelectionModel().getSelectedItem();
        if (firstConcat != null) {
            selectContact(firstConcat);
        }
    }

    public void syncContacts() {
        Contact currentContact = contactsListView.getSelectionModel().getSelectedItem();


        contactsListView.getItems().clear();
        List<Contact> contacts = contactDAO.getAllContacts();
        boolean hasContact = !contacts.isEmpty();
        if (hasContact) {
            contactsListView.getItems().addAll(contacts);
            Contact nextContact = contacts.contains(currentContact) ? currentContact : contacts.get(0);
            contactsListView.getSelectionModel().select(nextContact);
            selectContact(nextContact);
        }
        contactContainer.setVisible(hasContact);
    }

    @FXML
    private void onEditConfirm(){
        Contact selectedContact = contactsListView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            selectedContact.setFirstName(firstNameTextField.getText());
            selectedContact.setLastName(lastNameTextField.getText());
            selectedContact.setEmail(emailTextField.getText());
            selectedContact.setPhone(phoneTextField.getText());
            contactDAO.updateContact(selectedContact);
            syncContacts();
        }
    }

    @FXML
    private void onDelete() {
        Contact selectedContact = contactsListView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            contactDAO.deleteContact(selectedContact);
            syncContacts();
        }
    }


    private void selectContact(Contact contact) {
        contactsListView.getSelectionModel().select(contact);
        firstNameTextField.setText(contact.getFirstName());
        lastNameTextField.setText(contact.getLastName());
        emailTextField.setText(contact.getEmail());
        phoneTextField.setText(contact.getPhone());
    }

    @FXML
    private void onAdd() {
        final String firstName = "New";
        final String lastName = "Contact";
        final String email = "";
        final String phone = "";
        Contact contact = new Contact(firstName, lastName, email, phone);
        contactDAO.addContact(contact);
        syncContacts();
        selectContact(contact);
        firstNameTextField.requestFocus();
    }
    @FXML
    private void onCancel() {
        Contact selectedContact = contactsListView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            selectContact(selectedContact);
        }
    }

}
