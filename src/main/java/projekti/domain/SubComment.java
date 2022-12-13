package projekti.domain;

import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "subcomment")
public class SubComment extends AbstractPersistable<Long> {
    
    @NotEmpty
    @Size(min=1, max=500)
    @Column(name="SUBCONTENT", columnDefinition = "text")    
    private String subcontent;
    private LocalDateTime localDateTime;
    private String sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;
}