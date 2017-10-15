package ro.contezi.shopping.reply;

import ro.contezi.shopping.ShoppingListRepository;
import ro.contezi.shopping.ShoppingListView;
import ro.contezi.shopping.model.MessageFromFacebook;

public class ShoppingListRemove implements ConditionalReplyProvider {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListView shoppingListView;
    
    public ShoppingListRemove(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListView = shoppingListView;
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        String shoppingListId = messageFromFacebook.getSender().getId();
        String item = messageFromFacebook.getText().getText().substring("remove".length()).trim();
        shoppingListRepository.remove(shoppingListId, item);
        return shoppingListView.displayShoppingList(shoppingListRepository.get(shoppingListId));
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getText().toLowerCase().startsWith("remove");
    }

}
