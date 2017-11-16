package ro.contezi.shopping.reply;

import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;

public class ShoppingListRemove implements ConditionalReplyProvider {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListView shoppingListView;
    private final LatestList latestList;
    
    public ShoppingListRemove(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView, LatestList latestList) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListView = shoppingListView;
        this.latestList = latestList;
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        String shoppingListId = latestList.get(messageFromFacebook.getSender().getId()).getId();
        String item = messageFromFacebook.getText().getText().substring("remove".length()).trim();
        shoppingListRepository.remove(shoppingListId, item);
        return shoppingListView.displayShoppingList(shoppingListRepository.get(shoppingListId));
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getText().toLowerCase().startsWith("remove");
    }

}
