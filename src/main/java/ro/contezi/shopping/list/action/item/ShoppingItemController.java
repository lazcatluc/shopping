package ro.contezi.shopping.list.action.item;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class ShoppingItemController {
    private static final Logger LOGGER = getLogger(ShoppingItemController.class);

    private final ShoppingListAdd shoppingListAdd;
    private final ShoppingListBuy shoppingListBuy;
    private final ShoppingListRemove shoppingListRemove;
    private final ShoppingItemCost shoppingItemCost;

    @Autowired
    public ShoppingItemController(ShoppingListAdd shoppingListAdd, ShoppingListBuy shoppingListBuy,
                                  ShoppingListRemove shoppingListRemove, ShoppingItemCost shoppingItemCost) {
        this.shoppingListAdd = shoppingListAdd;
        this.shoppingListBuy = shoppingListBuy;
        this.shoppingListRemove = shoppingListRemove;
        this.shoppingItemCost = shoppingItemCost;
    }

    @MessageMapping("/item")
    public ShoppingItem receive(ShoppingItem shoppingItem) {
        LOGGER.info("Received item {} ", shoppingItem);
        if (shoppingItem.isRemoved()) {
            shoppingListRemove.buildReplies(shoppingItem.getShoppingListId(), shoppingItem);
            return shoppingItem;
        }
        if (shoppingItem.isBought()) {
            shoppingItemCost.setCost(shoppingItem.getShoppingListId(), shoppingItem);
            shoppingListBuy.buildReplies(shoppingItem.getShoppingListId(), shoppingItem);
            return shoppingItem;
        }
        shoppingListAdd.buildReplies(shoppingItem.getShoppingListId(), shoppingItem);
        return shoppingItem;
    }

}
