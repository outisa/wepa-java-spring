
package projekti.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projekti.domain.Account;
import projekti.domain.AccountRepository;
import projekti.domain.Comment;
import projekti.domain.Contact;
import projekti.domain.Skill;
import projekti.domain.SkillRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;  
    
    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;      

    public void updateLikedComments(Comment comment, Account account) {
        List<Comment> likedComments = account.getLikedComments();
        likedComments.add(comment);
        account.setLikedComments(likedComments);
        accountRepository.save(account);
    }

    public Map getAccountModels(Account account) {
        Pageable pageable = PageRequest.of(0, 30, Sort.by("complimentsTotal").descending());
        
        List<Skill> skills = skillRepository.findByAccount(account, pageable);
        List<Contact> contacts=account.getContacts();
        List<Contact> accepted= contacts.stream().filter(c -> c.getApproved()==true).collect(Collectors.toCollection(ArrayList::new));
        List<Contact> notYetAccepted= contacts.stream().filter(c -> c.getApproved()==false).collect(Collectors.toCollection(ArrayList::new)); 
       
        Map<String, List> accountModels = new HashMap<>();
        accountModels.put("accepted", accepted);
        accountModels.put("notYetAccepted", notYetAccepted);
        accountModels.put("skills", skills);
        return accountModels;
    }

    public void updateSkillList(Account account, Skill skill) {
        List<Skill> skills = account.getSkills();
        skills.add(skill);
        account.setSkills(skills);
        accountRepository.save(account);
    }

    public void create(Account account) {
        String password = account.getPassword();
        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
    }

    public void changeProfilePicture(byte[] bytes, String username) {
        Account account = accountRepository.findByUsername(username);
        account.setProfilePicture(bytes);
        accountRepository.save(account);        
    }
}
