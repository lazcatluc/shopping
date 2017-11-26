package ro.contezi.shopping.author;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface AuthorJpaRepository extends JpaRepository<Author, String> {

    List<Author> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    List<Author> findByFirstNameIgnoreCase(String name);
    
    default Author findWithLists(String authorId) {
        Author author = findOne(authorId);
        author.getListSharedWithMe().size();
        author.getMyLists().size();
        return author;
    }

}
