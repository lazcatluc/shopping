package ro.contezi.shopping.list;

import java.util.Set;

public interface ShoppingListRepository {
    void add(String shoppingListId, String item);
    void remove(String shoppingListId, String item);
    void buy(String shoppingListId, String item);

    ShoppingList saveShoppingList(ShoppingList list);
    Set<ShoppingListItem> get(String shoppingListId);
    ShoppingList share(String shoppingListId, Author author);
}
