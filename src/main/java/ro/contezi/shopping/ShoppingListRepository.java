package ro.contezi.shopping;

import java.util.List;
import java.util.Map;

public interface ShoppingListRepository {
    void add(String shoppingListId, String item);
    void remove(String shoppingListId, String item);
    void buy(String shoppingListId, String item);
    ShoppingList save(ShoppingList list);
    Map<String, Boolean> get(String shoppingListId);
    List<ShoppingList> findByAuthor(String author);
    default ShoppingList latestList(String author) {
        List<ShoppingList> lists = findByAuthor(author);
        if (lists.isEmpty()) {
            ShoppingList list = new ShoppingList();
            list.setAuthor(author);
            return save(list);
        }
        return lists.get(lists.size() - 1);
    }
}
