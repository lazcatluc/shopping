package ro.contezi.shopping.reply;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;

public abstract class ShoppingListAction implements ConditionalReplyProvider {
    protected final ShoppingListRepository shoppingListRepository;
    protected final ShoppingListView shoppingListView;
    protected final LatestList latestList;
    protected final InformOthers informOthers;

    public ShoppingListAction(LatestList latestList, ShoppingListView shoppingListView, InformOthers informOthers, ShoppingListRepository shoppingListRepository) {
        this.latestList = latestList;
        this.shoppingListView = shoppingListView;
        this.informOthers = informOthers;
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        ShoppingList shoppingList = latestList.get(messageFromFacebook.getSender().getId());
        String shoppingListId = shoppingList.getId();
        Arrays.stream(messageFromFacebook.getText().getText()
                .substring(actionDescription().length()).trim().split(","))
                .map(ShoppingListAction::removeUnicode)
                .map(String::trim)
                .forEach(item -> executeAction(shoppingListId, item));
        String message = shoppingListView.displayShoppingList(getShoppingListRepository().get(shoppingListId));
        informOthers.informOthers(messageFromFacebook.getSender(), shoppingList, message);
        return message;
    }

    public static String removeUnicode(String source) {
        return Normalizer.normalize(source, Normalizer.Form.NFD)
                .replaceAll("[^\\x00-\\x7F]", "");
    }

    protected abstract void executeAction(String shoppingListId, String item);

    protected abstract String actionDescription();

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getText().toLowerCase().startsWith(actionDescription());
    }

    protected ShoppingListRepository getShoppingListRepository() {
        return shoppingListRepository;
    }
}
