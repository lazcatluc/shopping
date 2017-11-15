package ro.contezi.shopping.list;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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

    @Override
    public List<ShoppingList> findByAuthor(String author) {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setId(author);
        shoppingList.setAuthor(author);
        shoppingList.setItems(get(author));
        return Collections.singletonList(shoppingList);
    }

    @Override
    public ShoppingList save(ShoppingList list) {
        shoppingLists.put(list.getId(), list.getItems());
        return list;
    }

}
