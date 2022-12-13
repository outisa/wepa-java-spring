package projekti.service;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.domain.Account;
import projekti.domain.AccountRepository;
import projekti.domain.Contact;
import projekti.domain.ContactRepository;

@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository;
    
    @Autowired
    AccountRepository accountRepository;

    public Contact addNewcontact(String adder, List<Account> accounts) {
        Contact contact  = new Contact(false, adder, accounts);
        contactRepository.save(contact);
        return contact;
    }

    public void saveContactToAccount(Account account, Contact contact) {
        List<Contact> contacts = account.getContacts();
        contacts.add(contact);
        account.setContacts(contacts);
        accountRepository.save(account);
    }

    public void accept(Long id) {
        Contact contact = contactRepository.getOne(id);
        contact.setApproved(true);
        contactRepository.save(contact);
    }

    public void delete(Long id) {
        Contact contact = contactRepository.getOne(id);
        List<Account> accounts = contact.getAccounts();
        updateAccounts(accounts, contact);
        contactRepository.delete(contact);
    }

    public List<String> findContacts(Account account) {
        List<Contact> contacts = account.getContacts();
        if (!contacts.isEmpty()) {        
            List<String> alreadyContact = contacts.stream().flatMap(c -> c.getAccounts().stream().map(a-> a.getUsername()))
                    .collect(Collectors.toCollection(ArrayList::new));
            System.out.println(alreadyContact);
            return alreadyContact;            
        } else {
            List<String> noContacts= new ArrayList<>();
            noContacts.add(account.getUsername());
            System.out.println(noContacts);
            return noContacts;
        }
    }
    
    private void updateAccounts(List<Account> accounts, Contact contact) {
        for (Account account: accounts) {
            List<Contact> contacts = account.getContacts();
            contacts.remove(contact);
            account.setContacts(contacts);
            accountRepository.save(account);
        }
    }    
}
