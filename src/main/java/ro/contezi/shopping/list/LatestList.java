package ro.contezi.shopping.list;

import java.time.ZonedDateTime;
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
        List<DatedList> lists = new ArrayList<>();
        if (!author.getMyLists().isEmpty()) {
            lists.add(new DatedList(author.getMyLists().iterator().next()));
        }
        if (!author.getListSharedWithMe().isEmpty()) {
            lists.add(new DatedList(author.getListSharedWithMe().iterator().next()));
        }
        if (lists.isEmpty()) {
            ShoppingList list = new ShoppingList();
            list.setAuthor(author);
            return shoppingListRepository.saveShoppingList(list);
        }
        Collections.sort(lists, (list1, list2) -> list2.getDate().compareTo(list1.getDate()));
        return lists.get(0).getList();
    }

    private static class DatedList {
        private final ZonedDateTime date;
        private final ShoppingList list;

        private DatedList(ShoppingList list) {
            this.date = list.getCreatedDate();
            this.list = list;
        }

        private DatedList(SharedList list) {
            this.date = list.getShareDate();
            this.list = list.getShoppingList();
        }

        ZonedDateTime getDate() {
            return date;
        }

        ShoppingList getList() {
            return list;
        }
    }
}
