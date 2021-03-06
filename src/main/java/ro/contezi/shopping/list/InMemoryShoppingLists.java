package ro.contezi.shopping.list;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import ro.contezi.shopping.author.Author;

public class InMemoryShoppingLists implements ShoppingListRepository {

    private final Map<String, ShoppingList> shoppingLists = new HashMap<>();

    @Override
    public ShoppingList find(String shoppingListId) {
        return getInitialized(shoppingListId);
    }

    @Override
    public ShoppingListItem add(String shoppingListId, String item) {
        return getInitialized(shoppingListId).addItem(item);
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
    public ShoppingListItem remove(String shoppingListId, String item) {
        return getInitialized(shoppingListId).removeItem(item);
    }

    @Override
    public ShoppingListItem buy(String shoppingListId, String item) {
        return getInitialized(shoppingListId).buyItem(item);
    }

    @Override
    public String toString() {
        return shoppingLists.toString();
    }

    @Override
    public ShoppingList saveShoppingList(ShoppingList list) {
        shoppingLists.put(list.getId(), list);
        return list;
    }

    @Override
    public ShoppingList share(String shoppingListId, Author author) {
        ShoppingList initialized = getInitialized(shoppingListId);
        initialized.shareWith(author);
        return initialized;
    }

}
