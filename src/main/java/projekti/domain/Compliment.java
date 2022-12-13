package projekti.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "compliment")
public class Compliment extends AbstractPersistable<Long> {
    
    @NotEmpty
    @Size(min=2, max=100)
    private String praise;
    private String addedBy;
    
    @ManyToOne
    private Skill skill;
}