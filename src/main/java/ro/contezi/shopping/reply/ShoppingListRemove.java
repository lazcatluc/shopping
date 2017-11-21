package ro.contezi.shopping.reply;

import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;

public class ShoppingListRemove extends ShoppingListAction {

    public ShoppingListRemove(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView,
                              LatestList latestList, InformOthers informOthers) {
        super(latestList, shoppingListView, informOthers, shoppingListRepository);
    }

    @Override
    protected void executeAction(String shoppingListId, String item) {
        getShoppingListRepository().remove(shoppingListId, item);
    }

    @Override
    protected String actionDescription() {
        return "remove";
    }

}
