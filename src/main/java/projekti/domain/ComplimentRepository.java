package projekti.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplimentRepository extends JpaRepository<Compliment, Long>{

    public Compliment findByAddedBy(String currentUserName);
    
}
