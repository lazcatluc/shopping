package ro.contezi.shopping.list.action.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShoppingItemCost {
    private final ShoppingListItemJpaRepository shoppingListItemJpaRepository;

    @Autowired
    public ShoppingItemCost(ShoppingListItemJpaRepository shoppingListItemJpaRepository) {
        this.shoppingListItemJpaRepository = shoppingListItemJpaRepository;
    }

    public void setCost(String shoppingListId, ShoppingItem shoppingItem) {
        shoppingListItemJpaRepository.findByShoppingList_idAndItemName(shoppingListId, shoppingItem.getItemName())
                .forEach(shoppingListItem -> {
                    shoppingListItem.setCost(shoppingItem.getCost());
                    shoppingListItemJpaRepository.save(shoppingListItem);
                });
    }
}
