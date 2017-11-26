package ro.contezi.shopping.list.action;

import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;

public class ShoppingListAdd extends ShoppingListAction {

    public ShoppingListAdd(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView,
                           LatestList latestList, InformOthers informOthers) {
        super(latestList, shoppingListView, informOthers, shoppingListRepository);
    }

    @Override
    protected void executeAction(String shoppingListId, String item) {
        getShoppingListRepository().add(shoppingListId, item);
    }

    @Override
    protected String actionDescription() {
        return "add";
    }

}
