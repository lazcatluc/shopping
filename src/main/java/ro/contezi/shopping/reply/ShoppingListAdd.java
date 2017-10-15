package ro.contezi.shopping.reply;

import ro.contezi.shopping.ShoppingListRepository;
import ro.contezi.shopping.ShoppingListView;
import ro.contezi.shopping.model.MessageFromFacebook;

public class ShoppingListAdd implements ConditionalReplyProvider {
    
    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListView shoppingListView;
    
    public ShoppingListAdd(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListView = shoppingListView;
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        String shoppingListId = messageFromFacebook.getSender().getId();
        String item = messageFromFacebook.getText().getText().substring("add".length()).trim();
        shoppingListRepository.add(shoppingListId, item);
        return shoppingListView.displayShoppingList(shoppingListRepository.get(shoppingListId));
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getText().toLowerCase().startsWith("add");
    }

}
