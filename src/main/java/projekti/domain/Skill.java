package projekti.domain;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name="skill")
public class Skill extends AbstractPersistable<Long> {
    
    @NotEmpty
    @Size(min=2, max=100)
    private String skillContent;
    
    private Integer complimentsTotal;

    @OneToMany(mappedBy="skill")
    private List<Compliment> compliments = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
    
}
