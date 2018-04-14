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
    private final Double cost;

    @JsonCreator
    public ShoppingItem(@JsonProperty("id") String id,
                        @JsonProperty("shoppingListId") String shoppingListId,
                        @JsonProperty("itemName") String itemName,
                        @JsonProperty("bought") boolean bought,
                        @JsonProperty("removed") boolean removed,
                        @JsonProperty("cost") Double cost) {
        this.id = id;
        this.shoppingListId = shoppingListId;
        this.itemName = itemName;
        this.bought = bought;
        this.removed = removed;
        this.cost = cost;
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
        this.cost = shoppingListItem.getCost();
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

    public Double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "ShoppingItem{" +
                "id='" + id + '\'' +
                ", shoppingListId='" + shoppingListId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", bought=" + bought +
                ", cost=" + cost +
                ", removed=" + removed +
                '}';
    }
}
