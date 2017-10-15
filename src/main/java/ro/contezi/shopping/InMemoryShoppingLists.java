package ro.contezi.shopping;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryShoppingLists implements ShoppingListRepository {
    
    private final Map<String, Map<String, Boolean>> shoppingLists = new HashMap<>();

    @Override
    public void add(String shoppingListId, String item) {
        get(shoppingListId).computeIfAbsent(item, s -> false);
    }

    @Override
    public Map<String, Boolean> get(String shoppingListId) {
        return shoppingLists.computeIfAbsent(shoppingListId, s -> new LinkedHashMap<>());
    }

    @Override
    public void remove(String shoppingListId, String item) {
        get(shoppingListId).remove(item);
    }

    @Override
    public void buy(String shoppingListId, String item) {
        get(shoppingListId).put(item, true);
    }

    @Override
    public String toString() {
        return shoppingLists.toString();
    }

}
