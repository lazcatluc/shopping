package ro.contezi.shopping.reply.text;

import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingListView;
import ro.contezi.shopping.reply.text.ConditionalReplier;

public class ShoppingListReplier implements ConditionalReplier {

    private final ShoppingListView shoppingListView;
    private final LatestList latestList;
    
    public ShoppingListReplier(ShoppingListView shoppingListView, LatestList latestList) {
        this.shoppingListView = shoppingListView;
        this.latestList = latestList;
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getText().toLowerCase().startsWith("list");
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        return shoppingListView.displayShoppingList(latestList
                .get(messageFromFacebook.getSender().getId()).getItems());
    }

}
