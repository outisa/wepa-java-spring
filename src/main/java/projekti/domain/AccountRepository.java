package projekti.domain;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    
    @EntityGraph(attributePaths = {"contacts"})
    public Account findByUsername(String username);
    
    @EntityGraph(attributePaths = {"contacts"})
    public List<Account> findByNameContainingIgnoreCase(String text);
    
    @EntityGraph(attributePaths = {"contacts"})
    public List<Account> findByUsername(List<String> usernames);
    
    public List<Account> findByContacts(List<Contact> contacts);
}
