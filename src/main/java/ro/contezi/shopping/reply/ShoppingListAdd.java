package ro.contezi.shopping.reply;

import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;

public class ShoppingListAdd implements ConditionalReplyProvider {
    
    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListView shoppingListView;
    private final LatestList latestList;
    private final InformOthers informOthers;

    public ShoppingListAdd(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView,
                           LatestList latestList, InformOthers informOthers) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListView = shoppingListView;
        this.latestList = latestList;
        this.informOthers = informOthers;
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        ShoppingList shoppingList = latestList.get(messageFromFacebook.getSender().getId());
        String shoppingListId = shoppingList.getId();
        String item = messageFromFacebook.getText().getText().substring("add".length()).trim();
        shoppingListRepository.add(shoppingListId, item);
        String message = shoppingListView.displayShoppingList(shoppingListRepository.get(shoppingListId));
        informOthers.informOthers(messageFromFacebook.getSender(), shoppingList, message);
        return message;
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getText().toLowerCase().startsWith("add");
    }

}
