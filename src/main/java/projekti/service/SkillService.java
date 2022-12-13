
package projekti.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.domain.Account;
import projekti.domain.Compliment;
import projekti.domain.Skill;
import projekti.domain.SkillRepository;

@Service
public class SkillService {
    
    @Autowired
    SkillRepository skillRepository;

    public void createSkill(Skill skill, Account account) {
        skill.setComplimentsTotal(0);
        skill.setAccount(account);
        skillRepository.save(skill);
    }

    public void updateCompliments(Skill skill, Compliment newCompliment) {
        List<Compliment> compliments = skill.getCompliments();
        compliments.add(newCompliment);
        skill.setCompliments(compliments);
        skill.setComplimentsTotal(skill.getComplimentsTotal()+1);
        skillRepository.save(skill);
    }
    
}
