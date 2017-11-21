package ro.contezi.shopping.reply;

import ro.contezi.shopping.list.LatestList;
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

}
