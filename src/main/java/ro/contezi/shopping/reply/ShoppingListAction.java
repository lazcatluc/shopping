package ro.contezi.shopping.reply;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Optional;
import ro.contezi.shopping.facebook.FacebookMessage;
import ro.contezi.shopping.facebook.FacebookQuickReply;
import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;

public abstract class ShoppingListAction implements ConditionalReplier {
    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListView shoppingListView;
    private final LatestList latestList;
    private final InformOthers informOthers;

    public ShoppingListAction(LatestList latestList, ShoppingListView shoppingListView,
                              InformOthers informOthers, ShoppingListRepository shoppingListRepository) {
        this.latestList = latestList;
        this.shoppingListView = shoppingListView;
        this.informOthers = informOthers;
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        ShoppingList shoppingList = latestList.get(messageFromFacebook.getSender().getId());
        String shoppingListId = shoppingList.getId();
        Arrays.stream(getText(messageFromFacebook)
                .substring(actionDescription().length()).trim().split(","))
                .map(ShoppingListAction::removeUnicode)
                .map(String::trim)
                .forEach(item -> executeAction(shoppingListId, item));
        String message = shoppingListView.displayShoppingList(getShoppingListRepository().get(shoppingListId));
        informOthers.informOthers(messageFromFacebook.getSender(), shoppingList, message);
        return message;
    }

    protected String getText(MessageFromFacebook messageFromFacebook) {
        FacebookQuickReply quickReply = messageFromFacebook.getText().getQuickReply();
        if (quickReply != null) {
            return quickReply.getPayload();
        }
        return Optional.ofNullable(messageFromFacebook.getText()).map(FacebookMessage::getText)
                .orElse("");
    }

    public static String removeUnicode(String source) {
        return Normalizer.normalize(source, Normalizer.Form.NFD)
                .replaceAll("[^\\x00-\\x7F]", "");
    }

    protected abstract void executeAction(String shoppingListId, String item);

    protected abstract String actionDescription();

    @Override
    public final boolean applies(MessageFromFacebook messageFromFacebook) {
        FacebookQuickReply quickReply = messageFromFacebook.getText().getQuickReply();
        if (quickReply != null && quickReply.getPayload().startsWith(actionDescription())) {
            return true;
        }
        return Optional.ofNullable(messageFromFacebook.getText()).map(FacebookMessage::getText)
                .orElse("").toLowerCase().startsWith(actionDescription()) &&
                appliesToRemainingText(latestList.get(messageFromFacebook.getSender().getId()),
                        getRemainingText(messageFromFacebook));
    }

    protected String getRemainingText(MessageFromFacebook messageFromFacebook) {
        String text = getText(messageFromFacebook).toLowerCase();
        if (text.length() <= actionDescription().length()) {
            return "";
        }
        return text.substring(actionDescription().length() + 1);
    }

    protected boolean appliesToRemainingText(ShoppingList shoppingList, String text) {
        return true;
    }

    protected ShoppingListRepository getShoppingListRepository() {
        return shoppingListRepository;
    }

    protected LatestList getLatestList() {
        return latestList;
    }
}
