
package projekti;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projekti.domain.Account;
import projekti.domain.AccountRepository;
import projekti.domain.Contact;
import projekti.domain.Skill;
import projekti.service.AccountService;
import projekti.service.SkillService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountTest extends  org.fluentlenium.adapter.junit.FluentTest{
    @LocalServerPort
    private Integer port;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private SkillService skillService;
    @Autowired
    private AccountRepository accountRepository;
    
    @Before
    public void setUp() {
        Account account1 = new Account("Mikko Mehiläinen", "mikko123", "salainen", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        accountService.create(account1);
        Account account2 = new Account("Maija Mehiläinen", "maija123", "secret", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        accountService.create(account2);
        Account account3 = new Account("Otto Normalverbraucher", "normal", "PassWort1", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        accountService.create(account3);
        Account account4 = new Account("Gisella Neu", "gisella", "Pass1Wort", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        accountService.create(account3);
        
        Contact contact1 = new Contact(false, "maija123", createAccounts(account1, account2));
        Contact contact2 = new Contact(true, "gisella", createAccounts(account4, account1));
        Skill skill = new Skill("HTML", 0, new ArrayList<>(), account2);
        List<Contact> contacts = account1.getContacts();
        contacts.add(contact2);
        contacts.add(contact1);
        account1.setContacts(contacts);
        accountRepository.save(account1);
        skillService.createSkill(skill, account2);
        accountService.updateSkillList(account2, skill);
        
    }
 
    @Test
    public void loggedInUserCanAddSkillsAndCommentAnotherUserSkills() {
        // Step 1: Go to page login
        goTo("http://localhost:"+port+"/login");       
        // Step 2: Find field "username" and put "username" on it
        find("#username").fill().with("mikko123");    
        // Step 3: Find field "password" and put "Password" on it
        find("#password").fill().with("salainen");        
        // Step 4: Send form 
        find("#login").first().click();
        // Step 6: Go to own site"
        find(By.linkText("mikko123")).click();
        // Step 7: Check that page contains not skill "JavaScript"
        assertFalse("skill was found, although it was not yet added", pageSource().contains("JavaScript"));
        // Step 8: Find field with id "skillContent" and fill it with "JavaScript"       
        find("#skillContent").fill().with("JavaScript");
        // Step 9: Submit form.        
        find("#submitSkill").click();
        // Step 7: Check that page contains skill "JavaScript"       
        assertTrue("skill was not found, although it was added", pageSource().contains("JavaScript"));
        // Step 8: Go to Maija's account page
        goTo("http://localhost:"+port+"/accounts/maija123");
        // Step 9: Check that there is HTML skill.
        assertTrue("skill was not found, although it was added", pageSource().contains("HTML"));
        assertTrue("skill was not found, although it was added", pageSource().contains("Compliments"));
        assertTrue("skill was not found, although it was added", pageSource().contains("0"));
        // Step 10: add new compliment for (only) skill HTML.
        find("#skillList").first().click();
        find("#praise").fill().with("That's Good to know! :)");
        // Step 11: Submit compliment.
        find("#submitPraise").click();
        // Step 12: Check that compliment was added
        assertTrue("skill was not found, although it was added", pageSource().contains("That's Good to know! :)"));       
        assertTrue("skill was not found, although it was added", pageSource().contains("HTML"));
        assertTrue("skill was not found, although it was added", pageSource().contains("Compliments"));
        assertTrue("skill was not found, although it was added", pageSource().contains("1"));
        // Step 13: Compliment can not be added twice to same skill
        find("#skillList").first().click();
        find("#praise").fill().with("That's So Cool");
        // Step 14: Submit compliment.
        find("#submitPraise").click();      
        // Step 12: Check that compliment was not added and text "You have already commented this skill!" is shown
        assertTrue("skill was not found, although it was added", pageSource().contains("HTML"));
        assertTrue("skill was not found, although it was added", pageSource().contains("Compliments"));
        assertTrue("skill has to many praises: expected 1 but was", pageSource().contains("1"));
        assertTrue(pageSource().contains("You have already commented this skill!"));        
        
        // Step 13: Check that mikko123 can not add skills to maija123
        assertFalse(pageSource().contains("Add new skills"));
    }
    
    public void userCanAddAcceptAndDeleteContacts() {
        // Go to page "search"
        goTo("http://localhost:"+port+"/accounts/mikko123");
        // Chech firts that "Otto Normalverbraucher" is not on contact list.
        assertFalse(pageSource().contains("Otto NormalVerbraucher")); 
        
        goTo("http://localhost:"+port+"/search");
        assertTrue(pageSource().contains("No results."));
        // Find field with id "searchFiled" and fill it with "m"        
        find("#searchField").fill().with("m");
        // Submit form.
        find("form").first().click(); 
        // Check that search results are shown.
        assertTrue(pageSource().contains("Maija Mehiläinen"));
        assertTrue(pageSource().contains("Otto Normalverbraucher"));
        assertTrue(pageSource().contains("Mikko Mehiläinen"));
        // Add Otto Normalverbrauches as contact (only name which is not contact)
        find("addContact").first().click();
        // go to account of "mikko123"
        find(By.linkText("mikko123")).click();
        
        // check that page contains "Otto NormalVerbraucher"
        assertTrue(pageSource().contains("Otto NormalVerbraucher"));
        // delete request of contact "Otto NormalVerbraucher"
        find("#deleteRequest").first().click();
        // check that page contains not "Otto NormalVerbraucher"        
        assertTrue(pageSource().contains("Otto NormalVerbraucher"));
        
        // check that page contains "Gisella Neu"        
        assertTrue(pageSource().contains("Gisella Neu"));
        // Delete contact "Gisella Neu"
        find("#deleteContact").first().click();
        // check that page contains not "Gisella Neu"         
        assertFalse(pageSource().contains("Gisella Neu"));
        
        //accept contact "Maija Mehiläinen"
        find("#acceptContact").first().click();
        // Check that page contains "Maija Mehiläinen"
        assertTrue(pageSource().contains("Maija Mehiläinen"));
        // Delete contact "Maija Mehiläinen"      
        find("#deleteContact").first().click();
        // Check that page contains "No Added contacts yet."
        assertTrue(pageSource().contains("No added contacts yet."));
        
        find(By.name("Logout")).click();    
        assertFalse(pageSource().contains("mikko123"));
        
    }

    private List<Account> createAccounts(Account account1, Account account2) {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account2);
        accounts.add(account1);
        return accounts;
    }
}
