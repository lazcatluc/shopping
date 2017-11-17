package ro.contezi.shopping.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LatestList {
    private final ShoppingListRepository shoppingListRepository;
    private final AuthorRepository authorRepository;
    
    public LatestList(ShoppingListRepository shoppingListRepository, AuthorRepository authorRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.authorRepository = authorRepository;
    }
    
    public ShoppingList get(String authorId) {
        authorRepository.getInitializedAuthor(authorId);
        Author author = authorRepository.findWithLists(authorId);
        List<ShoppingList> lists = new ArrayList<>();
        if (!author.getMyLists().isEmpty()) {
            lists.add(author.getMyLists().iterator().next());
        }
        if (!author.getListSharedWithMe().isEmpty()) {
            lists.add(author.getListSharedWithMe().iterator().next());
        }
        if (lists.isEmpty()) {
            ShoppingList list = new ShoppingList();
            list.setAuthor(author);
            return shoppingListRepository.save(list);
        }
        Collections.sort(lists, (list1, list2) -> list2.getCreatedDate().compareTo(list1.getCreatedDate()));
        return lists.get(0);
    }
}
