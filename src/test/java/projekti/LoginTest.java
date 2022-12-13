
package projekti;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginTest extends org.fluentlenium.adapter.junit.FluentTest {
    
    @LocalServerPort
    private Integer port;
    
    @Test
    public void signUpForUniqueUsernameAndUserCanLogInAndLogOut() {
        //step 1: Go to page registration.
        goTo("http://localhost:"+port+"/registration");
        // Step 2: Find field "name" and put "Maija Mehiläinen" on the field.
        find("#name").fill().with("Maija Mehiläinen");  
        // Step 3: Find field "username" and put "maimeh" on the field.
        find("#username").fill().with("maimeh");
        // Step 4: Find field "password" and put "Password" on the field.
        find("#pwd").fill().with("Password");        
        // Step 5: Find field "controlPassword" and put "Password" on the field.
        find("#controlpwd").fill().with("Password");        
        // Step 6: Send form.
        find("#submit").first().click();        
        // Step 7: Go to page registration again
        find(By.linkText("Sign Up")).click();          
        // Step 8: Find field with id "name" and put "Mari Mesikämmen" on the field.
        find("#name").fill().with("Mari Mesikämmen");  
        // Step 9: Find field with id "username" and put "maimeh" on the field.
        find("#username").fill().with("maimeh");
        // Step 10: Find field with id "password" and put "Password" on the field.
        find("#pwd").fill().with("wordPass");        
        // Step 11: Find field with id "controlPassword" and put "Password" on the field.
        find("#controlpwd").fill().with("wordPass");        
        // Step 12: Send form.
        find("#submit").click();
        // Step 13: registration with same username was not successfull.
        assertTrue("contains not username error", pageSource().contains("Username exists already, please try another one!"));
        // Step 14: Go to Login page.        
        find(By.linkText("Log in")).click();  
        // Step 15: Find field with id "username" and put "maimeh" on it.
        find("#username").fill().with("maimeh");    
        // Step 16: Find field with id "password" and put "Password" on it.
        find("#password").fill().with("Password");        
        // Step 17: Send form. 
        find("#login").click();
        // Step 18: check that page contains text "maimeh"
        assertTrue(pageSource().contains("maimeh"));
        // Step 19: Log out.
        find(By.name("Logout")).click();
    }
    
    @Test
    public void registrationValidationWorksWhenTooShortInput() {
        //step 1: Go to page registration.
        goTo("http://localhost:"+port+"/registration");
        
        // Step 2: Find field "name" and put "Maija Mehiläinen" on the field.
        find("#name").fill().with("q");  
        // Step 3: Find field "username" and put "maimeh" on the field.
        find("#username").fill().with("m");         
        // Step 4: Find field "password" and put "Password" on the field.
        find("#pwd").fill().with("k");
        // Step 5: Find field "controlPassword" and put "Password" on the field.
        find("#controlpwd").fill().with("k");
        // Step 6: Send form.
        find("#submit").first().click();
        assertTrue("contains not name error",pageSource().contains( "size must be between 6 and 30"));
        assertTrue("contains not username error",pageSource().contains( "size must be between 6 and 20"));          
        assertTrue("contains not password error",pageSource().contains("Password must be between 6 and 20 characters!"));        
    }

    
    @Test
    public void userWithWrongCredentialsCanNotLogIn() throws InterruptedException {
        // Step 1: Go to page login
        goTo("http://localhost:"+port+"/login");       
        // Step 2: Find field "username" and put "username" on it
        find("#username").fill().with("username");    
        // Step 3: Find field "password" and put "Password" on it
        find("#password").fill().with("password");        
        // Step 4: Send form 
        find("#login").first().click();
        // Step 5: check that page contains text "Wrong username or password!"
        assertTrue("contains not",pageSource().contains( "Wrong username or password!"));
        
     
    }
}
