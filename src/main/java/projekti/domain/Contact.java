package projekti.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.domain.Account;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name="contact")
public class Contact extends AbstractPersistable<Long>{
    private Boolean approved;
    private String adder;
    
    @ManyToMany
    private  List<Account> accounts;
}
