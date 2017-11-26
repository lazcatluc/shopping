package ro.contezi.shopping.list.action;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.list.ShoppingListItem;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;

public class ShoppingListBuy extends ShoppingListAction {

    public ShoppingListBuy(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView,
                           LatestList latestList, InformOthers informOthers) {
        super(latestList, shoppingListView, informOthers, shoppingListRepository);
    }

    @Override
    protected void executeAction(String shoppingListId, String item) {
        getShoppingListRepository().buy(shoppingListId, item);
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
