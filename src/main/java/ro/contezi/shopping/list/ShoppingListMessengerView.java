package ro.contezi.shopping.list;

import java.util.Set;

public class ShoppingListMessengerView implements ShoppingListView {
    
    private final String check = " ✔";

    @Override
    public String displayShoppingList(Set<ShoppingListItem> shoppingList) {
        if (shoppingList.isEmpty()) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        shoppingList.forEach(item -> sb.append("\n").append(item.getItemName()).append(item.getBoughtDate() != null ? check : ""));
        return sb.toString();
    }

}
