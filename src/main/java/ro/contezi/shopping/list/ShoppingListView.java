package ro.contezi.shopping.list;

import java.util.Set;

public interface ShoppingListView {
    String displayShoppingList(Set<ShoppingListItem> shoppingList);
}
