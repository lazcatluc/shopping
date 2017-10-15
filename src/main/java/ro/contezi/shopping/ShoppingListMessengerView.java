package ro.contezi.shopping;

import java.util.Map;

public class ShoppingListMessengerView implements ShoppingListView {
    
    private final String check = " ✔";

    @Override
    public String displayShoppingList(Map<String, Boolean> shoppingList) {
        if (shoppingList.isEmpty()) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        shoppingList.forEach((item, bought) -> sb.append("\n").append(item).append(bought ? check : ""));
        return sb.toString();
    }

}
