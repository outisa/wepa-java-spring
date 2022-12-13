
package projekti.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import projekti.domain.Account;
import projekti.domain.AccountRepository;
import projekti.domain.Comment;
import projekti.domain.CommentRepository;
import projekti.domain.Contact;
import projekti.domain.SubComment;
import projekti.domain.SubCommentRepository;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    
    @Autowired
    SubCommentRepository subCommentRepository;
    
    @Autowired
    AccountRepository accountRepository;
    
    
    public List<Comment> getComments() {
        String username = currentUser();
        Account account = accountRepository.findByUsername(username);
        List<Contact> co = account.getContacts();
        System.out.println(co.size());
        List<Contact> contacts = co.stream().filter(c -> c.getApproved()==true).collect(Collectors.toCollection(ArrayList::new));
        System.out.println(contacts.size());
        List<Account> accounts = new ArrayList<>();
        for (Contact contact: contacts) {
            accounts.add(contact.getAccounts().get(0));
            accounts.add(contact.getAccounts().get(1));            
        }
        accounts.add(account);
        List<String> senders = accounts.stream().map(a-> a.getUsername()).distinct().collect(Collectors.toCollection(ArrayList::new));   
        Pageable pageable =PageRequest.of(0, 25, Sort.by("localDateTime").descending());
        List<Comment> comments = commentRepository.findBySenderIn(senders, pageable);   
        Pageable pageable2 = PageRequest.of(0,10, Sort.by("localDateTime").descending());
        // Tämä ei ole optimaalinen tapa.
        comments.forEach((comment) -> {
            comment.setSubComments(subCommentRepository.findByComment(comment, pageable2));
        });
        return comments;
    }
    
    public void createComment(Comment comment) {
        ZoneId zoneID = ZoneId.of("Europe/Helsinki");
        comment.setLocalDateTime(LocalDateTime.now(zoneID));
        comment.setSender(currentUser());
        commentRepository.save(comment);
    }     

    public void updateLikes(Comment comment, Account account) {
        List<Account> likers = comment.getLikers();
        likers.add(account);
        comment.setLikers(likers);
        commentRepository.save(comment);        
    }

    public void createSubComment(SubComment subComment, Comment comment) {
        ZoneId zoneID = ZoneId.of("Europe/Helsinki");    
        subComment.setLocalDateTime(LocalDateTime.now(zoneID));
        subComment.setSender(currentUser());
        subComment.setComment(comment);
        subCommentRepository.save(subComment);               
    }

    public void updateSubCommentList(SubComment subComment, Comment comment) {
        List<SubComment> subComments = comment.getSubComments();
        subComments.add(subComment);
        comment.setSubComments(subComments);
        commentRepository.save(comment);  
    }
    
    private String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();       
    }  

}
