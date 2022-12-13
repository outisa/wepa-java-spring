package projekti.domain;

import java.util.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
    
    List<SubComment> findByComment(Comment comment, Pageable pageable);
}
