package ro.contezi.shopping.list;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface AuthorJpaRepository extends JpaRepository<Author, String> {

}
