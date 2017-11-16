package ro.contezi.shopping.list;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InMemoryShoppingLists implements ShoppingListRepository {

    private final Map<String, ShoppingList> shoppingLists = new HashMap<>();

    @Override
    public void add(String shoppingListId, String item) {
        getInitialized(shoppingListId).addItem(item);
    }

    @Override
    public Set<ShoppingListItem> get(String shoppingListId) {
        return getInitialized(shoppingListId).getItems();
    }

    private ShoppingList getInitialized(String shoppingListId) {
        return shoppingLists.computeIfAbsent(shoppingListId, s -> {
            ShoppingList list = new ShoppingList();
            list.setId(shoppingListId);
            Author author = new Author();
            author.setId(shoppingListId);
            list.setAuthor(author);
            return list;
        });
    }

    @Override
    public void remove(String shoppingListId, String item) {
        getInitialized(shoppingListId).removeItem(item);
    }

    @Override
    public void buy(String shoppingListId, String item) {
        getInitialized(shoppingListId).buyItem(item);
    }

    @Override
    public String toString() {
        return shoppingLists.toString();
    }

    @Override
    public List<ShoppingList> findFirst1ByAuthorOrderByCreatedDateDesc(Author author) {
        return Collections.singletonList(getInitialized(author.getId()));
    }

    @Override
    public ShoppingList save(ShoppingList list) {
        shoppingLists.put(list.getId(), list);
        return list;
    }

}
