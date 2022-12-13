package projekti.controller;

import projekti.domain.Skill;
import projekti.domain.Compliment;
import projekti.domain.Account;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import projekti.domain.AccountRepository;
import projekti.domain.ComplimentRepository;
import projekti.domain.SkillRepository;
import projekti.service.AccountService;
import projekti.service.SkillService;

@Controller
public class AccountController {
   
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private ComplimentRepository complimentRepository;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private SkillService skillService;
    
    @GetMapping("/registration")
    public String showForm(@ModelAttribute Account account) {
        return "registration";
    }
   
    
    @PostMapping("/registration")
    public String createAccount(@Valid @ModelAttribute Account account, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "registration";
        }
        Account control =accountRepository.findByUsername(account.getUsername());
        if (control != null) {
            bindingResult.rejectValue("username", "error.username", "Username exists already, please try another one!");
            return "registration";           
        }
        accountService.create(account);
        return "redirect:/";
    }
    
    @GetMapping("/accounts/{username}")
    public String viewAccount(@PathVariable String username, Model model, @ModelAttribute Skill skill, @ModelAttribute Compliment compliment) {
        Account account = accountRepository.findByUsername(username);
        Map models = accountService.getAccountModels(account);
        model.addAttribute("accepted", models.get("accepted"));
        model.addAttribute("notYetAccepted", models.get("notYetAccepted"));
        model.addAttribute("skills", models.get("skills"));
        model.addAttribute("account", account);   
        return "account";
    }
    
    @GetMapping(path="/profilepicture/{username}", produces = "image/*")
    @ResponseBody
    public byte[] fetchProfilePicture(@PathVariable String username) {
        return accountRepository.findByUsername(username).getProfilePicture();
    }
    
    @Secured("USER")
    @PostMapping("/account/{username}/skill/compliment")
    public String addComplimentToSkill(@Valid@ModelAttribute Compliment compliment, BindingResult bindingResult,
            @PathVariable String username, @RequestParam Long skillId, Model model, @ModelAttribute Skill skill) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName();   
        Skill skillToUpdate = skillRepository.getOne(skillId);
        List<Compliment> compliments = skillToUpdate.getCompliments();
        long complimentControl = compliments.stream().filter(c-> c.getAddedBy().equals(currentUserName)).count();
        if (complimentControl != 0 || bindingResult.hasErrors()) {
            Account account = accountRepository.findByUsername(username);
            Map models = accountService.getAccountModels(account);
            if (complimentControl != 0) {
                bindingResult.rejectValue("praise", "error.praise", "You have already commented this skill!");
            }
            model.addAttribute("accepted", models.get("accepted"));            
            model.addAttribute("notYetAccepted", models.get("notYetAccepted"));
            model.addAttribute("skills", models.get("skills"));
            model.addAttribute("account", account);   
            return "account";            
        }
        compliment.setAddedBy(currentUserName);
        compliment.setSkill(skillToUpdate);
        complimentRepository.save(compliment);
        skillService.updateCompliments(skillToUpdate, compliment);
        return "redirect:/accounts/" + username;
    }
    
    @Secured("USER")
    @PostMapping("/account/{username}/skill")
    public String addNewSkill(@Valid @ModelAttribute Skill skill, BindingResult bindingresult,
              @ModelAttribute Compliment compliment, @PathVariable String username, Model model) {
        
        Account account = accountRepository.findByUsername(username);        
        if (bindingresult.hasErrors()) {  
            Map models = accountService.getAccountModels(account);
            model.addAttribute("accepted", models.get("accepted"));
            model.addAttribute("notYetAccepted", models.get("notYetAccepted"));
            model.addAttribute("skills", models.get("skills"));
            model.addAttribute("account", account);           
            return "account";
        }        
        skillService.createSkill(skill, account);
        accountService.updateSkillList(account, skill);

        return "redirect:/accounts/" +username;
    }
    
    @Secured("USER")
    @PostMapping("/account/{username}")
    public String saveProfilePicture(@PathVariable String username, @RequestParam("file") MultipartFile profilePicture) throws IOException {
        if(isCurrentUser(username)) {
            accountService.changeProfilePicture(profilePicture.getBytes(), username);         
        }
        return "redirect:/accounts/" +username;
    }
    
    @Secured("USER")
    @PostMapping("/account/{username}/removepicture")
    public String removePicture(@PathVariable String username) {
        if (isCurrentUser(username)) {
            accountService.changeProfilePicture(null, username);
        }
        return "redirect:/accounts/" +username;
    }
    
    private Boolean isCurrentUser( String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName();
        return currentUserName.equals(username);
    }
}
