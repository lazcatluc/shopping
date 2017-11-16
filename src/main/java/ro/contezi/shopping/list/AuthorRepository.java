package ro.contezi.shopping.list;

public class AuthorRepository {
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
        return authorJpaRepository.save(author);
    }
}
