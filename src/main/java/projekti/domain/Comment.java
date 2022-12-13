package projekti.domain;

import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "Comment")
public class Comment extends AbstractPersistable<Long> {
    
    @NotEmpty
    @Size(min=1, max=1000)
    @Column(name="content", columnDefinition = "text", length = 1000)    
    private String content;
    private LocalDateTime localDateTime;
    private String sender;
    
    @ManyToMany(mappedBy="likedComments")
    private List<Account> likers = new ArrayList<>();
    
    @OneToMany(mappedBy="comment")
    private List<SubComment> subComments = new ArrayList<>();
    
}