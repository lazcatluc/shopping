package ro.contezi.shopping.list.action.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ro.contezi.shopping.list.ShoppingListItem;

public class ShoppingItem {
    private final String id;
    private final String shoppingListId;
    private final String itemName;
    private final boolean bought;
    private final boolean removed;

    @JsonCreator
    public ShoppingItem(@JsonProperty("id") String id,
                        @JsonProperty("shoppingListId") String shoppingListId,
                        @JsonProperty("itemName") String itemName,
                        @JsonProperty("bought") boolean bought,
                        @JsonProperty("removed") boolean removed) {
        this.id = id;
        this.shoppingListId = shoppingListId;
        this.itemName = itemName;
        this.bought = bought;
        this.removed = removed;
    }

    public ShoppingItem(ShoppingListItem shoppingListItem) {
        this(shoppingListItem, false);
    }

    public ShoppingItem(ShoppingListItem shoppingListItem, boolean removed) {
        this.id = shoppingListItem.getId();
        this.shoppingListId = shoppingListItem.getShoppingList().getId();
        this.itemName = shoppingListItem.getItemName();
        this.bought = shoppingListItem.getBoughtDate() != null;
        this.removed = removed;
    }

    public String getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isBought() {
        return bought;
    }

    public boolean isRemoved() {
        return removed;
    }

    public String getShoppingListId() {
        return shoppingListId;
    }

    @Override
    public String toString() {
        return "ShoppingItem{" +
                "id='" + id + '\'' +
                ", shoppingListId='" + shoppingListId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", bought=" + bought +
                '}';
    }
}
