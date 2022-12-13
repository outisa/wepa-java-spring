package projekti.controller;

import projekti.domain.Comment;
import projekti.domain.Account;
import java.util.*;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projekti.domain.AccountRepository;
import projekti.domain.CommentRepository;
import projekti.domain.SubComment;
import projekti.service.AccountService;
import projekti.service.CommentService;

@Controller
public class CommentController {
    
    @Autowired
    CommentRepository commentRepository;
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    CommentService commentService;
    
    @Autowired
    AccountService accountService;
    
    @GetMapping("/comments")
    public String viewComments(Model model, @ModelAttribute Comment comment, @ModelAttribute SubComment subComment) {
        model.addAttribute("comments", commentService.getComments());
        return "comments";
    }
    
    @Secured("USER")
    @PostMapping("/comments")
    public String saveComment(@Valid @ModelAttribute Comment comment, BindingResult bindingresult, @ModelAttribute SubComment subComment, Model model) {
        if (bindingresult.hasErrors()) {
            model.addAttribute("comments", commentService.getComments());            
            return "comments";
        }
        commentService.createComment(comment);
        return "redirect:/comments";
    }
    @Secured("USER")
    @RequestMapping("/comments/{id}/likes")
    public String addLike(@PathVariable Long id) {
        Comment comment = commentRepository.getOne(id);
        String username = currentUser();
        List<Account> likers = comment.getLikers();
        long controlAccount = likers.stream().filter(a -> a.getUsername().equals(username)).count();
        if (controlAccount >0) {
            return "redirect:/comments";            
        }
        Account account = accountRepository.findByUsername(username);        
        commentService.updateLikes(comment, account);
        accountService.updateLikedComments(comment, account);

        return "redirect:/comments";
    }
    
    @Secured("USER")
    @RequestMapping("/comments/{id}")
    public String addSubComment(@Valid @ModelAttribute SubComment subComment, BindingResult bindingResult, @PathVariable Long id,
            Model model, @ModelAttribute Comment comment ) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("comments", commentService.getComments());
            return "comments";        
        }
        Comment commentToUpdate = commentRepository.getOne(id);
        commentService.createSubComment(subComment, commentToUpdate);
        commentService.updateSubCommentList(subComment, commentToUpdate);      
        return "redirect:/comments";
    }
    
    private String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();       
    }

}
