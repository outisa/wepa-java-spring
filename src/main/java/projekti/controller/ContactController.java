package projekti.controller;

import projekti.domain.Contact;
import projekti.domain.Account;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projekti.domain.AccountRepository;
import projekti.service.ContactService;

@Controller
public class ContactController {
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    ContactService contactService;
    
    @GetMapping("/search")
    public String searchView(Model model) {
        model.addAttribute("results", null);
        return "search";
    }
    
    @PostMapping("/search")
    public String searchBy(@RequestParam String text, Model model) {
        Account account = accountRepository.findByUsername(getCurrentUser());
        if (account==null) {
            model.addAttribute("alreadyContact", null);            
        } else {
            List<String> alreadyContact = contactService.findContacts(account);
            model.addAttribute("alreadyContact", alreadyContact);
        }
        List<Account> accounts = accountRepository.findByNameContainingIgnoreCase(text);        
        model.addAttribute("results", accounts);
        return "search";
    }
    
    @PostMapping("/search/{username}")
    public String addContact(@PathVariable String username) {
        String adder = getCurrentUser();
        Account accountAdder = accountRepository.findByUsername(adder);
        Account account = accountRepository.findByUsername(username);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(accountAdder);
        Contact contact = contactService.addNewcontact(adder, accounts);
        contactService.saveContactToAccount(account, contact);
        contactService.saveContactToAccount(accountAdder, contact);
        return "redirect:/search";
    }
    
    @PostMapping("/contact/{id}/accept")
    public String acceptContact(@PathVariable Long id) {
        String username = getCurrentUser();
        contactService.accept(id);
        return "redirect:/accounts/" +username;
    }
    
    @PostMapping("/contact/{id}/delete")
    public String deleteContact(@PathVariable Long id) {
        String username = getCurrentUser();
        contactService.delete(id);
        return "redirect:/accounts/" + username;
    }    
    
    private String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
    
}
