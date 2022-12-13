
package projekti;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projekti.domain.Account;
import projekti.service.AccountService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentTest extends org.fluentlenium.adapter.junit.FluentTest {
    
    
    @LocalServerPort
    private Integer port;
 
    @Autowired
    private AccountService accountService;
    
    @Before
    public void setUp() {
        Account account1 = new Account("Bilbo Reppuli", "bilbo2000", "secret12", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        accountService.create(account1);        
    }

    @Test
    public void userCanAddCommentsAndSubComments() {
        goTo("http://localhost:"+port+"/login");       
        // Step 2: Find field "username" and put "username" on it
        find("#username").fill().with("bilbo2000");    
        // Step 3: Find field "password" and put "Password" on it
        find("#password").fill().with("secret12");        
        // Step 4: Send form 
        find("#login").first().click();
        // 
        find(By.linkText("Postings")).click();
        //
        assertFalse(pageSource().contains("Here we are")); 
        //
        find(By.name("content")).fill().with("Here we are.");
        find("#sendComment").first().click();  
        assertTrue(pageSource().contains("Here we are."));
        find(By.name("content")).fill().with("");
        find("#sendComment").first().click();  
        assertTrue(pageSource().contains("size must be between 1 and 1000"));      
        assertFalse(pageSource().contains("Oh yes!"));
        find("#subcomment").fill().with("Oh yes!");
        find("#sendSubcomment").click();
        assertTrue(pageSource().contains("Oh yes!"));
        find("#subcomment").fill().with("");
        find("#sendSubcomment").click();
        assertTrue(pageSource().contains("size must be between 1 and 500"));

    }
}
