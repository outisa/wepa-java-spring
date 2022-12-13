package projekti.domain;


import java.util.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    @EntityGraph(attributePaths = {"compliments"})
    public Skill getOne(Long id);
    
    @EntityGraph(attributePaths = {"compliments"})
    public List<Skill> findByAccount(Account account, Pageable pageable);
}
