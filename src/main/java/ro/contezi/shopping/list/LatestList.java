package ro.contezi.shopping.list;

import java.util.List;

public class LatestList {
    private final ShoppingListRepository shoppingListRepository;
    private final AuthorRepository authorRepository;
    
    public LatestList(ShoppingListRepository shoppingListRepository, AuthorRepository authorRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.authorRepository = authorRepository;
    }
    
    public ShoppingList get(String authorId) {
        Author author = authorRepository.getInitializedAuthor(authorId);
        List<ShoppingList> lists = shoppingListRepository.findFirst1ByAuthorOrderByCreatedDateDesc(author);
        if (lists.isEmpty()) {
            ShoppingList list = new ShoppingList();
            list.setAuthor(author);
            return shoppingListRepository.save(list);
        }
        return lists.get(0);
    }
}
