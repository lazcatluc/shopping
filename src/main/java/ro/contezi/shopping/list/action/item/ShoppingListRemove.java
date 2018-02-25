package ro.contezi.shopping.list.action.item;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingListItem;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;
import ro.contezi.shopping.list.action.InformOthers;

public class ShoppingListRemove extends ShoppingListAction {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ShoppingListRemove(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView,
                              LatestList latestList, InformOthers informOthers,
                              SimpMessagingTemplate simpMessagingTemplate) {
        super(latestList, shoppingListView, informOthers, shoppingListRepository);
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void executeAction(String shoppingListId, String item) {
        ShoppingListItem shoppingListItem = getShoppingListRepository().remove(shoppingListId, item);
        ShoppingItem shoppingItem = new ShoppingItem(shoppingListItem, true);
        simpMessagingTemplate.convertAndSend("/topic/items/"+shoppingListId, shoppingItem);
    }

    @Override
    protected String actionDescription() {
        return "remove";
    }

}
