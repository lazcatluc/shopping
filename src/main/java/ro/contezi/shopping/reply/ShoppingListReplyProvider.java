package ro.contezi.shopping.reply;

import ro.contezi.shopping.ShoppingListRepository;
import ro.contezi.shopping.ShoppingListView;
import ro.contezi.shopping.model.MessageFromFacebook;

public class ShoppingListReplyProvider implements ConditionalReplyProvider {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListView shoppingListView;
    
    public ShoppingListReplyProvider(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListView = shoppingListView;
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getText().toLowerCase().startsWith("list");
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        return shoppingListView.displayShoppingList(
                shoppingListRepository.latestList(messageFromFacebook.getSender().getId()).getItems());
    }

}
