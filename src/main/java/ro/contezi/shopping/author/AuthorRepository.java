package ro.contezi.shopping.author;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;

public class AuthorRepository {
    
    private static final Logger LOGGER = Logger.getLogger(AuthorRepository.class);
    
    private final AuthorJpaRepository authorJpaRepository;
    private final GraphApi graphApi;
    
    public AuthorRepository(AuthorJpaRepository authorJpaRepository, GraphApi graphApi) { 
        this.authorJpaRepository = authorJpaRepository;
        this.graphApi = graphApi;
    }

    public Author getInitializedAuthor(String authorId) {
        Author author = authorJpaRepository.findOne(authorId);
        if (author != null) {
            return author;
        }
        author = graphApi.getUserInfo(authorId);
        try {
            return authorJpaRepository.save(author);
        }
        catch (DataIntegrityViolationException dive) {
            LOGGER.warn("Saved user " + author + " twice on two parallel messages", dive);
        }
        return author;
    }

    public List<Author> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName) {
        return authorJpaRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
    }

    public List<Author> findByFirstNameIgnoreCase(String name) {
        return authorJpaRepository.findByFirstNameIgnoreCase(name);
    }

    public Author findWithLists(String authorId) {
        return authorJpaRepository.findWithLists(authorId);
    }
}
