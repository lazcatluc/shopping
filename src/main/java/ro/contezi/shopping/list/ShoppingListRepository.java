package ro.contezi.shopping.list;

import java.util.List;
import java.util.Set;

public interface ShoppingListRepository {
    void add(String shoppingListId, String item);
    void remove(String shoppingListId, String item);
    void buy(String shoppingListId, String item);
    ShoppingList save(ShoppingList list);
    Set<ShoppingListItem> get(String shoppingListId);
    List<ShoppingList> findFirst1ByAuthorOrderByCreatedDateDesc(String author);
    default ShoppingList latestList(String author) {
        List<ShoppingList> lists = findFirst1ByAuthorOrderByCreatedDateDesc(author);
        if (lists.isEmpty()) {
            ShoppingList list = new ShoppingList();
            list.setAuthor(author);
            return save(list);
        }
        return lists.get(0);
    }
}
