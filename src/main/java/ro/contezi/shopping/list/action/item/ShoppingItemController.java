package ro.contezi.shopping.list.action.item;

import org.slf4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;

import static org.slf4j.LoggerFactory.getLogger;

public class ShoppingItemController {
    private static final Logger LOGGER = getLogger(ShoppingItemController.class);

    private final ShoppingListAdd shoppingListAdd;
    private final ShoppingListBuy shoppingListBuy;
    private final ShoppingListRemove shoppingListRemove;

    public ShoppingItemController(ShoppingListAdd shoppingListAdd,
                                  ShoppingListBuy shoppingListBuy,
                                  ShoppingListRemove shoppingListRemove) {
        this.shoppingListAdd = shoppingListAdd;
        this.shoppingListBuy = shoppingListBuy;
        this.shoppingListRemove = shoppingListRemove;
    }

    @MessageMapping("/item")
    public void receive(ShoppingItem shoppingItem) {
        LOGGER.info("Received item {} ", shoppingItem);
        if (shoppingItem.isRemoved()) {
            shoppingListRemove.buildReplies(shoppingItem.getShoppingListId(), shoppingItem);
            return;
        }
        if (shoppingItem.isBought()) {
            shoppingListBuy.buildReplies(shoppingItem.getShoppingListId(), shoppingItem);
            return;
        }
        shoppingListAdd.buildReplies(shoppingItem.getShoppingListId(), shoppingItem);
    }

}
