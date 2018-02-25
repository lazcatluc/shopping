package ro.contezi.shopping.list.action.item;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.list.ShoppingListItem;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;
import ro.contezi.shopping.list.action.InformOthers;

public class ShoppingListBuy extends ShoppingListAction {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ShoppingListBuy(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView,
                           LatestList latestList, InformOthers informOthers,
                           SimpMessagingTemplate simpMessagingTemplate) {
        super(latestList, shoppingListView, informOthers, shoppingListRepository);
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void executeAction(String shoppingListId, String item) {
        ShoppingListItem shoppingListItem = getShoppingListRepository().buy(shoppingListId, item);
        ShoppingItem shoppingItem = new ShoppingItem(shoppingListItem);
        simpMessagingTemplate.convertAndSend("/topic/items/"+shoppingListId, shoppingItem);
    }

    @Override
    protected String actionDescription() {
        return "buy";
    }

    @Override
    protected boolean appliesToRemainingText(ShoppingList shoppingList, String text) {
        return text.contains(",") || getPartialMatches(shoppingList, text).isEmpty();
    }

    protected Set<String> getPartialMatches(ShoppingList shoppingList, String text) {
        Set<String> partialMatches = shoppingList.getItems().stream()
                .map(ShoppingListItem::getItemName)
                .filter(item -> item.toLowerCase().contains(text))
                .collect(Collectors.toSet());
        if (partialMatches.contains(text)) {
            return Collections.emptySet();
        }
        return partialMatches;
    }
}
