package projekti.domain;

import java.util.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
 
    @EntityGraph(attributePaths = {"likers"})
    public List<Comment> findBySenderIn(Collection<String> senders, Pageable pageable);
}
