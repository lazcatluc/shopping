package ro.contezi.shopping.list;

import java.util.Set;
import ro.contezi.shopping.author.Author;

public interface ShoppingListRepository {
    ShoppingList find(String shoppingListId);
    ShoppingListItem add(String shoppingListId, String item);
    ShoppingListItem remove(String shoppingListId, String item);
    ShoppingListItem buy(String shoppingListId, String item);

    ShoppingList saveShoppingList(ShoppingList list);
    Set<ShoppingListItem> get(String shoppingListId);
    ShoppingList share(String shoppingListId, Author author);
}
