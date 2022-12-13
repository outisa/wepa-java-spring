package projekti.domain;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name = "account")
public class Account extends AbstractPersistable<Long> {
    
    
    @NotEmpty
    @Size(min = 6, max=30)
    private String name;
    
    @NotEmpty
    @Size(min = 6, max=20)
    private String username;
    @NotEmpty
    @Size(min = 6, max=100)    
    private String password;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name="PROFILEPICTURE", columnDefinition = "bytea")
    private byte[] profilePicture;
    
    @OneToMany(mappedBy="account")
    private List<Skill> skills;
    
    @ManyToMany (mappedBy="accounts")
    private List<Contact> contacts = new ArrayList<>();
    
    @ManyToMany
    private List<Comment> likedComments = new ArrayList<>();
}
