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
        shoppingList.stream().filter(item -> item.getBoughtDate() != null)
                .forEach(item -> sb.append("\n").append(item.getItemName()).append(check));
        shoppingList.stream().filter(item -> item.getBoughtDate() == null)
                .forEach(item -> sb.append("\n").append(item.getItemName()));

        return sb.toString();
    }

}
