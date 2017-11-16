package ro.contezi.shopping.list;

import java.util.List;
import java.util.Set;

public interface ShoppingListRepository {
    void add(String shoppingListId, String item);
    void remove(String shoppingListId, String item);
    void buy(String shoppingListId, String item);
    ShoppingList save(ShoppingList list);
    Set<ShoppingListItem> get(String shoppingListId);
    List<ShoppingList> findFirst1ByAuthorOrderByCreatedDateDesc(Author author);
}
