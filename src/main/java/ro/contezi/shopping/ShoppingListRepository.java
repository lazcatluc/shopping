package ro.contezi.shopping;

import java.util.Map;

public interface ShoppingListRepository {
    void add(String shoppingListId, String item);
    void remove(String shoppingListId, String item);
    void buy(String shoppingListId, String item);
    Map<String, Boolean> get(String shoppingListId);
}
